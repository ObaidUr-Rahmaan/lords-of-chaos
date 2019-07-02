package com.lordsofchaos.gamelogic.entities.player;

import com.lordsofchaos.gamelogic.GameWorld;
import com.lordsofchaos.gamelogic.entities.NamedInputs;
import com.lordsofchaos.networking.game.client.NetworkedMouseEvent;
import javafx.scene.input.KeyEvent;


import java.awt.*;
import java.io.Serializable;

/**
 * Human Player
 * Players which have a human controller
 * @author Max Warren
 */
public class HumanPlayer extends Player implements Serializable {



    private boolean leftPressed =false;
    private boolean rightPressed =false;
    private boolean upPressed = false;
    private boolean downPressed = false;


    /**
     * Generates a new player
     * @param location
     * @param myWorld
     */
    public HumanPlayer(Point location, GameWorld myWorld) {
        super(location, myWorld);
        this.id = myWorld.getNextId();
        this.fixedId = true;
        this.fireCooldown = 0;
    }

    /**
     * Updates the internal state of the object
     * Sets their movement according to the currently pressed buttons
     */
    @Override
    public void updateObject() {
        //System.out.println(Math.atan2((double)velocity.x, (double)velocity.y));
        super.updateObject();
        //System.out.println("think");
        this.correctVelocity(this.speed);
    }


    private void correctVelocity(int speed) {
        this.setVelocity(new Point(0,0 ));
        //System.out.println("stopped");
        double directionRadians = ((double) direction) * Math.PI /180.0;
        if(upPressed && ! downPressed){
            this.setVelocity(new Point((int) (Math.sin(directionRadians)*speed) ,-(int) (Math.cos(directionRadians)*5)));
        }
        if(downPressed && ! upPressed){
            this.setVelocity(new Point(-(int) (Math.sin(directionRadians)*speed) ,(int) (Math.cos(directionRadians)*5)));

        }
        if(leftPressed && ! rightPressed){
            this.direction = this.direction - 4;
            if(this.direction < 0){
                this.direction = 360 + direction;
            }
        }
        if(rightPressed && ! leftPressed){
            this.direction = this.direction + 4;
            if(this.direction > 360){
                this.direction = -360 + direction;
            }
        }
        if(this.getVelocity().x == 0 && this.getVelocity().y == 0){
            return;
        }
    }


    /**
     * Process a new input command
     * Each movement key is tracked to check if it is pressed.
     * The mouses location is tracked every frame
     * @param command The command to be performed by the player
     */
    public void processEvent(NamedInputs command) {
        try {
            if(!command.isMouseEvent()){
                KeyEvent keyCommand = (KeyEvent) command.getEvent();
                String inputButton = keyCommand.getText();
                //System.out.println(":::" + inputButton + ":::" + keyCommand.getEventType());
                boolean pressed = false;
                if(keyCommand.getEventType() == KeyEvent.KEY_PRESSED){
                    pressed = true;
                }
                else if(keyCommand.getEventType() == KeyEvent.KEY_RELEASED){
                    pressed = false;
                }
                switch (inputButton) {
                    case "a":
                        leftPressed = pressed;
                        break;
                    case "d":
                        rightPressed = pressed;
                        break;
                    case "w":
                        upPressed = pressed;
                        break;
                    case "s":
                        downPressed = pressed;
                        break;
                    case " ":
                        if(pressed){
                            this.fire();
                        }
                        break;
                }
            }
            else{
                NetworkedMouseEvent event = command.getMouseEvent();
                this.setLooking(event.getLocation());
                if(event.isClicked()){
                    this.fire();
                }
            }
        } catch (ClassCastException e) {
            System.out.println("Input event handled incorrectly");
        }

    }

}
