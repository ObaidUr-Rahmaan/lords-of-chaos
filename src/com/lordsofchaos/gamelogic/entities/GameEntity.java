package com.lordsofchaos.gamelogic.entities;

import com.lordsofchaos.gamelogic.transmission.Sprite;

import java.awt.*;
import java.io.Serializable;

/**
 * Standard methods for objects in the game world
 *
 * @author Max Warren
 */
public abstract class GameEntity implements Serializable {


    protected Point location;
    protected Point velocity;
    protected Point size;
    protected int id;
    protected boolean fixedId;


    protected boolean finished;

    /**
     * Creates a basic new entity
     * @param location The location at which it will spawn
     * @param size The size of the entities hitbox
     */
    public GameEntity(Point location, Point size) {
        this.location = location;
        this.size = size;
        this.velocity = new Point(0, 0);
        this.fixedId = false;
    }


    /**
     * Updates data about the internal state of the object
     */
    abstract public void updateObject();

    /**
     * Get a sprite which can display the entity
     * @return A sprite representing the entity
     */
    public abstract Sprite getSprite();


    /**
     * Whether the entity needs to be removed from the gameworld
     * @return if the entity is finished
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Sets whether the entity is finished
     * @param finished
     */
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    /**
     * Get the location of the entity
     * @return The entity's location
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Set the location of the entity
     * @param location The entity's new location
     */
    public void setLocation(Point location) {
        this.location = location;
    }

    /**
     * Get the velocity of the entity
     * @return The entity's velocity
     */
    public Point getVelocity() {
        return velocity;
    }

    /**
     * Set the velocity of the entity
     * @param velocity The entity's new velocity
     */
    public void setVelocity(Point velocity) {
        this.velocity = velocity;
    }

    /**
     * Get the size of the entity
     * @return The entity's size
     */
    public Point getSize() {
        return size;
    }

    /**
     * Get the Id of the entity
     * @return The entity's Id
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Check if the entity has a fixed Id (Id changing on game reset would cause issues)
     * @return If the entity has fixed Id
     */
    public boolean isFixedId() {
        return fixedId;
    }
}