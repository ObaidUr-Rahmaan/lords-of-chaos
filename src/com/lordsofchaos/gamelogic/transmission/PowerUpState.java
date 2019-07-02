package com.lordsofchaos.gamelogic.transmission;

/**
 * Lists which powerups a player can have
 * @author Max Warren
 */
public enum PowerUpState {
    /**
     * No current powerup
     */
    normal,
    /**
     * The players movement is slowed
     */
    slow,
    /**
     * The player cannot be hurt
     */
    invincible,
    /**
     * The player can fire multiple shots per trigger pull
     */
    multishot,
    /**
     * The player can fire a more powerful shot
     */
    rocket,
}
