package com.lordsofchaos.ai;

import com.lordsofchaos.gamelogic.GameWorld;
import com.lordsofchaos.gamelogic.entities.GameEntity;
import com.lordsofchaos.gamelogic.entities.gameworlditems.Bullet;
import com.lordsofchaos.gamelogic.entities.gameworlditems.PowerUp;
import com.lordsofchaos.gamelogic.entities.player.HumanPlayer;
import com.lordsofchaos.gamelogic.entities.player.Player;
import com.lordsofchaos.gamelogic.transmission.Sprite;
import com.lordsofchaos.gamelogic.transmission.SpriteType;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * AI player
 *
 * @author Zornitsa Tonozlieva
 */

public class AIPlayer extends Player implements Serializable {

    //milisec between making dеcisions
    private long decisionTime = 2000;
    private long passedTimeInState = 0;
    private AIState aistate;

    private AStar aStar;
    private Queue<Point> currentPath = new LinkedList<Point>();

    private Point enemyLocation;
    private ArrayList<Point> powerUpLocations = new ArrayList<Point>();
    private ArrayList<Point> bulletLocations = new ArrayList<Point>();
    private boolean existsBullet = false;
    private boolean existsPowerUp = false;
    private boolean existsEnemy = false;
    private int poweUpHeuristic;
    private ArrayList<Point> aiBots = new ArrayList<Point>();

    /**
     * Constructor of AIPlayer
     * @param location the initial location
     * @param myWorld the map
     */
    public AIPlayer(Point location, GameWorld myWorld) {
        super(location, myWorld);
        this.poweUpHeuristic = getPoweUpHeuristic();
        aStar = new AStar(myWorld.getMapWalls(), myWorld.getTileHeight(), myWorld.getTileWidth());
        this.looking = new Point(0, 0);
    }

    /**
     * Sets a state for the ai
     * @param state the state for ai
     */
    private void setAiState(AIState state) {
        if (this.aistate != state) {
            this.aistate = state;
            passedTimeInState = 0;
        }
        this.setLooking(this.enemyLocation); //aim
        switch (aistate) {
            case CHASE:
                setCurrentPath(this.enemyLocation);
                return;
            case SHOOT:
                this.fire();
                aistate = AIState.DECISION_MADE;
                return;
            case EVADE:
                Point bullet = closestObject(this.bulletLocations);
                dodgeBullet(bullet);
                return;
            case POWERUP:
                setCurrentPath(closestObject(this.powerUpLocations));
            case GAMEOVER:
                setVelocity(new Point(0,0));
        }
    }

    /**
     * Update method for ai in every frame
     * @param passedTime time passed in every frame
     */
    public void update(long passedTime) {
        //Give an impressive display

        passedTimeInState += passedTime;

        //make decision if time is up
        if (passedTimeInState >= decisionTime) {
            aistate = AIState.DECISION_MADE;
        }
        //done with current path
        else if (currentPath == null || currentPath.isEmpty()) {
            aistate = AIState.DECISION_MADE;
        }

        //make a new decision
        if (aistate == AIState.DECISION_MADE) {
            passedTimeInState = 0;

            //sets locations of enemies, powerUps, bullets
            this.updateCurrentGameState();

            if(!existsEnemy)
                setAiState(AIState.GAMEOVER);
            else if (this.existsBullet && objectIsNearby(this.bulletLocations, 140))
                setAiState(AIState.EVADE);
            else if (existsEnemy && canShootEnemy(this.enemyLocation))
                setAiState(AIState.SHOOT);
            else if (this.existsPowerUp && objectIsNearby(this.powerUpLocations, this.poweUpHeuristic))
                setAiState(AIState.POWERUP);
            else
                setAiState(AIState.CHASE);
        }
        if (this.existsBullet && objectIsNearby(this.bulletLocations, 150))
            setAiState(AIState.EVADE);
        else if (currentPath != null && !currentPath.isEmpty()) {
            Point myLocationTile = new Point(location.x/30, location.y/30);
            if (myLocationTile.x == currentPath.peek().x && myLocationTile.y == currentPath.peek().y) {
                currentPath.poll();
            }
            if(!currentPath.isEmpty())
                makeMove(currentPath.peek(), myLocationTile);
        }
        //this.setVelocity(new Point(0,0));
    }

    /**
     * Next move for the ai
     * @param destination the next position
     * @param myLocationTile the current position
     */
    private void makeMove(Point destination, Point myLocationTile) {
        if (!aiBots.contains(destination)) {
            if (myLocationTile.x == destination.x) {
                if (myLocationTile.y > destination.y) {
                    this.setVelocity(new Point(0, -this.speed));
                }
                if (myLocationTile.y < destination.y) {
                    this.setVelocity(new Point(0, this.speed));
                }
            } else {
                if (myLocationTile.x > destination.x) {
                    this.setVelocity(new Point(-this.speed, 0));
                }
                if (myLocationTile.x < destination.x) {
                    this.setVelocity(new Point(this.speed, 0));
                }
            }
        }

    }

    /**
     * ai trying to avoid a nearby bullet
     * @param bullet location of the bullet
     */
    private void dodgeBullet(Point bullet) {
        Point aiNormalised = new Point(location.x/30, location.y/30);
        Point bulletNormalised = new Point(bullet.x/30, bullet.y/30);

        if (bulletNormalised.x == aiNormalised.x || bulletNormalised.x + 1 == aiNormalised.x
                || bulletNormalised.x - 1 == aiNormalised.x) {
            if (myWorld.isWall(new Point(aiNormalised.y, aiNormalised.x -1)))
                this.setVelocity(new Point(this.speed, 0));
            else
                this.setVelocity(new Point(-this.speed, 0));
        } else if (bulletNormalised.y - 1 == aiNormalised.y || bulletNormalised.y + 1 == aiNormalised.y
                || bulletNormalised.y  == aiNormalised.y) {
            if (myWorld.isWall(new Point(aiNormalised.y -1, aiNormalised.x)))
                this.setVelocity(new Point(0, this.speed));
            else
                this.setVelocity(new Point(0, -this.speed));
        }

        aistate = AIState.DECISION_MADE;
    }

