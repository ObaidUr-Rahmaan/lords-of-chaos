package com.lordsofchaos.networking.game.client;

import com.lordsofchaos.gamelogic.transmission.GameState;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Client Thread continuously sending the Valid KeyEvents to the Server
 * <p>
 * 'W' -> Move UP
 * 'S' -> Move DOWN
 * 'A' -> Move LEFT
 * 'D' -> Move RIGHT
 * 'SPACE' -> Fire BULLET
 * 'MOUSE CLICK' -> FIRE BULLET
 *
 * @author Obaid Ur-Rahmaan
 */
public class ClientSender extends Task {

    // ObjectOutputStream to send Server KeyEvent
    private ObjectOutputStream outputStream;

    // THIS Scene the Client is listening on
    private Scene scene;

    private GameState latestState;

    public ClientSender(ObjectOutputStream outputStream, Scene scene, GameState latestState) {
        this.outputStream = outputStream;
        this.scene = scene;
        this.latestState = latestState;
    }

    @Override
    protected Object call() {

        scene.setOnMouseMoved(event -> {
            try {
                //System.out.println(event.getClass());
                this.outputStream.writeObject(new NetworkedMouseEvent(event));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        scene.setOnMouseClicked(event -> {
            try {
                this.outputStream.writeObject(new NetworkedMouseEvent(event));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Listen for Key Presses
        scene.setOnKeyPressed(event -> {

            KeyCode keyCode = event.getCode();

            try {
                switch (keyCode) {
                    case W:
                        this.outputStream.writeObject(event);
                        break;
                    case S:
                        this.outputStream.writeObject(event);
                        break;
                    case D:
                        this.outputStream.writeObject(event);
                        break;
                    case A:
                        this.outputStream.writeObject(event);
                        break;
                    case SPACE:
                        this.outputStream.writeObject(event);
                        break;
                }
            } catch (IOException e) {
                System.out.println("Unable to send KeyEvent");
            }

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

        // Listen for Key Releases
        scene.setOnKeyReleased(event -> {

            KeyCode keyCode = event.getCode();

            try {
                switch (keyCode) {
                    case W:
                        ClientSender.this.outputStream.writeObject(event);
                        break;
                    case S:
                        ClientSender.this.outputStream.writeObject(event);
                        break;
                    case D:
                        ClientSender.this.outputStream.writeObject(event);
                        break;
                    case A:
                        ClientSender.this.outputStream.writeObject(event);
                        break;
                    case SPACE:
                        ClientSender.this.outputStream.writeObject(event);
                        break;
                }
            } catch (IOException e) {
                System.out.println("Unable to send KeyEvent");
            }

        });

        return new Object();
    }


}
