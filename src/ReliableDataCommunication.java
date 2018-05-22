import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

public class ReliableDataCommunication {
	// Set max size of chunks we want to send.
	private static int packetSize = 997;
	private static int  packetSizeWithSequenceNumber = packetSize + 1;

	public static void main(String[] args) throws IOException {
		// If there are no arguments given, the program will behave as the RECEIVER.
		if(args.length == 0) {
			int nextExpectedSequenceNumber = 1;

			DatagramSocket serverSocket = new DatagramSocket(4445);
			boolean running = true;

			int packetNumber = 1;
			while(running) {
				// ***************************************************************
				// TODO HERE: Write code to send ACK to client.




				// ***************************************************************
				System.out.println("\nPacket number: " + packetNumber);

				byte[] bytesReceived = new byte[packetSizeWithSequenceNumber];
				DatagramPacket packetReceived = new DatagramPacket(bytesReceived, bytesReceived.length);

				try {
					// NOTE: This receive method is BLOCKING.
					serverSocket.receive(packetReceived);
				} catch (IOException e) {
					// Do nothing for now.
				}
				
				// Get packet number so we can verify that we are getting things in correct order.
				int receivedPacketNumber = bytesReceived[packetSizeWithSequenceNumber - 1];
				System.out.println("receivedPacketNumber: " + receivedPacketNumber);
				System.out.println("nextExpectedSequenceNumber: " + nextExpectedSequenceNumber);
				if(receivedPacketNumber != nextExpectedSequenceNumber) {
					continue;
				} else { // Else, we know we got the right packet and can move on to expecting the next one.
					nextExpectedSequenceNumber++;
				}

				// Get IP address and port of client who sent packet.
				InetAddress clientAddress = packetReceived.getAddress();
				int clientPort = packetReceived.getPort();
//				System.out.println("\nAddress of sending machine is: " + clientAddress);
				// Get port number of client process that sent packet.
//				System.out.println("Port number of client process that sent packet: " + clientPort);

				String received = new String(packetReceived.getData(), 0, packetReceived.getLength());
				System.out.println("From Client: " + received);

				// ***************************************************************
				// TODO HERE: Write code to send ACK to client.
				byte[] ackToSend = new byte[1];
				ackToSend[0] = (byte) nextExpectedSequenceNumber;
				DatagramPacket ackPacket = new DatagramPacket(ackToSend, ackToSend.length, clientAddress, clientPort);
				serverSocket.send(ackPacket);


				// ***************************************************************

				packetNumber++;
			}
			serverSocket.close();
			
		} else { // If there ARE arguments, the program will behave as the SENDER.
			DatagramSocket clientSocket = new DatagramSocket();
			byte[] serverIPAddressInBytes = {(byte) 192, (byte) 168, (byte) 3, (byte) 163};
			InetAddress serverIP = InetAddress.getByAddress(serverIPAddressInBytes);
			System.out.println("Server IP is: " + serverIP.toString());
			int serverPort = 4445;

//			boolean isConnectionEstablished = false;
//			while(!isConnectionEstablished) {
//				// Send a first small packet to establish connection.
//				byte[] greetingByte = new byte[1];
//				greetingByte[0] = 'h';
//				DatagramPacket packetToSend = new DatagramPacket(greetingByte, greetingByte.length, serverIP, serverPort);
//				clientSocket.send(packetToSend);
//				
//				
//			}


			// Read in file.
			Path path = Paths.get("fileToSend");
			byte[] fullFile = Files.readAllBytes(path);
			System.out.println(fullFile.length);
			for(byte b : fullFile) {
				//				System.out.print((char) b);
			}

			// Break it into chunks no larger than the specified packet size (997 in the case of this exercise).
			int fileSize = fullFile.length;
			int numberOfPacketsNeeded = (fileSize / packetSize);
			if(fileSize % packetSize != 0) { // Need one extra packet if the total file size is not divisible by the packet size.
				numberOfPacketsNeeded += 1;
			}
			System.out.println("Number of packets needed: " + numberOfPacketsNeeded);
			System.out.println();;

			// Start at 2 instead of 1 because the next one we are expecting is two.
			int nextExpectedAck = 2;
			
			for(int i=0; i<numberOfPacketsNeeded; i++) {
				int currentSequenceNumber = i + 1;
				byte[] fileChunkToSend = new byte[packetSizeWithSequenceNumber];

				// Get chunk to send.
				for(int j=0; j<packetSize; j++) {
					// Need check to avoid index out of bounds exception. This is only really problematic when
					// filling the LAST chunk.
					if((i * packetSize) + j >= fileSize) {
						break;
					}

					// Just a bit of math to make sure we get the correct byte from our fullFile array.
					fileChunkToSend[j] = fullFile[(i * packetSize) + j];
				}

				// Add sequence number as last byte in chunk.
				// NOTE: This approach is not bulletproof for several reasons. One of the reasons is that if the
				// currentSequenceNumber gets so big that it can no longer be represented by a single byte, then this will
				// break since the fileChunkToSend is an array of bytes.
				fileChunkToSend[packetSizeWithSequenceNumber - 1] = (byte) currentSequenceNumber;

				// Send it to receiver.
				// NOTE: The server's IP and port number are specified here when we create the packet to send.
				DatagramPacket packetToSend = new DatagramPacket(fileChunkToSend, fileChunkToSend.length, serverIP, serverPort);

				// ***************************************************************
				// TODO HERE: Write code to receive ACK from server and wait until
				// you receive this before moving on to the formation of the next
				// chunk. Continue attempting to send this chunk until you receive
				// the corresponding ACK.
				
				boolean isReadyForNextChunk = false;
				while(!isReadyForNextChunk) {
					// Try to send the packet.
					clientSocket.send(packetToSend);

					byte[] ackReceived = new byte[1];
					DatagramPacket ackPacketReceived = new DatagramPacket(ackReceived, ackReceived.length);
					
					System.out.println("nextExpectedAck: " + nextExpectedAck);
					
					try {
						// NOTE: This receive method is BLOCKING. Therefore, we will set a timeout so that we are not stuck
						// waiting forever.
						clientSocket.setSoTimeout(1000);
						clientSocket.receive(ackPacketReceived);
					} catch (SocketTimeoutException t) {
						// If we get a timeout, we know we have not received the ack, so we should try sending it again.
						System.out.println("TIMEOUT");
						continue;
					}
					
					// If we don't timeout, but somehow end up receiving a packet number other than the one
					// we were expecting, we must continue trying to send the current packet.
					int integerAckReceived = ackReceived[0];
					if(integerAckReceived != nextExpectedAck) {
						continue;
					}
					
					nextExpectedAck++;
					System.out.println("nextExpectedAck after good: " + nextExpectedAck);
					isReadyForNextChunk = true;
					System.out.println("isReadyForNextChunk: " + isReadyForNextChunk);

					String receivedAck = new String(ackPacketReceived.getData(), 0, ackPacketReceived.getLength());
					System.out.println("ACK from server: " + integerAckReceived);
					System.out.println();

				}
			}
		}
	}
}
