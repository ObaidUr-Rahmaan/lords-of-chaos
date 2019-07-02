package com.lordsofchaos.networking.game.client;

import com.lordsofchaos.gamelogic.transmission.GameState;
import com.lordsofchaos.gamelogic.transmission.PowerUpState;
import com.lordsofchaos.gamelogic.transmission.Sprite;
import com.lordsofchaos.gamelogic.transmission.SpriteType;
import com.lordsofchaos.graphics.*;
import com.lordsofchaos.ui.TeamProject;
import com.sun.glass.ui.Application;
import com.sun.glass.ui.Robot;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Deals with Drawing of the Graphics on to the Clients screen
 *
 * (Edited and further improved by Ben)
 *
 * @author Obaid Ur-Rahmaaan
 */
public class IntegratedSDP extends Task {

    private static boolean DISPLAY_FRAME_RATE = true;
    private final String title;
    //private static boolean DISPLAY_FRAME_RATE = false;
    private Stage stage;
    private Scene scene;
    private Maze maze;
    private Tank tank;
    private ArrayList<Cell> allCells;
    private ArrayList<Bullet> bullets;
    private ArrayList<Tank> tanks;
    private BorderPane root;
    private HashMap<Integer, GraphicObject> serverMapping;
    private List<TankColor> colorList;
    private Text frameText;
    private javafx.scene.shape.Rectangle healthDisplay;
    private javafx.scene.shape.Rectangle healthBacking;
    private int playerHealth;
    private Point tankPos;
    private Integer playersId = -2;
    private boolean delayActive = false;
    private Point mouseLocation;
    private Robot robot;
    private HashMap<Integer,TankColor> tankColors = new HashMap<>();
    private HashMap<Integer,PowerUpState> powerUps = new HashMap<>();
    private Stage rootPane;
    private long endTime;
    private AnimationTimer timer;
    private javafx.application.Application myApp;
    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0 ;
    private boolean arrayFilled = false ;
    double frameRate = 0.0;

    private boolean singlePlayer;

    public IntegratedSDP(Stage stage, Scene scene, BorderPane root, Maze maze, Tank tank,
                         ArrayList<Cell> allCells, ArrayList<Bullet> bullets, ArrayList<Tank> tanks,
                         HashMap<Integer, GraphicObject> serverMapping, List<TankColor> colorList, Stage rootPane, javafx.application.Application myApp, boolean singlePlayer) {
        this.stage = stage;
        this.scene = scene;
        this.root = root;
        this.maze = maze;
        this.tank = tank;
        this.allCells = allCells;
        this.bullets = bullets;
        this.tanks = tanks;
        this.serverMapping = serverMapping;
        this.colorList = colorList;

        /*
        Display health bar
         */
        this.healthBacking = new javafx.scene.shape.Rectangle(25, 25, 50, 10);
        healthBacking.setFill(Color.RED);
        this.singlePlayer = singlePlayer;
        this.healthDisplay = new javafx.scene.shape.Rectangle(25, 25, 50, 10);
        this.healthDisplay.setCache(true);
        this.healthBacking.setCache(true);
        healthDisplay.setFill(Color.GREEN);

        if(singlePlayer){
            root.getChildren().add(healthBacking);
            root.getChildren().add(healthDisplay);
        }
        this.rootPane =rootPane;
        this.myApp = myApp;

        //if (DISPLAY_FRAME_RATE) {
            //root.getChildren().add(frameText);
        //}
        scene.setCursor(Cursor.CROSSHAIR);
        robot = Application.GetApplication().createRobot();
        title = stage.getTitle();



    }

