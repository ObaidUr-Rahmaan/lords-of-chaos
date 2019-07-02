package com.lordsofchaos.networking.game.server;

import com.lordsofchaos.gamelogic.GameWorld;
import com.lordsofchaos.networking.global.Constants;
import com.lordsofchaos.networking.shared.Lobby;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Game GameServer listens to each GameClient connecting and creates a
 * new Echoer to handle each one.
 * <p>
 * Started from Host GameClient
 *
 * @author Obaid Ur-Rahmaan
 */
public class GameServer implements Runnable {

    private Lobby lobby;
    private GameWorld gameWorld;

    // Can only create one
    private DatagramSocket UDPSocket;

    private ArrayList<ServerEchoer> clientHandlers;

    public GameServer(Lobby lobby, GameWorld gameWorld) {
        this.lobby = lobby;
        this.gameWorld = gameWorld;
        this.clientHandlers = new ArrayList<>();
    }

    @Override
    public void run() {
        try (ServerSocket TCPSocket = new ServerSocket(Constants.TCP_SERVER_PORT)) {

            this.UDPSocket = new DatagramSocket(Constants.UDP_SERVER_PORT);

            System.out.println("GameServer Running.");
            System.out.println("IP: " + Constants.MULTI_PLAYER_SERVER_IP);

            int numberOfHumanClientsInLobby = gameWorld.getNumberOfHumanPlayers();

            // Loop creating new ServerEchoer's for each GameClient
            for (int i = 0; i < numberOfHumanClientsInLobby; i++) {
                // Receive UDP Packet first
                byte[] buffer = new byte[80];
                DatagramPacket blankPacket = new DatagramPacket(buffer, buffer.length);
                UDPSocket.receive(blankPacket);
                UUID clientsUUID = SerializationUtils.deserialize(buffer);
                System.out.println("UDP Connection Setup");
                // Then Set up TCP Connection
                Socket clientConnected = TCPSocket.accept();
                System.out.println("Listening for Clients to connect...");
                System.out.println("GameClient " + clientConnected.getInetAddress() + " connected");
                System.out.println("TCP Connection Setup");

                this.lobby.addTCPGameConnectedClient(clientConnected);
                this.lobby.addUDPGameConnectedClients(blankPacket);
                System.out.println("Twin Protocol Setup Successfully (UDP/TCP)");

                ServerEchoer clientHandler = new ServerEchoer(clientConnected, UDPSocket, clientsUUID);
                this.clientHandlers.add(clientHandler);

                gameWorld.addPlayer(clientsUUID);

                System.out.println("Added Client " + clientConnected.getInetAddress() + " to GameWorld");
                System.out.println("Connected Clients: " + this.lobby.getTCPGameConnections().size());
                System.out.println();
            }

            System.out.println("Created Local Servers Threads for all Clients in Lobby");

            // For each of the ClientHandlers, set the Shared Lobby and the Shared GameWorld
            for (ServerEchoer clientHandler : this.clientHandlers) {
                clientHandler.setGameWorld(gameWorld);
                clientHandler.setLobby(lobby);
                clientHandler.setLatestGameState(gameWorld.getGameState());
                clientHandler.start();

                Thread.sleep(300);
            }

            System.out.println("Starting Game World...");
            gameWorld.start();
            System.out.println("Game World started");
            int playersLeft = clientHandlers.size();
            while (clientHandlers.size() > 0){
                for(int i = 0; i < clientHandlers.size(); i++){

                    if(clientHandlers.get(i).isDead()){
                        clientHandlers.remove(i);
                    }
                }
            }
            this.UDPSocket.close();
            this.gameWorld.setDead(true);

            // AI Players are automatically added by GameWorld since AI and GameLogic communicate directly

        } catch (IOException e) {
            System.out.println("GameServer exception " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
