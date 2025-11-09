import java.net.*;
import java.io.*;

/**
 *  UDP Client that sends messages to server
 */
public class UDPClient {
    private String serverAddress;
    private int serverPort;
    private DatagramSocket socket;
    private int messageNumber = 0;

    public UDPClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void start() {
        try {
            socket = new DatagramSocket();
            InetAddress serverAddr = InetAddress.getByName(serverAddress);

            System.out.println("UDP Client connected to " + serverAddress + ":" + serverPort);
            System.out.println("Type your messages:\n");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;

            while ((line = reader.readLine()) != null) {
                messageNumber++;

                // Add message number to the text
                String fullMessage = messageNumber + ":" + line;
                byte[] data = fullMessage.getBytes();

                // Send packet
                DatagramPacket packet = new DatagramPacket(data, data.length,
                        serverAddr, serverPort);
                socket.send(packet);

                System.out.println("Sent [#" + messageNumber + "]: " + line);
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
        if (args.length < 2) {
            System.out.println("Usage: java UDPClient <host> <port>");
            System.out.println("Example: java UDPClient localhost 8080");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        UDPClient client = new UDPClient(host, port);
        client.start();
    }
}