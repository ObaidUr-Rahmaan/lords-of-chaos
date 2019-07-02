package com.lordsofchaos.gamelogic.entities.player;

import com.lordsofchaos.gamelogic.GameWorld;
import com.lordsofchaos.gamelogic.entities.GameEntity;
import com.lordsofchaos.gamelogic.entities.gameworlditems.Bullet;
import com.lordsofchaos.gamelogic.entities.gameworlditems.Rocket;
import com.lordsofchaos.gamelogic.transmission.PowerUpState;
import com.lordsofchaos.gamelogic.transmission.Sounds;
import com.lordsofchaos.gamelogic.transmission.Sprite;
import com.lordsofchaos.gamelogic.transmission.SpriteType;

import java.awt.*;
import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * General class for all players in the game
 * contains functionality common among both human and AI players
 *
 * @author Max Warren
 */
public abstract class Player extends GameEntity implements Serializable {

    public final static int PLAYER_HEIGHT = 27;
    public final static int PLAYER_WIDTH = 28;

    private static final int INVINCIBLE_TIME = 300;
    private static final int MULTISHOT_TIME = 420;
    private static final int SLOW_TIME = 480;
    private static final int ROCKET_TIME = 300;

    private static final int MOVE_SPEED = 6;
    private static final int SLOWED_SPEED = 4;

    private static final int DEFAULT_SHOT_DAMAGE = 10;
    private static final int ROCKET_SHOT_DAMAGE = 40;
    private static final int MULTISHOT_SHOT_DAMAGE = 30;

    private static final int DEFAULT_SHOT_COOLDOWM = 30;
    private static final int ROCKET_SHOT_COOLDOWN = 90;
    private static final int MULTISHOT_SHOT_COOLDOWN = 60;

    public static int invincibleTime = SLOW_TIME/40;
    public static int multishotTime = MULTISHOT_TIME/60;
    public static int slowTime = SLOW_TIME/60;
    public static int rocketTime = ROCKET_TIME/60;

    private static final int STARTING_HEALTH = 100;

    protected int health;
    protected int fireCooldown;
    protected int direction;
    protected PowerUpState state;
    protected int powerUpTime;
    protected int speed;

    protected Point looking;
    protected GameWorld myWorld;

    protected SpriteType facing;

    /**
     * Generates a new player
     * @param location The players location
     * @param myWorld A reference to the players world
     */
    public Player(Point location, GameWorld myWorld) {
        super(location, new Point(PLAYER_HEIGHT, PLAYER_WIDTH));
        this.myWorld = myWorld;
        this.setUp();
    }

    /**
     * Resets the players internal state to a start of game situation
     */
    public void setUp(){
        this.speed = MOVE_SPEED;
        this.fireCooldown = 0;
        this.health = STARTING_HEALTH;
        this.looking = new Point(0, 0);
        this.facing= SpriteType.PLAYER_DOWN;
        this.finished = false;
    }

    /**
     * Processes internal changes to the bullet over the time of one frame.
     * Will mark the player for death if they are out of health
     */
    public void updateObject(){
        fireCooldown--;
        if(powerUpTime > 0){
            //System.out.println(this.state);
            powerUpTime--;
        }else {
            this.state = PowerUpState.normal;
            this.speed = MOVE_SPEED;
        }
        if(health <= 0){
            this.finished = true;
        }
    }

    /**
     * Fires a bullet from the player and try to add it to the gameworld
     */
    public void fire() {
        if (this.fireCooldown > 0) {
            return;
        }
        switch (this.state) {
            //Fire 3 bullets at slightly different directions
            case multishot:
                Point lookingSwap = new Point(-((looking.y-location.y)/2), (looking.x-location.x)/2);
                Point bulletStartPoint = this.getFacingEdge(new Point(5, 5));
                Bullet bullet = new Bullet(this.id, MULTISHOT_SHOT_DAMAGE, bulletStartPoint, this.bulletCompMaths(10), 5);
                if(myWorld.wallCheck(bulletStartPoint, bullet)){
                    myWorld.addItem(bullet);
                    myWorld.queueSound(Sounds.bulletFire);
                }
                looking.x += lookingSwap.x;
                looking.y += lookingSwap.y;
                bulletStartPoint = this.getFacingEdge(new Point(5, 5));
                bullet = new Bullet(this.id, MULTISHOT_SHOT_DAMAGE, bulletStartPoint, this.bulletCompMaths(10), 5);
                if(myWorld.wallCheck(bulletStartPoint, bullet)){
                    myWorld.addItem(bullet);
                    myWorld.queueSound(Sounds.bulletFire);
                }
                looking.x -= 2*lookingSwap.x;
                looking.y -= 2*lookingSwap.y;
                bulletStartPoint = this.getFacingEdge(new Point(5, 5));
                bullet = new Bullet(this.id, MULTISHOT_SHOT_DAMAGE, bulletStartPoint, this.bulletCompMaths(10), 5);
                if(myWorld.wallCheck(bulletStartPoint, bullet)){
                    myWorld.addItem(bullet);
                    myWorld.queueSound(Sounds.bulletFire);
                }
                looking.x += lookingSwap.x;
                looking.y += lookingSwap.y;
                fireCooldown = MULTISHOT_SHOT_COOLDOWN;
                break;
                //Fire a move powerful bullet
                case rocket:
                bulletStartPoint = this.getFacingEdge(new Point(5, 5));
                bullet = new Rocket(this.id, ROCKET_SHOT_DAMAGE, bulletStartPoint, this.bulletCompMaths(20), 5, this.myWorld.getCurrentItems(), 20);
                if(myWorld.wallCheck(bulletStartPoint, bullet)){
                    myWorld.addItem(bullet);
                    myWorld.queueSound(Sounds.bulletFire);
                }
                fireCooldown = ROCKET_SHOT_COOLDOWN;
                break;
                //Fire a normal shot
            default:
                bulletStartPoint = this.getFacingEdge(new Point(5, 5));
                bullet = new Bullet(this.id, DEFAULT_SHOT_DAMAGE, bulletStartPoint, this.bulletCompMaths(12), 5);
                if(myWorld.wallCheck(bulletStartPoint, bullet)){
                    myWorld.addItem(bullet);
                    myWorld.queueSound(Sounds.bulletFire);
                }
                fireCooldown = DEFAULT_SHOT_COOLDOWM;
                break;
        }
    }

