package com.lordsofchaos.networking.game.client;

import com.lordsofchaos.gamelogic.transmission.GameState;
import com.lordsofchaos.networking.global.Constants;
import javafx.concurrent.Task;
import org.apache.commons.lang3.SerializationUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Client Thread managing Receiving of GameStates after GameLogic processing
 *
 * @author Obaid Ur-Rahmaan
 */
public class ClientReceiver extends Task {

    private DatagramSocket UDPSocket;

    static GameState latestState;

    ClientReceiver(DatagramSocket UDPSocket, GameState latestState) {
        this.UDPSocket = UDPSocket;
        ClientReceiver.latestState = latestState;
    }

    @Override
    protected Object call() throws Exception {

        while (true) {
            // Receive new Game State from Server
            byte[] buffer = new byte[Constants.BUFFER_SIZE];
            DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
            UDPSocket.receive(receivedPacket);
            latestState = SerializationUtils.deserialize(receivedPacket.getData());
        }
    }
}
