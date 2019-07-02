package com.lordsofchaos.networking.game.client;

import com.lordsofchaos.gamelogic.GameWorld;
import com.lordsofchaos.gamelogic.transmission.GameState;
import com.lordsofchaos.graphics.*;
import com.lordsofchaos.networking.global.Constants;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main GameClient Application Window - Displays Map and Client Character
 *
 * @author Obaid Ur-Rahmaan
 */
public class GameClient extends Application {

    private Socket TCPSocket;
    private DatagramSocket UDPSocket;

    // Streams
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private GameWorld gameWorld;

    // All Client Objects on Screen
    //---------------------------------------------------------
    private Scene scene;
    private Maze maze;
    private Tank tank;
    protected ArrayList<Cell> allCells;
    private ArrayList<Bullet> bullets;
    private ArrayList<Tank> tanks;
    private BorderPane root = new BorderPane();
    private GameState latestState;
    private HashMap<Integer, GraphicObject> serverMapping;
    private List<TankColor> colorList;
    private ExecutorService gameClientHandler;
    private Text loadingText;

    private boolean singlePlayer;
    private String SERVER_IP;

    public GameClient(boolean singlePlayer) {
        this.singlePlayer = singlePlayer;
    }

    public GameClient(boolean singlePlayer, String hostIp) {
        this.singlePlayer = singlePlayer;
        this.SERVER_IP = hostIp;
    }

    //-----------------------------------------------------------


    public Socket getTCPSocket() {
        return TCPSocket;
    }

    public DatagramSocket getUDPSocket() {
        return UDPSocket;
    }


    //------------------------------------------------------------

    private Stage window;

    //------------------------------------------------------------


    /**
     * Creates a new window for the GameClient
     *
     * @return Parent root object
     */
    private Parent createContent() {
//        this.root.setPrefSize(400, 300);
        //System.out.println(maze.getBoundsInLocal());s
        this.root.getChildren().add(maze);

//        loadingText = new Text(650, 300, "Loading...");
//        loadingText.setTextAlignment(TextAlignment.CENTER);
//        loadingText.setFont(Font.font("PT Sans", FontWeight.BOLD, 80));
//        root.getChildren().add(loadingText);

        //this.maze.createMaze();

        return root;
    }

    /**
     * Sets the stage
     *
     * @param window Stage to set
     */
    public void giveStage(Stage window) {
        this.window = window;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Lord of Chaos");

        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        this.maze = new Maze(Background.GRASS);
        this.colorList = new LinkedList<>(Arrays.asList(TankColor.values()));
        this.colorList.addAll(Arrays.asList(TankColor.values()));
        this.scene = new Scene(createContent(),screenSize.getWidth(),screenSize.getHeight());

        float centreScreenXConstant = 1.0f / 2;
        float centreScreenYConstant = 1.0f / 3;


        Screen screen = Screen.getPrimary();
        Bounds bounds = maze.getBoundsInParent();

        Rectangle2D boundsScreen = screen.getVisualBounds();

        double centerX = boundsScreen.getMinX() + (boundsScreen.getWidth() - bounds.getWidth()) * centreScreenXConstant;
        double centerY = boundsScreen.getMinY() + (boundsScreen.getHeight() - bounds.getHeight() + 29) * centreScreenYConstant;

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //System.out.println("Bounds:");
        //System.out.println(bounds.getWidth());
        //System.out.println(bounds.getHeight());
        /*primaryStage.setResizable(true);
        primaryStage.setX(centerX);
        primaryStage.setY(centerY);
        primaryStage.setWidth(bounds.getWidth() + 15);
        primaryStage.setHeight(bounds.getHeight() + 32);*/
        //primaryStage.setX(primaryScreenBounds.getMinX());
        //primaryStage.setY(primaryScreenBounds.getMinY());
        //primaryStage.setWidth(primaryScreenBounds.getWidth());
        //primaryStage.setHeight(primaryScreenBounds.getHeight());
        primaryStage.minWidthProperty().bind(scene.heightProperty().multiply(1));
        primaryStage.minHeightProperty().bind(scene.widthProperty().divide(1.77));

        primaryStage.setMaximized(true);

        //primaryStage.setResizable(true);



        // Create Sockets + Streams
        try {
            this.UDPSocket = new DatagramSocket();
            // Send Blank Packet to Server so Server can store UDP port of this DatagramSocket (i.e. to Receive GameStates on ClientReceiver later)
            UUID myRandomID = UUID.randomUUID();
            byte[] buffer = new byte[10];
            buffer = SerializationUtils.serialize(myRandomID);
            if (this.singlePlayer) {
                this.UDPSocket.send(new DatagramPacket(buffer, buffer.length, InetAddress.getByName(Constants.SINGLE_PLAYER_SERVER_IP), Constants.UDP_SERVER_PORT));
                this.TCPSocket = new Socket(Constants.SINGLE_PLAYER_SERVER_IP, Constants.TCP_SERVER_PORT);
                this.outputStream = new ObjectOutputStream(TCPSocket.getOutputStream());
                this.inputStream = new ObjectInputStream(TCPSocket.getInputStream());
                System.out.println("Client Streams and Sockets set up");
            } else {
                this.UDPSocket.send(new DatagramPacket(buffer, buffer.length, InetAddress.getByName(this.SERVER_IP), Constants.UDP_SERVER_PORT));
                this.TCPSocket = new Socket(this.SERVER_IP, Constants.TCP_SERVER_PORT);
                this.outputStream = new ObjectOutputStream(TCPSocket.getOutputStream());
                this.inputStream = new ObjectInputStream(TCPSocket.getInputStream());
                System.out.println("Client Streams and Sockets set up");
            }
        } catch (IOException e) {
            System.out.println("Could not connect to the Server");
        }

        System.out.println("Haven't got the Game World object yet");

        // Get Shared GameWorld from Server (i.e. to get that initial GameState)

        synchronized (this) {
            gameWorld = (GameWorld) inputStream.readObject();
        }

        System.out.println("Got the GameWorld");

        // Get initial state of GameWorld (i.e. to prevent Null Pointer Exception later when Rendering)
        this.latestState = this.gameWorld.getGameState();
        Background mapBg;
        if (gameWorld.getMapBackground()==null){
            mapBg = Background.GRASS;
        }else{
            mapBg = gameWorld.getMapBackground();
        }
        //System.out.println("Maze:");
        //System.out.println(latestState.getMaze());
        //System.out.println(mapBg);
        this.maze.genMaze(mapBg);
        this.maze.importMaze(latestState.getMaze(),mapBg);

        // New Thread Pool of Client Workers
        gameClientHandler = Executors.newFixedThreadPool(4);
        gameClientHandler.execute(new Thread(new ClientSender(this.outputStream, this.scene, this.latestState)));
        gameClientHandler.execute(new Thread(new ClientReceiver(this.UDPSocket, this.latestState)));
        gameClientHandler.execute(new Thread(new IntegratedSDP(primaryStage, scene, root, maze, tank, allCells, bullets, tanks, serverMapping, colorList, window, this, singlePlayer)));
        gameClientHandler.execute(new Thread(new AudioPlayer(latestState)));


        primaryStage.setScene(this.scene);
        primaryStage.show();
//        root.getChildren().remove(loadingText);


    }

    @Override
    public void stop() throws Exception {
        try {
            outputStream.writeObject("end");
            gameClientHandler.shutdownNow();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
