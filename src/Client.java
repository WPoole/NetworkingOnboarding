import java.io.*;
import java.net.*;

public class Client {

	public static void main(String[] args) throws IOException {

		// Set host name (IP address) and port number that we are connecting to.
		// NOTE: Got this IP address by typing "ifconfig" into terminal. 
		// This is the LOCAL IP address of this computer, i.e. on the current LAN. 
		// Saying it is local just means it is not public, i.e. that it cannot be accessed from outside the current LAN.
		String hostName = "192.168.3.163"; 
		int portNumber = 9999;

		// Set up server socket connection.
		Socket serverSocket = null;
		try {
			serverSocket = new Socket(hostName, portNumber);
		} catch (IOException e){
			System.err.println("Could not connect to host.");
			System.exit(1);
		}

		//  Set up writer, reader, and standard input.
		PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

		// Display list of commands to the user.
		System.out.println("Enter one of the following three commands: ");
		System.out.println("1. /name <name> - This sets the name of this client on the server.");
		System.out.println("2. /users - Gets the list of clients connected to the server.");
		System.out.println("3. /msg <name> <msg> - Sends a message to another client given by the name.");

		// Start user input thread.
		UserInputThread userInputThread = new UserInputThread(out, stdIn);
		userInputThread.start();
		// Start data-receiving thread.
		UserDataReceivingThread userDataReceivingThread = new UserDataReceivingThread(in);
		userDataReceivingThread.start();


//		while((fromServer = in.readLine()) != null) {
//			System.out.println("Server: " + fromServer);
//			if (fromServer.equals("Bye."))
//				break;
//
//			fromUser = stdIn.readLine();
//			if (fromUser != null) {
//				System.out.println("Client: " + fromUser);
//				out.println(fromUser);
//			}
//		}
	}
}
