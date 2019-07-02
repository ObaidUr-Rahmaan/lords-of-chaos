package com.lordsofchaos.networking.global;

import org.apache.commons.validator.routines.InetAddressValidator;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Global Constants that the Game GameServer and Connected Game Clients have access to
 *
 * RUN THIS CLASS TO GET YOUR VALID IP !
 *
 * @author Obaid Ur-Rahmaan
 */
public class Constants {

    public static int MAX_CONNECTIONS = 4;

    public static int TCP_SERVER_PORT = 4000;
    public static int UDP_SERVER_PORT = 5000;

    public static int BUFFER_SIZE = 64000;

    // Set to the host's IP
    // Obaid's IP (Host) on Ali's HotSpot: 172.20.10.4
    public static String SINGLE_PLAYER_SERVER_IP = "127.0.0.1";
    public static String MULTI_PLAYER_SERVER_IP = "192.168.70.1";

    public static InetAddress getGameServerIPAddress() {

        ArrayList<InetAddress> localAddresses = getLocalAddresses();

        ArrayList<InetAddress> validAddresses = new ArrayList<>();

        if (localAddresses != null) {
            for (InetAddress inetAddress : localAddresses) {
                if (InetAddressValidator.getInstance().isValidInet4Address(inetAddress.getHostAddress())) {
//                    System.out.println(validAddresses);
                    validAddresses.add(inetAddress);
                }
            }
        }

        // Last one is the actual one required for Networking to work
        return validAddresses.get(validAddresses.size() - 1);

    }

    private static ArrayList<InetAddress> getLocalAddresses() {

        try {
            ArrayList<InetAddress> addresses = new ArrayList<>();
            Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
            while (b.hasMoreElements()) {
                for (InterfaceAddress interfaceAddress : b.nextElement().getInterfaceAddresses()) {
                    addresses.add(interfaceAddress.getAddress());
                }
            }

            return addresses;
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        System.out.println(Constants.getGameServerIPAddress());
    }

}
