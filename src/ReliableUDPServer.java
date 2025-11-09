import java.net.*;
import java.io.*;

/**
 * UDP Server with acknowledgments - sends ACK for each message
 */
public class ReliableUDPServer {
    private int port;
    private DatagramSocket socket;
    private int lastSeq = 0;

    public ReliableUDPServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            socket = new DatagramSocket(port);

            System.out.println("Reliable UDP Server started on port " + port);
            System.out.println("Waiting for messages...\n");

            byte[] buffer = new byte[1024];

            while (true) {
                // Receive message
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                InetAddress clientAddr = packet.getAddress();
                int clientPort = packet.getPort();

                String message = new String(packet.getData(), 0, packet.getLength());

                // Split sequence number and text
                String[] parts = message.split(":", 2);
                if (parts.length == 2) {
                    int seq = Integer.parseInt(parts[0]);
                    String text = parts[1];

                    System.out.println("Received from " + clientAddr.getHostAddress() +
                            ":" + clientPort);
                    System.out.println("  Sequence: " + seq);
                    System.out.println("  Message: " + text);

                    // Check for issues
                    if (seq == lastSeq) {
                        System.out.println("  WARNING: Duplicate!");
                    } else if (seq > lastSeq + 1 && lastSeq > 0) {
                        System.out.println("  WARNING: Lost " + (seq - lastSeq - 1) +
                                " messages");
                    }

                    lastSeq = seq;

                    // Send acknowledgment
                    String ack = "ACK:" + seq;
                    byte[] ackData = ack.getBytes();
                    DatagramPacket ackPacket = new DatagramPacket(ackData, ackData.length,
                            clientAddr, clientPort);
                    socket.send(ackPacket);
                    System.out.println("  ACK sent\n");
                }
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

        ReliableUDPServer server = new ReliableUDPServer(port);
        server.start();
    }
}