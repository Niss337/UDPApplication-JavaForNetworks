import java.net.*;
import java.io.*;

/**
 * Simple UDP Multicast Sender
 * Sends messages to all receivers that joined the multicast group.
 */
public class MulticastSender {

    public static void main(String[] args) {
        String groupAddress = "230.0.0.1";
        int port = 8888;

        try {
            MulticastSocket socket = new MulticastSocket(); // No need to bind
            InetAddress group = InetAddress.getByName(groupAddress);

            System.out.println("Type messages to send to the multicast group (type 'exit' to quit):");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String message;

            while ((message = reader.readLine()) != null) {
                if (message.equalsIgnoreCase("exit")) break;

                byte[] data = message.getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length, group, port);
                socket.send(packet);

                System.out.println("Message sent: " + message);
            }

            socket.close();
            System.out.println("Sender closed.");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
