import java.io.*;
import java.net.*;
import java.util.Hashtable;

public class Server {

	public static void main(String[] args) throws IOException {
		
		// Table that maps the user ports to their user names.
		// Note: We are using a Hash TABLE instead of a Hash MAP because Hash tables 
		// are thread safe whereas Hash maps are not.
		Hashtable table = new Hashtable();

		// Set up socket.
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(9999);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 9999.");
			System.exit(1);
		}

		while(true) {
			// Get socket from client and accept connection.
			Socket clientSocket = null;
			try {
				// If we manage to accept a connection from a client, we want to start a thread for this connection.
				// We need the threads to be able to have multiple connections at the same time.
				clientSocket = serverSocket.accept();
				ServerThread serverThread = new ServerThread(clientSocket, table);
				serverThread.start();
			} catch (IOException e) {
				// Do nothing, just keep waiting for more connections.
			}
		}
	}
}

