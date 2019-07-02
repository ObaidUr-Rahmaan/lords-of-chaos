package com.lordsofchaos.networking.shared;

import com.lordsofchaos.networking.game.client.GameClient;

import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Lobby Class containing all info for a game group of maximum 4 (Human and/or AI)
 *
 * @author Obaid Ur-Rahmaan
 */
public class Lobby implements Serializable {

    // Only set once when GameClient creates a Lobby
    public String name;
    private String password;

    // Set to the GameClient's IP that created this Lobby
    private InetAddress gameServerIPAddress;

    // Players in this lobby
    private CopyOnWriteArrayList<GameClient> currentInLobbyHumanGameClients;

    // GameClients in Lobby after Host clicks 'Start Game'
    private CopyOnWriteArrayList<Socket> TCPGameConnections;
    private CopyOnWriteArrayList<DatagramPacket> UDPGameConnections;   // Stores the IP and Port of Clients

    // AI bots in this lobby
    private int numberOfAIPlayers;

    // Constructor
    public Lobby(String name, String password, InetAddress gameServerIPAddress) {
        this.name = name;
        this.password = password;
        this.gameServerIPAddress = gameServerIPAddress;
        this.TCPGameConnections = new CopyOnWriteArrayList<>();
        this.UDPGameConnections = new CopyOnWriteArrayList<>();

        // Initialise the List of players and AI's
        this.currentInLobbyHumanGameClients = new CopyOnWriteArrayList<>();
    }

    public Lobby(InetAddress localHost) {
        this.gameServerIPAddress = localHost;
    }


    //---------------------------------------------------------------------------------------------


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public InetAddress getGameServerIPAddress() {
        return gameServerIPAddress;
    }

    public void setGameServerIPAddress(InetAddress gameServerIPAddress) {
        this.gameServerIPAddress = gameServerIPAddress;
    }

    public CopyOnWriteArrayList<GameClient> getCurrentInLobbyHumanGameClients() {
        return currentInLobbyHumanGameClients;
    }

    public void setCurrentInLobbyHumanGameClients(CopyOnWriteArrayList<GameClient> currentInLobbyHumanGameClients) {
        this.currentInLobbyHumanGameClients = currentInLobbyHumanGameClients;
    }

    public CopyOnWriteArrayList<Socket> getTCPGameConnections() {
        return TCPGameConnections;
    }

    public void addTCPGameConnectedClient(Socket gameConnectedClients) {
        this.TCPGameConnections.add(gameConnectedClients);
    }

    public CopyOnWriteArrayList<DatagramPacket> getUDPGameConnections() {
        return UDPGameConnections;
    }

    public void addUDPGameConnectedClients(DatagramPacket packet) {
        this.UDPGameConnections.add(packet);
    }

    public int getNumberOfAIPlayers() {
        return numberOfAIPlayers;
    }

    public void addAIPlayer() {
        this.numberOfAIPlayers = numberOfAIPlayers + 1;
    }

    public void removeAIPlayer() {
        this.numberOfAIPlayers = numberOfAIPlayers - 1;
    }

}
