import java.net.*;
import java.util.Random;

/**
 * Simulates packet loss by randomly dropping packets
 */
public class PacketLossSimulator {
    private int port;
    private double lossRate; // 0.0 to 1.0
    private Random random;

    public PacketLossSimulator(int port, double lossRate) {
        this.port = port;
        this.lossRate = lossRate;
        this.random = new Random();
    }

    public void start() {
        try {
            DatagramSocket socket = new DatagramSocket(port);

            System.out.println("Packet Loss Simulator");
            System.out.println("Port: " + port);
            System.out.println("Loss rate: " + (int)(lossRate * 100) + "%");
            System.out.println("Randomly dropping packets!\n");

            byte[] buffer = new byte[1024];
            int received = 0;
            int dropped = 0;

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                received++;

                InetAddress clientAddr = packet.getAddress();
                int clientPort = packet.getPort();

                String message = new String(packet.getData(), 0, packet.getLength());

                // Randomly decide to drop packet
                if (random.nextDouble() < lossRate) {
                    dropped++;
                    System.out.println("DROPPED packet #" + received);
                    System.out.println("  Total dropped: " + dropped + "/" + received +
                            " (" + (dropped * 100 / received) + "%)\n");
                } else {
                    System.out.println("ACCEPTED packet #" + received);

                    // Send ACK back
                    String[] parts = message.split(":", 2);
                    if (parts.length >= 1) {
                        String ack = "ACK:" + parts[0];
                        byte[] ackData = ack.getBytes();
                        DatagramPacket ackPacket = new DatagramPacket(ackData, ackData.length,
                                clientAddr, clientPort);
                        socket.send(ackPacket);
                        System.out.println("  ACK sent\n");
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int port = 8080;
        double lossRate = 0.3; // 30% default

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            lossRate = Double.parseDouble(args[1]);
        }

        PacketLossSimulator sim = new PacketLossSimulator(port, lossRate);
        sim.start();
    }
}