    /**
     * Update the fields with current game state values
     */
    private void updateCurrentGameState() {
        ArrayList<GameEntity> currentItems = this.myWorld.getCurrentItems();
        this.bulletLocations.clear();
        this.aiBots.clear();
        this.powerUpLocations.clear();
        this.existsBullet = false;
        this.existsPowerUp = false;
        this.existsEnemy = false;

        for (GameEntity gameEntity : currentItems) {
            if (gameEntity instanceof Bullet) {
                this.existsBullet = true;
                this.bulletLocations.add(gameEntity.getLocation());
            } else if (gameEntity instanceof PowerUp) {
                this.existsPowerUp = true;
                this.powerUpLocations.add(gameEntity.getLocation());
            } else if (gameEntity instanceof HumanPlayer) {
                this.existsEnemy = true;
                this.enemyLocation = gameEntity.getLocation();
            } else  if (gameEntity instanceof AIPlayer) {
                this.aiBots.add(gameEntity.getLocation());
            }
        }
    }

    private boolean objectIsNearby(ArrayList<Point> objectLocations, double aiRaduis) {
        for (Point objectLocation : objectLocations) {
            if (objectIsNearby(objectLocation, aiRaduis)) return true;
        }
        return false;
    }

    private boolean objectIsNearby(Point objectLocation, double aiRadius) {
        double objectDistance = Math.sqrt(Math.pow(this.location.x - objectLocation.x, 2) +
                Math.pow(this.location.y - objectLocation.y, 2));
        return objectDistance < aiRadius;
    }

    /**
     * Gets a random range in which is acceptable to go for a power up
     */
    private int getPoweUpHeuristic() {
        ArrayList<Integer> randomOptions = new ArrayList<Integer>();
        randomOptions.add(800);
        randomOptions.add(300);
        randomOptions.add(100);
        Random rand = new Random();
        return randomOptions.get(rand.nextInt(randomOptions.size()));
    }

    private Point closestObject(ArrayList<Point> objectLocations) {
        double closestObjectDist = Double.MAX_VALUE;
        Point closestObject = new Point(0,0);
        for (Point objectLocation : objectLocations) {
            double objectDistance = Math.sqrt(Math.pow(this.location.x - objectLocation.x, 2) +
                    Math.pow(this.location.y - objectLocation.y, 2));
            if (objectDistance < closestObjectDist) {
                closestObjectDist = objectDistance;
                closestObject = objectLocation;
            }
        }
        return closestObject;
    }

    /**
     * Checks if it is worth shooting at a human player
     * @param enemyLocation the location of the human player
     */
    private boolean canShootEnemy(Point enemyLocation) {
        if (!objectIsNearby(enemyLocation,250)) return false;
        Point aiNormalised = new Point(location.x/30, location.y/30);
        Point enemyNormalised = new Point(enemyLocation.x/30, enemyLocation.y/30);

        if (enemyNormalised.x == aiNormalised.x) {
            int yMin = Math.min(aiNormalised.y, enemyNormalised.y);
            int yMax = Math.max(aiNormalised.y, enemyNormalised.y);
            for (int y = yMin + 1; y < yMax; y++){
                int x = enemyNormalised.x;
                if(myWorld.isWall(new Point(y, x))) return false;
            }
        } else {
            int m = (aiNormalised.y - enemyNormalised.y) / (aiNormalised.x - enemyNormalised.x);
            int b = enemyNormalised.y - m * enemyNormalised.x;
            int xMin = Math.min(aiNormalised.x, enemyNormalised.x);
            int xMax = Math.max(aiNormalised.x, enemyNormalised.x);

            for (int x = xMin + 1; x < xMax; x++){
                int y = m * x + b;
                if(myWorld.isWall(new Point(y, x))) return false;
            }
        }
        return true;
    }

    /**
     * Calls a method in A* to construct an optimal path to goal
     * @param goalNode the goal location
     */
    private void setCurrentPath(Point goalNode) {
        ArrayList<Point>path = aStar.findRoute(goalNode, this.location);
        this.currentPath.clear();
        currentPath.addAll(path);
        currentPath.poll();
    }

    @Override
    public void updateObject() {
        super.updateObject();
        this.speed = 3;
        //This method call checks which direction
        this.currentFacing();
        update(50);
    }

    @Override
    public Sprite getSprite() {
        return new Sprite(this.location, SpriteType.PLAYER_BASIC, this.id, this.directionFromSprite(), this.state, this.health, this.getTurretBearing());
    }

    //This is the code to reimplement the old facing
    private double directionFromSprite(){
        switch (facing){
            case PLAYER_UP: return 0;
            case PLAYER_DOWN: return 180;
            case PLAYER_LEFT: return 270;
            case PLAYER_RIGHT: return 90;

        }
        return 0;
    }

    private void currentFacing(){
        if(Math.abs(this.velocity.x) > 0){
            if(this.velocity.x > 0){
                this.facing = SpriteType.PLAYER_RIGHT;
            }
            else {
                this.facing = SpriteType.PLAYER_LEFT;

            }
        }
        if(Math.abs(this.velocity.y) > Math.abs(this.velocity.x)){
            if(this.velocity.y > 0){
                this.facing = SpriteType.PLAYER_DOWN;
            }
            else {
                this.facing = SpriteType.PLAYER_UP;

            }
        }
    }

}