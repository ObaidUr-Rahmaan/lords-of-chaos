package com.lordsofchaos.ui.controllers;

import com.lordsofchaos.gamelogic.GameWorld;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput implements KeyListener {


    private boolean w = false;
    private boolean a = false;
    private boolean s = false;
    private boolean d = false;
    private boolean space = false;
    private GameWorld player;


    public KeyInput(GameWorld world) {
        this.player = world;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        System.out.println(keyEvent.getKeyChar());
        char c = keyEvent.getKeyChar();
        switch (c) {
            case 'w':
                w = true;
                break;
            case 'a':
                a = true;
                break;
            case 's':
                s = true;
                break;
            case 'd':
                d = true;
                break;
            case ' ':
                space = true;
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        char c = keyEvent.getKeyChar();
        switch (c) {
            case 'w':
                w = false;
                break;
            case 'a':
                a = false;
                break;
            case 's':
                s = false;
                break;
            case 'd':
                d = false;
                break;
            case ' ':
                space = false;
                break;
        }
    }

    /*public void sendCommands() {
        if (w) {
            player.PrototypeQueueAction("w");
        }
        if (a) {
            player.PrototypeQueueAction("a");
        }
        if (s) {
            player.PrototypeQueueAction("s");
        }
        if (d) {
            player.PrototypeQueueAction("d");
        }
        if (space) {
            player.PrototypeQueueAction(" ");
        }
    }*/
}
