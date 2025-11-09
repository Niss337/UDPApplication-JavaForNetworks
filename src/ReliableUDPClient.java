import java.net.*;
import java.io.*;

/**
 * UDP Client with retries - waits for ACK and retries if needed
 */
public class ReliableUDPClient {
    private String serverAddress;
    private int serverPort;
    private DatagramSocket socket;
    private int messageNumber = 0;

    public ReliableUDPClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void start() {
        try {
            socket = new DatagramSocket();
            InetAddress serverAddr = InetAddress.getByName(serverAddress);

            System.out.println("Reliable UDP Client started");
            System.out.println("Server: " + serverAddress + ":" + serverPort);
            System.out.println("Type your messages:\n");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;

            while ((line = reader.readLine()) != null) {
                sendWithRetry(line, serverAddr);
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    private void sendWithRetry(String message, InetAddress serverAddr) {
        try {
            messageNumber++;
            String fullMessage = messageNumber + ":" + message;

            boolean gotAck = false;
            int maxAttempts = 3;

            System.out.println("\nSending message #" + messageNumber + ": " + message);

            for (int attempt = 1; attempt <= maxAttempts && !gotAck; attempt++) {
                // Send message
                byte[] data = fullMessage.getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length,
                        serverAddr, serverPort);
                socket.send(packet);

                if (attempt > 1) {
                    System.out.println("  Retry #" + (attempt - 1));
                }

                // Wait for ACK
                try {
                    socket.setSoTimeout(2000); // 2 seconds timeout

                    byte[] ackBuffer = new byte[100];
                    DatagramPacket ackPacket = new DatagramPacket(ackBuffer,
                            ackBuffer.length);
                    socket.receive(ackPacket);

                    String ack = new String(ackPacket.getData(), 0, ackPacket.getLength());

                    if (ack.equals("ACK:" + messageNumber)) {
                        System.out.println("  ACK received!");
                        gotAck = true;
                    }

                } catch (SocketTimeoutException e) {
                    System.out.println("  Timeout - no ACK received");
                }
            }

            if (!gotAck) {
                System.out.println("  Failed after " + maxAttempts + " attempts");
            }

            socket.setSoTimeout(0); // Reset timeout

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java ReliableUDPClient <host> <port>");
            System.out.println("Example: java ReliableUDPClient localhost 8080");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        ReliableUDPClient client = new ReliableUDPClient(host, port);
        client.start();
    }
}