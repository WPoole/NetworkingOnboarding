import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class EchoServer extends Thread {

	private DatagramSocket serverSocket;
	private boolean running;

	public EchoServer() throws SocketException {
		serverSocket = new DatagramSocket(4445);
	}

	public void run() {
		running = true;

		while(running) {
			byte[] bytesReceived = new byte[256];
			
			DatagramPacket packetReceived = new DatagramPacket(bytesReceived, bytesReceived.length);
			try {
				serverSocket.receive(packetReceived);
			} catch (IOException e) {
				// Do nothing for now.
			}
			
			// Get IP address of client who sent packet.
			InetAddress clientAddress = packetReceived.getAddress();
			System.out.println("Address of sending machine is: " + clientAddress);
			// Get port number of client process that sent packet.
			int clientPort = packetReceived.getPort();
			System.out.println("Port number of client process that sent packet: " + clientPort);
			
//			packetReceived = new DatagramPacket(bytesReceived, bytesReceived.length, clientAddress, clientPort);
			String received = new String(packetReceived.getData(), 0, packetReceived.getLength());
			System.out.println("From Client: " + received);
			
			if (received.equals("end")) {
				running = false;
				continue;
			}
		}
		serverSocket.close();
	}
}