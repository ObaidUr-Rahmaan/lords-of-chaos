package com.lordsofchaos.networking.game.client;

import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.Serializable;

/**
 * The Javafx mouse events cannot be reliably serialized (Contrary to their specification).
 * This class takes the relevent information from a Javafx mouse event and turns it into
 * a class that is definitely serializable
 *
 * @author Max Warren
 */
public class NetworkedMouseEvent implements Serializable {

    /**
     * Whether the mouse was clicked or merely hovering
     */
    private boolean clicked;
    /**
     * The location on the screen that the mouse was located
     */
    private  Point location;

    /**
     * Makes a network compatible version of a mouse event
     * @param e The javafx mouse event to be changed
     */
    public NetworkedMouseEvent(MouseEvent e){
        this.location = new Point((int)e.getSceneX(), (int)e.getSceneY());
        if(e.getEventType() == MouseEvent.MOUSE_CLICKED){
            clicked = true;
        }
        else{
            clicked = false;
        }
    }

    public boolean isClicked() {
        return clicked;
    }

    public Point getLocation() {
        return location;
    }


}
