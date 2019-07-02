package com.lordsofchaos.gamelogic.entities.gameworlditems;

import com.lordsofchaos.gamelogic.entities.GameEntity;
import com.lordsofchaos.gamelogic.transmission.Sprite;
import com.lordsofchaos.gamelogic.transmission.SpriteType;

import java.awt.*;
import java.io.Serializable;

/**
 * The power up boxes found on the map.
 * These don't do a whole lot of stuff on their own
 *
 * The powerup that they contain is selected randomly when they re picked up
 *
 * @author Max Warren
 */

public class PowerUp extends GameEntity implements Serializable {

    /**
     * The size of the powerup box in the gameworld
     */
    public final static int POWERUP_SIZE = 20;

    /**
     * Makes a new powerup inside the game world
     * @param position
     */
    public PowerUp(Point position) {
        super(position, new Point(POWERUP_SIZE, POWERUP_SIZE));
    }

    /**
     * The object does not need anything to change when it is updated
     */
    @Override
    public void updateObject() {
        return;
    }

    /**
     * @return The sprite that displays the powerup on screen.
     */
    public Sprite getSprite() {
        return new Sprite(this.location, SpriteType.PICKUP_CRATE, this.id, 0);
    }
}