package com.lordsofchaos.gamelogic;

import com.lordsofchaos.ai.AIPlayer;
import com.lordsofchaos.gamelogic.entities.*;
import com.lordsofchaos.gamelogic.entities.gameworlditems.Bullet;
import com.lordsofchaos.gamelogic.entities.gameworlditems.Explosion;
import com.lordsofchaos.gamelogic.entities.gameworlditems.PowerUp;
import com.lordsofchaos.gamelogic.entities.gameworlditems.Rocket;
import com.lordsofchaos.gamelogic.entities.player.*;
import com.lordsofchaos.gamelogic.transmission.GameState;
import com.lordsofchaos.gamelogic.transmission.Sounds;
import com.lordsofchaos.gamelogic.transmission.Sprite;
import com.lordsofchaos.graphics.Background;
import com.lordsofchaos.networking.game.client.NetworkedMouseEvent;
import javafx.scene.input.InputEvent;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;


/**The main class for the entire game world
 * 
 * @author Max Warren
 */
public class GameWorld extends Thread implements Serializable {

    private static final int TILE_WIDTH = 30;
    private static final int TILE_HEIGHT = 30;
    private static final int MAP_WIDTH_TILE = 60;
    private static final int MAP_HEIGHT_TILE = 30;
    private static final int MAX_PLAYERS = 4;
    private final int POWER_UP_START = 600;
    private final int POWER_UP_TIME = 300;


    private int powerUpCountDown;
    private boolean[][] mapWalls;

    private ArrayList<GameEntity> currentItems;
    private ArrayList<Player> players;
    private ArrayList<HumanPlayer> humanPlayers;
    private HashMap<UUID, HumanPlayer> whosCommands;
    private ArrayBlockingQueue<NamedInputs> queuedInput;


    private GameState simpleState;

    private boolean dead;
    private int nextId;
    private int numberOfHumanPlayers;
    private int numberOfAIBots;
    private int numberOfPlayers;

    private Background mapBackground;

    /**
     * Generates a new gameworld
     * @param numberOfHumanPlayers The maximum number of humans players that can connect
     * @param numberOfAIPlayers The addidtional number of ai players that should be added
     */
    public GameWorld(int numberOfHumanPlayers, int numberOfAIPlayers, boolean sandMap){
        if(sandMap){
            setMapBackground(Background.GRASSNOT);
        }
        else {
            setMapBackground(Background.GRASS);
        }
        this.nextId = 0;
        dead = false;
        whosCommands = new HashMap<UUID, HumanPlayer>();
        if(numberOfHumanPlayers > MAX_PLAYERS){
            System.out.println("invalid number of humans");
            numberOfHumanPlayers = MAX_PLAYERS;
        }
        if (numberOfHumanPlayers + numberOfAIPlayers > MAX_PLAYERS) {
            numberOfAIPlayers = MAX_PLAYERS - numberOfHumanPlayers;
            System.out.println("invalid player count sum");
        }
        this.numberOfHumanPlayers = numberOfHumanPlayers;
        this.numberOfAIBots = numberOfAIPlayers;
        this.numberOfPlayers = numberOfHumanPlayers + numberOfAIPlayers;

        this.queuedInput = new ArrayBlockingQueue<NamedInputs>(3000);
        MapGenerator mapGenerator = new MapGenerator(MAP_WIDTH_TILE, MAP_HEIGHT_TILE);
        this.mapWalls = mapGenerator.makeMaze();
        this.simpleState = new GameState(mapWalls, new CopyOnWriteArrayList<Sprite>(), new CopyOnWriteArrayList<Sounds>(), sandMap);
        currentItems = new ArrayList<GameEntity>();
        players = new ArrayList<Player>();
        humanPlayers = new ArrayList<HumanPlayer>();
        //System.out.println("created");

    }

    public Background getMapBackground() {
        return mapBackground;
    }

    public void setMapBackground(Background mapBackground) {
        this.mapBackground = mapBackground;
    }

