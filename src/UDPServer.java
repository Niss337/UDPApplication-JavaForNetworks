import java.net.*;
import java.io.*;

/**
 * UDP Server that receives messages from clients
 */
public class UDPServer {
    private int port;
    private DatagramSocket socket;

    public UDPServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            socket = new DatagramSocket(port);

            System.out.println("UDP Server started on port " + port);
            System.out.println("Waiting for messages...\n");

            byte[] buffer = new byte[1024];

            while (true) {
                // Receive packet
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                // Get client info
                InetAddress clientAddr = packet.getAddress();
                int clientPort = packet.getPort();

                // Get message
                String message = new String(packet.getData(), 0, packet.getLength());

                // Display message
                System.out.println("[" + clientAddr.getHostAddress() + ":" +
                        clientPort + "] " + message);
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    public static void main(String[] args) {
        int port = 8080;

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        UDPServer server = new UDPServer(port);
        server.start();
    }
}