    /**
     * Applies a random powerup to the player
     */
    public void powerUp(){
        this.speed = MOVE_SPEED;
        //ThreadLocalRandom.current().nextInt(4)
        switch (ThreadLocalRandom.current().nextInt(4)){
            case 0:
                this.state = PowerUpState.invincible;
                this.powerUpTime = INVINCIBLE_TIME;
                break;
            case 1:
                this.state = PowerUpState.multishot;
                this.powerUpTime = MULTISHOT_TIME;
                break;
            case 2:
                this.state = PowerUpState.slow;
                this.powerUpTime = SLOW_TIME;
                this.speed = SLOWED_SPEED;
                break;
            case 3:
                this.state = PowerUpState.rocket;
                this.powerUpTime = ROCKET_TIME;
                break;
        }
    }

    /**
     * Find the direction the tanks turret is facing
     * @return the tanks turret facing
     */
    protected int getTurretBearing(){
        //Returns a bearing showing the direction the turret is facing
        int xComponent =  looking.x - (location.x + (PLAYER_WIDTH / 2));
        int yComponent =  looking.y - (location.y + (PLAYER_HEIGHT / 2));
        //Find the bearing (in radians) of the players crosshair
        double bearing = Math.atan2((double)xComponent, (double)yComponent);
        int intBearing = -(int)(bearing*180/Math.PI) + 180;
        return intBearing;
    }


    private Point getFacingEdge(Point bulletSize) {
        //Calculate the vector between the center of the player sprite and their crosshair.
        int xComponent =  looking.x - (location.x + (PLAYER_WIDTH / 2));
        int yComponent =  looking.y - (location.y + (PLAYER_HEIGHT / 2));
        //Find the bearing (in radians) of the players crosshair
        double bearing = Math.atan2((double)xComponent, (double)yComponent);
        //Find the vertical difference from the midline
        double exitHorizontal = Math.tan(bearing) * PLAYER_HEIGHT/2;
        //As location refers to the top left of the tank various offsets need to be used depending on which side it leaves from.
        //Catching exits from the top or bottom
        if(Math.abs(exitHorizontal) < PLAYER_WIDTH/2){
            //Bullet position below the tank
            if(yComponent >= 0){
                return new Point(this.location.x+PLAYER_WIDTH/2 + (int)exitHorizontal - (bulletSize.y / 2) , this.location.y+PLAYER_HEIGHT + bulletSize.y*2);
            }
            //Bullet position above the tank
            else {
                return new Point(this.location.x+PLAYER_WIDTH/2 - (int)exitHorizontal - (bulletSize.y / 2) , this.location.y- bulletSize.y*2);

            }
        }
        //Catching exits from either side
        else {
            double exitVertical = (PLAYER_WIDTH/2) / Math.tan(bearing);
            //Bullet position to the left of the tank
            if(xComponent>=0){
                return new Point( this.location.x+PLAYER_WIDTH + bulletSize.x*2, this.location.y+(PLAYER_HEIGHT/2) + (int)exitVertical - (bulletSize.y/2));
            }
            //Bullet positions to the right of the tank
            else {
                return new Point(this.location.x-bulletSize.x - bulletSize.x*2, this.location.y+(PLAYER_HEIGHT/2) - (int)exitVertical - (bulletSize.y/2));

            }
        }
    }

    private Point bulletCompMaths(int totalSpeed) {
        //Calculate the vector the bullet wants to travel along and use this to find the x and y components of the bullets movement
        Point travelVector = new Point((looking.x - (location.x + (PLAYER_WIDTH / 2))), (looking.y - (location.y + (PLAYER_HEIGHT / 2))));
        //Find the bearing of the bullets path
        double travelHyp = Math.sqrt((travelVector.x * travelVector.x) + (travelVector.y * travelVector.y));
        //Use the bearing to find the x and y components of the bullets motion.
        return new Point((int)(totalSpeed * travelVector.x / travelHyp), (int)(totalSpeed * travelVector.y / travelHyp));
    }

    /**
     * Get a representation of the player
     * @return The sprite that can display the player
     */
    public Sprite getSprite() {
        return new Sprite(this.location, SpriteType.PLAYER_BASIC, this.id, this.directionFromSprite(), this.state, this.getHealth(), this.getTurretBearing());
    }

    /**
     * Get the players current direction
     * @return The players direction
     */
    private double directionFromSprite(){
        return this.direction;
    }

    /**
     * Set the direction the tank is facing
     * @param direction The tanks new direction
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * See where the player is looking
     * @return where the player is looking
     */
    public Point getLooking() {
        return looking;
    }

    /**
     * Sets where the player is currently looking
     * @param looking The point the player is looking at
     */
    public void setLooking(Point looking) {
        this.looking = looking;
    }

    /**
     * Get the health of the player
     * @return Health of the player
     */
    public int getHealth() {
        return health;
    }


    /**
     * Set the health of the player
     * Living invincible players cannot have their health affected
     * @param health The new health of the player
     */
    public void setHealth(int health) {
        if(this.state == PowerUpState.invincible && !this.isFinished()){
            return;
        }
        this.health = health;
    }
}
