# UDP/TCP Chat Application – Java for Networks (3RTS TP #2)

This project was developed as part of **3RTS – Java for Networks (TP #2, Room D067)** under the supervision of **S. Yese** . 
It explores how the **User Datagram Protocol (UDP)** works, its connectionless nature, and how we can make it more reliable through additional mechanisms such as acknowledgments and retransmissions.  
The project also includes experiments on packet loss, datagram size limits, and multicast communication.

## 🧠 Project Overview

The goal of this TP was to:
- Implement a **UDP client/server** chat application in Java.  
- Understand UDP’s **unreliable and connectionless** behavior.  
- Build a **reliable layer** over UDP with sequence numbers, ACKs, and retries.  
- Simulate **packet loss** and measure the effect on reliability.  
- Experiment with **multicast** (one-to-many) communication.

All programs are written in pure Java, using only standard `java.net` and `java.io` libraries.

## 📂 Project Files and Descriptions

### 🟢 1. UDPServer.java
A simple **UDP server** that listens on a given port (default: 8080) and prints any message it receives from clients.  
It continuously receives packets and displays them in the format:

### 🔵 2. UDPClient.java
A simple **UDP client** that sends messages to the server.  
Each message is automatically **numbered** before sending (`1:Hello`, `2:Test`, …) so that we can track message order on the server side.  
The client sends messages instantly without establishing a connection , showing how UDP transmits data directly without verification.

### 🧩 3. ReliableUDPServer.java
An improved version of the UDP server that implements **basic reliability features**:
- Reads the **sequence number** from each message.  
- Detects **duplicates** and **missing messages** (based on sequence numbers).  
- Sends an acknowledgment (ACK) back to the client
- 
### 🧩 4. ReliableUDPClient.java
A client that sends messages **reliably** over UDP:
- Adds a **sequence number** to every message.  
- Waits for a matching **ACK** from the server.  
- If no ACK is received within **2 seconds**, it **retries up to 3 times**.

### ⚙️ 5. PacketLossSimulator.java
This program **simulates network packet loss**.  
It listens on a UDP port and randomly **drops packets** based on a given loss rate (for example, 30% or 50%).  
If a packet is not dropped, it sends back an ACK.

### 🌐   6. MulticastSender.java
This file demonstrates one-to-many communication using UDP multicast.
It sends a message to a multicast group address (for example, 230.0.0.1 on port 8888), allowing all receivers subscribed to that group to receive the same message simultaneously.

### 🌐 7. MulticastReceiver.java
This receiver joins the same multicast group as the sender and listens for messages.
Every message sent by the sender appears instantly on all active receivers, showing how multicast efficiently distributes one message to many clients.
###  How to Run the Programs
### ➤ Basic UDP Chat
### Server:
javac UDPServer.java
java UDPServer 8080
### Client:
javac UDPClient.java
java UDPClient localhost 8080
Type messages in the client → they appear on the server instantly.

### ➤ Reliable UDP (with ACKs and Retries)
### Server:
javac ReliableUDPServer.java
java ReliableUDPServer 8080
### Client:
javac ReliableUDPClient.java
java ReliableUDPClient localhost 8080
### With Packet Loss
Simulator:
javac PacketLossSimulator.java
java PacketLossSimulator 8080 0.3
### ➤ Multicast Communication
Receiver (you can open several):
javac MulticastReceiver.java
java MulticastReceiver
### Sender:
javac MulticastSender.java
java MulticastSender
Type a message → it appears on all receivers simultaneously.
### 📊 What We Learned
UDP is connectionless and stateless, sending datagrams without handshake.
Reliability can be simulated manually with sequence numbers, acknowledgments, and retries.
Datagram transmission depends on buffer size , large messages can be truncated if the buffer is too small.
Multicast allows efficient one-to-many communication without repeated unicast transmissions.
In contrast, TCP automates reliability and flow control, which is why it’s preferred for guaranteed delivery.
