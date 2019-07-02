package com.lordsofchaos.gamelogic.entities.gameworlditems;

import com.lordsofchaos.gamelogic.entities.GameEntity;
import com.lordsofchaos.gamelogic.transmission.Sprite;
import com.lordsofchaos.gamelogic.transmission.SpriteType;

import java.awt.*;
import java.io.Serializable;

/**
 * Projectiles fired by the player
 * These are objects representing the bullets fired by the players in the game world
 * @author Max Warren
 */
public class Bullet extends GameEntity implements Serializable {

    /**
     *The number of frames that the bullet will remain in the gameworld for
     */
    private final int LIFETIME = 600;


    /**
     * How many frames the bullet has left to remain in the gameworld
     */
    private int timeLeft;

    /**
     * The amount of health a bullet will remove from a player that it hits
     */
    private int damage;
    protected int owner;

    /**
     *
     * @param owner The Id of the player that fired the bullet
     * @param damage The damage the bullet will do to a player that it hits
     * @param position The initial location of the bullet. Stores the location of the bullets top left corner
     * @param velocity The velocity the bullet is moving with
     * @param bulletSize The size of the new bullet
     */
    public Bullet(int owner, int damage, Point position, Point velocity, int bulletSize) {
        super(position, new Point(bulletSize, bulletSize));
        this.owner = owner;
        this.damage = damage;
        this.velocity = velocity;
        this.timeLeft = LIFETIME;
    }

    /**
     * Processes internal changes to the bullet over the time of one frame.
     * Reduces the time remaining on the bullet
     */
    public void updateObject() {
        timeLeft -= 1;
        if (--timeLeft < 1) {
            this.finished = true;
        }
    }

    public int getBearing(){
        //Returns a bearing showing the direction the bullet is heading
        double bearing = Math.atan2((double)velocity.x, (double)velocity.y);
        int intBearing = -(int)(bearing*180/Math.PI) + 180;
        return intBearing;
    }

    /**
     * Get the damage of the bullet
     * @return The damage of the bullet
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Get a representation of the bullet
     * @return The sprite that can display the bullet
     */
    public Sprite getSprite() {
        return new Sprite(this.location, SpriteType.BULLET, this.id, getBearing(), this.owner);
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

}