    /**
     * Starts the GameWorld updating and processing its data in real time.
     * It should tick 60 times each second whilst running
     */
    @Override
    public void run(){
        while(players.size()< numberOfPlayers){
            AIPlayer temp = new AIPlayer(this.playerPlaceLocation(players.size()), this);
            temp.setDirection(this.playerFacing(players.size()));
            players.add(temp);
        }
        this.init();
        while(!dead){
            long startTime = System.nanoTime();
            this.updateTick();
            long endTime = System.nanoTime();
            long waitTime = (1000000000/60 - (endTime - startTime)) - 250000;
            try {
                waitTime = Math.max(waitTime, 0);
                Thread.sleep(waitTime/1000000, (int)waitTime% 1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println("Tick time: " + (System.nanoTime() - startTime) + "  Think time: " + (endTime -startTime));
        }
    }

    /**
     * Performs all the updating for a single processing tick (1/60th of a second)
     */
    public void updateTick(){
        //System.out.println("Per tick");
        //System.out.println("tick");
        try{
            while(queuedInput.size() > 0){
                NamedInputs processesInput = queuedInput.take();
                whosCommands.get(processesInput.getAddress()).processEvent(processesInput);
            }
        }
        catch(InterruptedException e){
            System.out.println("Interrupted");
        }
        powerUpCountDown -= 1;
        if(powerUpCountDown < 1){
            this.spawnPowerUp();
            powerUpCountDown = POWER_UP_TIME;
        }
        this.endCheck();
        //System.out.println("updating" + currentItems.size());
        for (int i = 0; i < currentItems.size(); i++){
            currentItems.get(i).updateObject();
        }
        ArrayList<GameEntity> objectsToRemove = new ArrayList<GameEntity>();
        //Move every item and collision check
        for (GameEntity e: currentItems){
            if (e.isFinished()){
                objectsToRemove.add(e);
            }
        }
        currentItems.removeAll(objectsToRemove);
        for (int e = 0; e < currentItems.size(); e++) {
            moveObject(currentItems.get(e));
        }
        CopyOnWriteArrayList<Sprite> currentSendItems = new CopyOnWriteArrayList<>();
        for(GameEntity entity: currentItems){
            currentSendItems.add(entity.getSprite());
        }
        simpleState.getObjects().clear();
        simpleState.getObjects().addAll(currentSendItems);
    }


    /**
     * Pass a command to one of the human players
     * @param event The Event
     * @param playerAddress The UUID of the player who is being commanded
     */
    public void queueAction(InputEvent event, UUID playerAddress){
        queuedInput.add(new NamedInputs(event, playerAddress));
    }

    /**
     * Pass a mouse event command to one of the human players
     * @param mouseEvent The Event
     * @param playerAddress The UUID of the player who is being commanded
     */
    public void queueAction(NetworkedMouseEvent mouseEvent, UUID playerAddress){
        //System.out.println("Mouse at:   " + mouseLocation);
        queuedInput.add(new NamedInputs(mouseEvent, playerAddress));
    }


    //Reset and regenerate the map
    private void init(){
        //Reset thing which need to change to restart the game
        simpleState.soundsSent();
        simpleState.getObjects().clear();
        simpleState.setMaze(mapWalls);

        for(int i = 0; i < players.size(); i++){
            players.get(i).setUp();
            players.get(i).setLocation(this.playerPlaceLocation(i));
            players.get(i).setDirection(this.playerFacing(i));
            this.addItem(players.get(i));
        }
        powerUpCountDown = POWER_UP_START;
    }

    private boolean endCheck(){
        //Check to see if the game is over and start informing clients
        int playerAlive = 0;
        for(Player p: players){
            if(!p.isFinished()){
                playerAlive++;
            }
        }
        if(playerAlive <= 1){
            simpleState.setGameOver(true);
            //this.dead = true;
            //System.out.println("over");
            return false;
        }
        else {
            playerAlive = 0;
            for(Player p: humanPlayers){
                if(!p.isFinished()){
                    playerAlive++;
                }
            }
            if(playerAlive == 0){
                simpleState.setGameOver(true);
                //this.dead = true;
                //System.out.println("over");
                return false;
            }
        }
        return true;
    }

    //This method should be called each time a new player is added to the game
    public boolean addPlayer(UUID playersUUID) {
        //System.out.println("Player address" +playerAddress);
        //System.out.println("Player address title" +playerAddress.toString());
        if (players.size() >= MAX_PLAYERS) {
            return false;
        }
        HumanPlayer addedPlayer = new HumanPlayer(this.playerPlaceLocation(players.size()), this );
        addedPlayer.setDirection(this.playerFacing(players.size()));
        players.add(addedPlayer);
        humanPlayers.add(addedPlayer);
        whosCommands.put(playersUUID, addedPlayer);
        return true;
    }


    private Point playerPlaceLocation(int numberPlaced){
        switch(numberPlaced){
            case 0: return new Point(TILE_WIDTH, TILE_HEIGHT);
            case 1: return new Point(TILE_WIDTH, (MAP_HEIGHT_TILE*TILE_HEIGHT-Player.PLAYER_HEIGHT)-TILE_HEIGHT-10);
            case 2: return new Point((MAP_WIDTH_TILE*TILE_WIDTH-Player.PLAYER_WIDTH)-TILE_WIDTH, TILE_HEIGHT);
            case 3: return new Point((MAP_WIDTH_TILE*TILE_WIDTH-Player.PLAYER_WIDTH)-TILE_WIDTH-30, (MAP_HEIGHT_TILE*TILE_HEIGHT-Player.PLAYER_HEIGHT)-TILE_HEIGHT-30);
        }
        return new Point(0,0);
    }

    private int playerFacing(int numberPlaced){
        switch(numberPlaced){
            case 0: return 135;
            case 1: return 45;
            case 2: return 225;
            case 3: return 315;
        }
        return 0;
    }

    private void spawnPowerUp() {
        Random locationGen = new Random();
        int randomX = locationGen.nextInt(MAP_WIDTH_TILE*TILE_WIDTH - PowerUp.POWERUP_SIZE);
        int randomY = locationGen.nextInt(MAP_HEIGHT_TILE*TILE_HEIGHT - PowerUp.POWERUP_SIZE);
        PowerUp potentialNewPowerUp = new PowerUp(new Point(randomX, randomY));
        if(this.posCheck(potentialNewPowerUp.getLocation(),potentialNewPowerUp , false)){
            this.addItem(potentialNewPowerUp);
        }
    }

    public int getNextId() {
        return nextId++;
    }

    private boolean moveObject(GameEntity e) {
        Point vel = e.getVelocity();
        if (vel.x == 0 && vel.y == 0) {
            return true;
        }
        Point startingLoc = e.getLocation();
        Point finalLoc = new Point(startingLoc.x + +vel.x, startingLoc.y + vel.y);
        int checking = currentItems.indexOf(e);
        if (posCheck(finalLoc, e,  true)) {
            e.setLocation(finalLoc);
            return true;
        }else if(e instanceof Player){
            Point xMove = new Point(finalLoc.x, e.getLocation().y);
            if(posCheck(xMove, e,  false)){
                e.setLocation(xMove);
                return true;
            }
            Point yMove = new Point(e.getLocation().x, finalLoc.y);
            if(posCheck(yMove, e,  false)){
                e.setLocation(yMove);
                return true;
            }
        }
        return false;
    }

    private boolean posCheck(Point finalLoc, GameEntity entity, boolean collide) {
        Point size = entity.getSize();
        if(!wallCheck(finalLoc, entity)){
            return false;
        }
        boolean stopped = false;
        int entityBottom = finalLoc.y + size.y;
        int entityRight = finalLoc.x + size.x;
        //Check for collisions with all other items in the world.
        for(int currentEntityNumber = 0; currentEntityNumber<=currentItems.size()-1; currentEntityNumber++){
            GameEntity checkingEntity = currentItems.get(currentEntityNumber);
            if(entity == checkingEntity){
                continue;
            }
            Point checkLock = checkingEntity.getLocation();
            Point checkSize = checkingEntity.getSize();
            if(!(checkLock.x > entityRight || checkLock.x+checkSize.x < finalLoc.x|| checkLock.y > entityBottom || checkLock.y+checkSize.y < finalLoc.y)){
                    //System.out.println("entity hit");
                    if(collide){
                        stopped = collide(entity, checkingEntity) || stopped;

                    }
                    else {
                        stopped = true;
                    }
            }
        }
        return !stopped;
    }

    public boolean wallCheck(Point location, GameEntity entity){
        Point size = entity.getSize();
        if(location.x < 0 || location.x > (TILE_WIDTH*MAP_WIDTH_TILE) - entity.getSize().x ||location.y < 0 || location.y > (TILE_HEIGHT*MAP_HEIGHT_TILE) - entity.getSize().y ){
            return false;
        }
        int tileRight = (location.x + size.x) / TILE_WIDTH;
        int tileUp = (location.y) / TILE_HEIGHT;
        int tileDown = (location.y + size.y) / TILE_HEIGHT;
        for (int xTile = location.x / TILE_WIDTH; xTile <= tileRight && xTile < MAP_WIDTH_TILE; xTile++) {
            for (int yTile = tileUp; yTile <= tileDown && yTile < MAP_HEIGHT_TILE; yTile++) {
                if (mapWalls[yTile][xTile]) {
                    if(entity instanceof Bullet){
                        //Special code for bullet/wall collisions
                        if(mapWalls[yTile][entity.getLocation().x/TILE_WIDTH] ) {
                            entity.setVelocity(new Point(entity.getVelocity().x, -entity.getVelocity().y));
                        }
                        else if(mapWalls[entity.getLocation().y/TILE_HEIGHT][xTile] ){
                            entity.setVelocity(new Point(-entity.getVelocity().x, entity.getVelocity().y));
                        }
                        else {
                            entity.setVelocity(new Point(-entity.getVelocity().x, -entity.getVelocity().y));
                        }
                        if(entity instanceof Rocket){
                            entity.setFinished(true);
                        }

                    } else if(entity instanceof Player){
                        if(mapWalls[yTile][entity.getLocation().x/TILE_WIDTH] ) {
                            entity.setVelocity(new Point(entity.getVelocity().x, -entity.getVelocity().y));

                        }
                        else if(mapWalls[entity.getLocation().y/TILE_HEIGHT][xTile] ){
                            entity.setVelocity(new Point(-entity.getVelocity().x, entity.getVelocity().y));
                        }
                        else {
                            entity.setVelocity(new Point(-entity.getVelocity().x, -entity.getVelocity().y));
                        }

                    }

                    return false;
                }
            }
        }
        return true;
    }

    private boolean collide(GameEntity moving, GameEntity still){
        if(moving instanceof Player){
            if(still instanceof Player){
                return true;
            } else if (still instanceof Bullet) {
                if (!still.isFinished() && !moving.isFinished()) {
                    //System.out.println("hit player " + moving.getId());
                    Player player = (Player) moving;
                    player.setHealth(player.getHealth() - ((Bullet) still).getDamage());
                    still.setFinished(true);
                    this.addItem(new Explosion(new Point(still.getLocation().x-16, still.getLocation().y-16)));
                }
                return false;
            } else if(still instanceof PowerUp){
                still.setFinished(true);
                ((Player) moving).powerUp();
                return false;
            }

        } else if (moving instanceof Bullet) {
            if (still instanceof Player) {
                if (!still.isFinished() && !moving.isFinished()) {
                    //System.out.println("hit player " + still.getId());
                    Player player = (Player) still;
                    //System.out.println(player.getHealth());
                    player.setHealth(player.getHealth() - ((Bullet) moving).getDamage());
                    //System.out.println("Player hit   id:" +player.getId() + "health: " + player.getHealth());
                    moving.setFinished(true);
                    this.addItem(new Explosion(new Point(moving.getLocation().x-16, moving.getLocation().y-16)));

                }
                return false;

            } else if (still instanceof Bullet || still instanceof PowerUp) {
                still.setFinished(true);
                moving.setFinished(true);
                this.addItem(new Explosion(new Point(still.getLocation().x-16, still.getLocation().y-16)));
                return false;
            }
        }
        return false;
    }

    public void addItem(GameEntity itemToAdd) {
        if(!itemToAdd.isFixedId()){
            itemToAdd.setId(this.getNextId());
        }
        this.currentItems.add(itemToAdd);
    }

    public boolean[][] getMapWalls() {return this.mapWalls;}

    public boolean isWall(Point point) {
        return this.mapWalls[point.x][point.y];
    }

    public void queueSound(Sounds e){
        simpleState.getSoundsToStart().add(e);
    }

    public ArrayList<GameEntity> getCurrentItems() {
        return currentItems;
    }

    public GameState getGameState() {
        return simpleState;
    }

    public GameState getUniqueGameState(UUID identifier){
        try{
            return new GameState(this.simpleState, this.whosCommands.get(identifier));
        }catch (NullPointerException e){
            return new GameState(this.simpleState, null);
        }
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public int getNumberOfHumanPlayers() {
        return numberOfHumanPlayers;
    }

    public int getNumberOfAIBots() {
        return numberOfAIBots;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public int getTileWidth() {return this.MAP_WIDTH_TILE;}

    public int getTileHeight() {return this.MAP_HEIGHT_TILE;}
}