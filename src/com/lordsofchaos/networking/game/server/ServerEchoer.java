package com.lordsofchaos.networking.game.server;

import com.lordsofchaos.gamelogic.GameWorld;
import com.lordsofchaos.gamelogic.transmission.GameState;
import com.lordsofchaos.networking.shared.Lobby;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Game GameServer handling each GameClient
 *
 * @author Obaid Ur-Rahmaan
 */
public class ServerEchoer extends Thread {

    private Socket TCPSocket;
    private DatagramSocket UDPSocket;
    private Lobby lobby;
    private GameWorld gameWorld;
    private GameState latestGameState;

    // Streams
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private ServerReceiver serverReceiver;
    private ServerSender serverSender;
    private boolean dead = false;
    //Identity
    UUID myUUID;

    public ServerEchoer(Socket TCPSocket, DatagramSocket UDPSocket, UUID myUUID) {
        // Create Sockets and Streams
        try {
            this.UDPSocket = UDPSocket;
            this.TCPSocket = TCPSocket;
            this.outputStream = new ObjectOutputStream(this.TCPSocket.getOutputStream());
            this.inputStream = new ObjectInputStream(this.TCPSocket.getInputStream());
            this.myUUID = myUUID;
        } catch (IOException e) {
            System.out.println("Couldn't setup Server Sockets and/or Streams");
        }
    }

    @Override
    public void run() {

        // new Thread Pool of Server Workers
        ExecutorService gameServerHandler = Executors.newFixedThreadPool(2);
        serverReceiver = new ServerReceiver(TCPSocket, UDPSocket, lobby, gameWorld, inputStream, latestGameState ,this.myUUID , this);
        gameServerHandler.execute(new Thread(serverReceiver));
        serverSender = new ServerSender(UDPSocket, outputStream, lobby, gameWorld.getGameState(), gameWorld, this.myUUID);
        gameServerHandler.execute(new Thread(serverSender));

    }

    public void endStreams(){
        serverReceiver.end();
        serverSender.end();
        dead = true;
    }

    /**
     * Sets the Lobby of the this Game
     * @param lobby Lobby to set
     */
    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    /**
     * Sets the GameWorld of this Game
     * @param gameWorld GameWorld to set
     */
    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    /**
     * Sets the GameState of this Game
     * @param latestGameState GameState to setg
     */
    public void setLatestGameState(GameState latestGameState) {
        this.latestGameState = latestGameState;
    }

    public boolean isDead() {
        return dead;
    }
}