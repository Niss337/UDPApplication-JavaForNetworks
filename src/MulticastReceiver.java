import java.net.*;
import java.io.*;

/**
 * Simple UDP Multicast Receiver
 * Listens to a multicast group and prints all received messages.
 */
public class MulticastReceiver {
    public static void main(String[] args) {
        String groupAddress = "230.0.0.1";  // same as sender
        int port = 8888;                    // same as sender

        MulticastSocket socket = null;

        try {
            // create socket bound to the multicast port
            socket = new MulticastSocket(port);

            // resolve group address
            InetAddress group = InetAddress.getByName(groupAddress);

            // join group
            socket.joinGroup(group);
            System.out.println("Joined multicast group " + groupAddress + " on port " + port);

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received: " + message);
            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}
