package com.lordsofchaos.gamelogic.transmission;

import com.lordsofchaos.gamelogic.entities.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A simplified class describing the game world that can be passed over the network
 *
 * @author Max Warren
 */
public class GameState implements Serializable {


    public boolean[][] maze;
    public boolean gameOver = false;
    public boolean sandMap;

    public CopyOnWriteArrayList<Sprite> objects;
    //Whichever sounds need to be passed to the Audio manager
    public CopyOnWriteArrayList<Sounds> soundsToStart;


    //Player unique data
    private Integer health;
    private Integer playersId;
    private boolean dead;


    //Info to be passed directly to the UI I.E. current player health should be here too. I'm not sure what would be the best format for that though.

    /**
     * Generates a generic shared version of the map
     * @param map Free/blocked tiles in the
     * @param objects A list of objects in the world that need to be displayed
     * @param sounds Any sounds which need to be started currently
     */
    public GameState(boolean[][] map, CopyOnWriteArrayList<Sprite> objects, CopyOnWriteArrayList<Sounds> sounds, boolean sandMap){
        this.maze = map;
        this.objects =  objects;
        this.soundsToStart = sounds;
        this.sandMap = sandMap;
    }

    /**
     * Generate the data to be shown to a particular player in the gameworld
     * @param sharedState The shared data for all players
     * @param player The player who's personalized information needs to be displayed
     */
    public GameState(GameState sharedState, Player player){
        this.maze= sharedState.getMaze();
        this.objects = sharedState.getObjects();
        this.soundsToStart = sharedState.getSoundsToStart();
        this.playersId = -4;
        this.gameOver = sharedState.gameOver;
        if(player != null){
            this.health = player.getHealth();
            this.playersId = player.getId();
            this.dead = player.isFinished();
        }
    }

    /**
     * Get the id of the controlled player
     * @return The controlled player's ID
     */
    public Integer getPlayersId() {
        return playersId;
    }

    /**
     * Get the maze
     * @return the maze
     */
    public boolean[][] getMaze() {
        return maze;
    }

    /**
     * Set the maze
     * @param maze The new maze
     */
    public void setMaze(boolean[][] maze) {
        this.maze = maze;
    }

    /**
     * Get all of the objects to display
     * @return objects to display
     */
    public CopyOnWriteArrayList<Sprite> getObjects() {
        return objects;
    }

    /**
     * Set objects to display
     * @param objects New objects to display
     */
    public void setObjects(CopyOnWriteArrayList<Sprite> objects) {
        this.objects = objects;
    }

    /**
     * Get any sounds which need to be player
     * @return any sounds which need to be player
     */
    public CopyOnWriteArrayList<Sounds> getSoundsToStart() {
        return soundsToStart;
    }

    /**
     * Clear any queued up sounds
     */
    public void soundsSent(){
        this.soundsToStart.clear();
    }

    /**
     * Get the health of the controlled player
     * @return The player's health
     */
    public Integer getHealth() {
        return health;
    }

    /**
     * Check if the controlled player is dead
     * @return Is the controlled player dead
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * Check if the game is finished
     * @return Is the game finished
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Set if the game is over
     * @param gameOver If the game is over or not now
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
