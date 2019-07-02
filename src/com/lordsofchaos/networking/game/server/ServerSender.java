package com.lordsofchaos.networking.game.server;

import com.lordsofchaos.gamelogic.GameWorld;
import com.lordsofchaos.gamelogic.transmission.GameState;
import com.lordsofchaos.networking.shared.Lobby;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.UUID;

/**
 * Continuously Sends the latest GameState to all Clients in the Game
 *
 * @author Obaid Ur-Rahmaan
 */
public class ServerSender implements Runnable {

    private DatagramSocket UDPSocket;
    private Lobby lobby;
    private GameWorld gameWorld;
    private GameState latestGameState;

    private UUID myUUID;
    private boolean killSwitch = false;

    private ObjectOutputStream outputStream;

    public ServerSender(DatagramSocket UDPSocket, ObjectOutputStream outputStream, Lobby lobby,
                        GameState latestGameState, GameWorld gameWorld, UUID myUUID) {
        this.UDPSocket = UDPSocket;
        this.outputStream = outputStream;
        this.lobby = lobby;
        this.latestGameState = latestGameState;
        this.gameWorld = gameWorld;
        this.myUUID = myUUID;
    }

    @Override
    public void run() {

        try {

            // Send GameWorld to Client first
            synchronized (gameWorld) {
                this.outputStream.writeObject(gameWorld);
            }

            byte[] buffer;
            while (!killSwitch) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /*for (Sprite s : latestGameState.getObjects()) {
                    System.out.println(s.id + " " + s.image + " " + s.location.x + " " + s.location.y);
                }
                System.out.println("\n\n");*/
                // Continuously send a Packet of the latestGameState to all Clients in the Lobby Game (loop)

                for (DatagramPacket UDPConnections : lobby.getUDPGameConnections()) {
                    synchronized (this.latestGameState) {
                        buffer = SerializationUtils.serialize(this.gameWorld.getUniqueGameState(myUUID));
                    }
                    this.UDPSocket.send(new DatagramPacket(buffer, buffer.length, UDPConnections.getAddress(), UDPConnections.getPort()));
                }
                ServerReceiver.latestGameState.soundsSent();
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.outputStream.close();

        } catch (IOException e) {
            System.out.println("Closing Server Sender...");
            System.out.println("Closing Streams Gracefully...");
            try {
                this.outputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }


    }

    public void end(){
        killSwitch = true;
    }

}
