package com.lordsofchaos.gamelogic;

import com.lordsofchaos.gamelogic.transmission.GameState;
import com.lordsofchaos.gamelogic.transmission.Sprite;

import java.awt.*;
import java.awt.event.InputEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Test
 *
 * @author Max Warren
 */
public class GameWorldTesting {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Enter an argument to run a test");
            return;
        }

        switch (args[0]) {
            case "mapGenTest":
                mapTest();
                break;
            case "runningTest":
                //runningTest();
                break;
        }

    }

    /*private static void runningTest() {
        GameWorld testWorld = new GameWorld(1, 3);
        try {
            //testWorld.addPlayer(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("playerAdded");
        testWorld.start();
        while (true){
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //for (Sprite s: testWorld.getGameState().getObjects()){
            //    System.out.println(s.id + " " + s.image + " " + s.location.x + " " + s.location.y);
            //}
            //System.out.println("\n\n");
        }
    }*/

    private static void mapTest(){
        GameWorld testWorld = new GameWorld(3 ,1, false);
        System.out.println("World Made");
        GameState checking = testWorld.getGameState();
        for (int y = 0; y < checking.getMaze().length; ++y) {
            for (int x = 0; x < checking.getMaze()[y].length; ++x) {
                if (checking.getMaze()[y][x]) {
                    System.out.print("#");
                } else {
                    System.out.print("-");
                }
            }
            System.out.println();
        }
    }
}