package com.lordsofchaos.gamelogic.transmission;

import java.io.Serializable;

/**
 * Sprite Type
 * Different type of sprites which may be displayed
 * @author Max Warren
 */
public enum SpriteType implements Serializable {
    /**
     * Normal player Sprite
     */
    PLAYER_BASIC,
    /**
     * Legacy type for AI
     */
    PLAYER_UP,
    /**
     * Legacy type for AI
     */
    PLAYER_DOWN,
    /**
     * Legacy type for AI
     */
    PLAYER_LEFT,
    /**
     * Legacy type for AI
     */
    PLAYER_RIGHT,
    /**
     * Displays a bullet
     */
    BULLET,
    /**
     * Displays a rocket
     */
    ROCKET,
    /**
     * Displays the powerup graphic
     */
    PICKUP_CRATE,
    /**
     * Display an explosion
     */
    EXPLOSION
}
