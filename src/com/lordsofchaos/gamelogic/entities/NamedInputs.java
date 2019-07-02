package com.lordsofchaos.gamelogic.entities;

import com.lordsofchaos.networking.game.client.NetworkedMouseEvent;
import javafx.scene.input.InputEvent;

import java.io.Serializable;
import java.util.UUID;

/**
 * Named Input Events from Human Players
 *
 * @author Max Warren
 */
public class NamedInputs implements Serializable {

    private InputEvent event;
    private NetworkedMouseEvent mouseEvent;
    private UUID address;
    private boolean isMouseEvent;

    /**
     * Generates a new event for keyboard presses
     * @param event The keyboard press
     * @param address The UUID of the player requesting the command
     */
    public NamedInputs(InputEvent event, UUID address) {
        this.event = event;
        this.address = address;
        this.isMouseEvent = false;
    }

    /**
     * Generates a new event for mouse action
     * @param event The mouse event
     * @param address The UUID of the player requesting the command
     */
    public NamedInputs(NetworkedMouseEvent event, UUID address){
        this.mouseEvent = event;
        this.address = address;
        this.isMouseEvent = true;
    }

    /**
     * Return the keyboard  press event
     * @return The contained keyboard press event
     */
    public InputEvent getEvent() {
        return event;
    }

    /**
     * Return the identifier of the requesting player
     * @return The players UUID
     */
    public UUID getAddress() {
        return address;
    }

    /**
     * Return the Mouse event
     * @return The contained mouse event
     */
    public NetworkedMouseEvent getMouseEvent() {
        return mouseEvent;
    }

    /**
     * Check if the event is a mouse event
     * @return If the event is a mouse event
     */
    public boolean isMouseEvent() {
        return isMouseEvent;
    }
}