    @Override
    protected Object call() throws Exception {
        serverMapping = new HashMap<>();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                try {
                    update();

                        /*
                        Frame rate calculation
                         */
                        long oldFrameTime = frameTimes[frameTimeIndex] ;
                        frameTimes[frameTimeIndex] = now ;
                        frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length ;
                        if (frameTimeIndex == 0) {
                            arrayFilled = true ;
                        }

                    if (arrayFilled) {
                        long elapsedNanos = now - oldFrameTime ;
                        long elapsedNanosPerFrame = elapsedNanos / frameTimes.length ;
                        frameRate = 1_000_000_000.0 / elapsedNanosPerFrame ;
                        //System.out.println(String.format("Current frame rate: %.3f", frameRate));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        this.timer = timer;

        /*
        Adjust window and scale to fit screen
         */
        adjustScreen();
        timer.start();

        return new Object();
    }

    //--------------------------------------------------------------------------------------------

    /**
     * Updates all the objects in the game by drawing them on to the screen
     * based on the latest Game State received from the Server.
     * @throws InterruptedException
     */
    public void update() throws InterruptedException {

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        //Get game state
        ArrayList<Integer> ObjectId = new ArrayList<>();
        GameState latestState = ClientReceiver.latestState;
        CopyOnWriteArrayList<Sprite> gameStateObj = latestState.getObjects();

        //Display frame rate in window title
        stage.setTitle(title+ " - FPS: " + Integer.toString((int) Math.round(frameRate)));

        if (latestState != null && !latestState.gameOver) {


            /*
            Get user health, and adjust health bar
             */
            Integer stateHealth = latestState.getHealth();
            if (stateHealth != null) {
                if(stateHealth>0 && singlePlayer) {

                    this.healthDisplay.setWidth(stateHealth / 2);
                }
                playerHealth = stateHealth;
            }
            try {
                playersId = latestState.getPlayersId();
            } catch (NullPointerException e) {
                System.out.println("no connection yet");
            }



            /*
            Iterate all sprites, create and render
             */
            for (Sprite e : gameStateObj) {
                ObjectId.add(e.id);

                //Check if sprite already on screen
                if (serverMapping.containsKey(e.id)) {

                    //Update location
                    serverMapping.get(e.id).setLocation(e.location);

                    if(e.image == SpriteType.PLAYER_BASIC){
                        serverMapping.get(e.id).setBarrelRotation(e.turretRot);
                    }

                    /*
                    Rendering this users tank
                     */
                    if (e.image == SpriteType.PLAYER_BASIC && e.id == playersId){

                        serverMapping.get(e.id).setRotation(e.rotation,false);
                        tankPos = e.location;

                        healthBacking.setX(tankPos.getX()-12);
                        healthBacking.setY(tankPos.getY()- 20);
                        healthDisplay.setX(tankPos.getX()-12);
                        healthDisplay.setY(tankPos.getY()-20);

                        double mouseX = robot.getMouseX();
                        double mouseY = robot.getMouseY();

                        Bounds barrelLocation = serverMapping.get(e.id).getBarrelLocation();

                        if (barrelLocation != null){
                            float xDistance = (float) (mouseX - barrelLocation.getMinX());
                            float yDistance = (float) (barrelLocation.getMinY() - mouseY);
                            double angle = (360 + Math.toDegrees(Math.atan2(xDistance, yDistance))) % 360;

                        }

                    }else{
                        serverMapping.get(e.id).setRotation(e.rotation,false);
                    }


                    /*
                    Powerup handling
                     */
                    if(e.image == SpriteType.PLAYER_BASIC){
                        if (e.state == PowerUpState.normal ){
                            if (powerUps.get(e.id) != PowerUpState.normal){
                                serverMapping.get(e.id).endPowerUp();
                                powerUps.put(e.id,PowerUpState.normal);
                            }
                        } else {
                            if (e.state != powerUps.get(e.id)) {
                                serverMapping.get(e.id).setPowerUp(e.state);
                                powerUps.put(e.id, e.state);
                            }
                        }

                    }




                /*
                Executed if new sprite, not on screen yet
                 */
                } else {
                    if (e.image == SpriteType.PLAYER_BASIC) {

                        /*
                        Get colour for tank from list
                         */
                        TankColor thisColor;

                        if (tankColors.get(e.id )!= null){
                            thisColor = tankColors.get(e.id);
                        }
                        else {
                            try{
                                thisColor = colorList.get(0);
                            } catch(Exception e1){
                                thisColor = TankColor.blue;
                            }

                        }

                        /*
                        Initialise new tank
                         */
                        Tank newTank = new Tank(e.id, thisColor, e.rotation);
                        tankColors.put(e.id,thisColor);
                        if(colorList.size()>1){
                            colorList.remove(thisColor);
                        }
                        newTank.setLocation(e.location);
                        if (e.id != playersId){
                            newTank.removeIndic();
                        }
                        serverMapping.put(e.id, newTank);

                        //Render to screen
                        root.getChildren().add(newTank.getView());

                        /*
                        Bullet and rocket handling
                         */
                    } else if (e.image == SpriteType.BULLET || e.image == SpriteType.ROCKET) {

                        Bullet newBullet;

                        if (powerUps.get(e.owner) != PowerUpState.rocket){
                            newBullet = new Bullet(e.id,tankColors.get(e.owner));
                        } else {
                            newBullet = new Bullet(e.id,tankColors.get(e.owner),true);
                        }

                        newBullet.setRotation(e.rotation,false);
                        newBullet.setLocation(e.location);
                        serverMapping.put(e.id, newBullet);
                        root.getChildren().add(newBullet.getView());

                        /*
                        Powerup box handling
                         */
                    } else if (e.image == SpriteType.PICKUP_CRATE) {
                        PowerUp newPowerUp = new PowerUp(e.id);
                        newPowerUp.setLocation(e.location);
                        serverMapping.put(e.id, newPowerUp);
                        root.getChildren().add(newPowerUp.getView());

                        /*
                        Explosion on impact handling
                         */
                    } else if (e.image == SpriteType.EXPLOSION) {
                        Explosion newExplosion = new Explosion(e.id);
                        newExplosion.setLocation(e.location);
                        serverMapping.put(e.id, newExplosion);
                        root.getChildren().add(newExplosion.getView());
                    }
                }
            }

            //Remove objects that are no longer meant to be on screen
            killAllObjects(ObjectId, gameStateObj);

        }

        /*
        Game over handling, clearing the screen
         */
        if (latestState.gameOver) {
            if (!delayActive) {
                this.endTime = System.nanoTime();
                delayActive = true;
            }
            boolean alive = false;
            for(Sprite e : gameStateObj){
                if(e.id == playersId){
                    alive = true;
                }
            }
            killAllObjects(ObjectId, gameStateObj);
            root.getChildren().remove(healthDisplay);
            root.getChildren().remove(healthBacking);

            //Blur the screen for effect
            maze.setEffect(new GaussianBlur(50));
            VBox gameOverRoot = new VBox(5);
            Text gameOverText = new Text(650, 300, "You Lose!");
            if(alive){
                gameOverText.setText("You Win!");
            }

            gameOverText.setTextAlignment(TextAlignment.CENTER);
            gameOverText.setFont(Font.font("PT Sans", FontWeight.BOLD, 80));

            root.getChildren().add(gameOverText);
            gameOverRoot.setAlignment(Pos.CENTER);
            gameOverRoot.setPadding(new Insets(20));


            //THE CODE TO EXIT TO MENU
            if((System.nanoTime() - endTime) > 1000000000l) {
                TeamProject tp = TeamProject.tp;
                try {
                    tp.reInitilize(rootPane);
                    timer.stop();
                    myApp.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            delayActive = true;
        }
    }

    /**
     * Removes all objects from the Game once Game is over
     *
     * @param objectId List of integer ID's of each object in the GameState
     * @param gameStateObj List of all the physical objects of each Sprite in teh GameState
     */
    private void killAllObjects(ArrayList<Integer> objectId, CopyOnWriteArrayList<Sprite> gameStateObj) {
        Iterator it = serverMapping.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (!objectId.contains(pair.getKey())) {
                GraphicObject graphObj = (GraphicObject) pair.getValue();
                root.getChildren().remove(graphObj.getView());
                it.remove();
                serverMapping.remove(pair.getKey());
                gameStateObj.remove(pair.getValue());
            }
            if (playerHealth < 0 && singlePlayer) {
                root.getChildren().remove(healthDisplay);
                root.getChildren().remove(healthBacking);

            }
        }
        objectId.clear();
    }

    /**
    Adjust screen to fit user window
     */
    private void adjustScreen(){

        //stage.setMaximized(true);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        int initWidth = 1800;
        int initHeight = 900;

        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        double width = resolution.getWidth();
        double height = resolution.getHeight();

        double w = width/initWidth;  //your window width
        double h = height/initHeight;  //your window hight
        Scale scale = new Scale(w, h, 0, 0);
        scale.xProperty().bind(root.widthProperty().divide(initWidth));
        scale.yProperty().bind(root.heightProperty().divide(initHeight));
        root.getTransforms().add(scale);

        /*
        Listener to see if window resized, and if so rescale the content
         */
        scene.rootProperty().addListener(new ChangeListener<Parent>(){
            @Override public void changed(ObservableValue<? extends Parent> arg0, Parent oldValue, Parent newValue){
                scene.rootProperty().removeListener(this);
                scene.setRoot(root);
                ((Region)newValue).setPrefWidth(initWidth);     //make sure is a Region!
                ((Region)newValue).setPrefHeight(initHeight);   //make sure is a Region!
                root.getChildren().clear();
                root.getChildren().add(newValue);
                scene.rootProperty().addListener(this);
            }
        });
    }
}
