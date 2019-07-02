package com.lordsofchaos.networking.game.server;

import com.lordsofchaos.gamelogic.GameWorld;
import com.lordsofchaos.gamelogic.transmission.GameState;
import com.lordsofchaos.networking.game.client.NetworkedMouseEvent;
import com.lordsofchaos.networking.shared.Lobby;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.UUID;

/**
 * Listens and receives Key/Mouse Event from Clients + Queues for GameLogic to handle
 *
 * @author
 */
public class ServerReceiver implements Runnable {

    private Socket TCPSocket;
    private DatagramSocket UDPSocket;
    private Lobby lobby;
    private GameWorld gameWorld;
    public static GameState latestGameState;

    private UUID myUUID;
    ServerEchoer combined;

    private ObjectInputStream inputStream;

    private boolean killSwitch = false;

    public ServerReceiver(Socket TCPSocket, DatagramSocket UDPSocket, Lobby lobby, GameWorld gameWorld,
                          ObjectInputStream inputStream, GameState latestGameState, UUID myUUID, ServerEchoer combined) {
        this.TCPSocket = TCPSocket;
        this.UDPSocket = UDPSocket;
        this.lobby = lobby;
        this.gameWorld = gameWorld;
        this.latestGameState = latestGameState;
        this.inputStream = inputStream;
        this.myUUID = myUUID;
        this.combined = combined;
    }

    @Override
    public void run() {
        // Continuously Listen for KeyEvent Press + Send to GameLogic
        try {

            System.out.println("Sent Shared GameWorld to Client");
            while (!killSwitch) {
                // Receive KeyEvents from Clients
                try {

                    Object event = this.inputStream.readObject();
                    if(event instanceof String){
                        if((event).equals("end")) {
                            //System.out.println("done");
                            combined.endStreams();
                        }
                    }
                    if(event instanceof KeyEvent){
                        gameWorld.queueAction((KeyEvent)event, myUUID);

                    }
                    else if(event instanceof NetworkedMouseEvent){
                        gameWorld.queueAction((NetworkedMouseEvent) event, myUUID);
                    }
                    // Send KeyEvent to GameLogic for Processing

                    // Receive new GameState from GameLogic + Set to the latestGameState
                    ServerReceiver.latestGameState = gameWorld.getGameState();

                } catch (IOException e) {
                    System.out.println("Socket Exception");
                    break;
                } catch (ClassNotFoundException e) {
                    System.out.println("Could not receive KeyEvent from Connected Client");
                }
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                System.out.println("Lost connection with Connected Client");
                System.out.println("Closing Server Receiver...");
                System.out.println("Closing Streams Gracefully...");
                this.inputStream.close();
                this.TCPSocket.close();
            } catch (IOException e) {
                // Oh, well!
            }
        }
    }

    public void end(){
        killSwitch = true;
    }
}
