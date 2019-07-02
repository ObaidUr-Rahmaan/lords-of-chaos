package com.lordsofchaos.gamelogic.transmission;

import java.awt.*;
import java.io.Serializable;

/**
 * Sprite Object
 *
 * @author Max Warren
 */
public class Sprite implements Serializable {

    /**
     * The location the sprite should be drawn at
     */
    public Point location;
    /**
     * The image that should be displayed for the sprite
     */
    public SpriteType image;
    /**
     * The id of the sprite
     */
    public int id;
    /**
     * The rotation of sprite should be drawn with
     */
    public double rotation;
    /**
     * The state of the sprite if they are a player
     */
    public PowerUpState state;
    /**
     * The health of the sprite if it a player
     */
    public int health;
    /**
     * The rotation of the turret if it is a player
     */
    public int turretRot;
    /**
     * The Id of the shooter if it is a bullet
     */
    public int owner;

    /**
     * Generates a generic sprite
     * @param location Where it is
     * @param image How it should be displayed
     * @param id The id of the item
     * @param rotation The rotation it should be drawn at
     */
    public Sprite(Point location, SpriteType image, int id, double rotation) {
        this.location = location;
        this.image = image;
        this.id = id;
        this.rotation = rotation;
        this.state = PowerUpState.normal;
    }

    /**
     * Generates a bullet sprite
     * @param location Where it is
     * @param image How it should be displayed
     * @param id The id of the item
     * @param rotation The rotation it should be drawn at
     * @param owner The id of the shooter
     */
    public Sprite(Point location, SpriteType image, int id, double rotation, int owner) {
        this.location = location;
        this.image = image;
        this.id = id;
        this.rotation = rotation;
        this.state = PowerUpState.normal;
        this.owner = owner;
    }

    /**
     * Generates a player sprite
     * @param location Where it is
     * @param image How it should be displayed
     * @param id The id of the item
     * @param rotation The rotation it should be drawn at
     * @param powerUp the powerup the player has
     * @param health the health of the player
     * @param turretRot the angle of the player's turret
     */
    public Sprite(Point location, SpriteType image, int id, double rotation, PowerUpState powerUp, int health, int turretRot) {
        this.location = location;
        this.image = image;
        this.id = id;
        this.rotation = rotation;
        this.state = powerUp;
        this.health = health;
        this.turretRot = turretRot;
    }
}
