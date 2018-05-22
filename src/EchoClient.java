import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class EchoClient {
	private DatagramSocket clientSocket;
	private InetAddress serverIP;
	private int serverPort = 4445;

	public EchoClient() throws SocketException, UnknownHostException {
		clientSocket = new DatagramSocket();
		serverIP = InetAddress.getByName("localhost");
		
	}

	public void sendEcho(String msg) throws IOException {
		byte[] bytesToSend = msg.getBytes();
		// NOTE: The server's IP and port number are specified here when we create the packet to send.
		DatagramPacket packetToSend = new DatagramPacket(bytesToSend, bytesToSend.length, serverIP, serverPort);
		clientSocket.send(packetToSend);
	}

	public void close() {
		clientSocket.close();
	}
}