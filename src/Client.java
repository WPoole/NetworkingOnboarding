import java.io.*;
import java.net.*;

public class Client {
	
	public static void main(String[] args) throws IOException {

		// Set host name and port number that we are connecting to.
		String hostName = "localhost";
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
		String fromServer, fromUser;
		
//		System.out.println("Enter one of the following three commands: ");
//		System.out.println("1. /name <name> - This sets the name of this client on the server.");
//		System.out.println("2. /users - Gets the list of clients connected to the server.");
//		System.out.println("3. /msg <name> <msg> - Sends a message to another client given by the name.");
//		
//		while(true) {
//			fromUser = stdIn.readLine();
//			if (fromUser != null) {
//				System.out.println("Client: " + fromUser);
//				out.println(fromUser);
//			}
//		}

		while((fromServer = in.readLine()) != null) {
			System.out.println("Server: " + fromServer);
			if (fromServer.equals("Bye."))
				break;

			fromUser = stdIn.readLine();
			if (fromUser != null) {
				System.out.println("Client: " + fromUser);
				out.println(fromUser);
			}
		}
		
		out.close();
		in.close();
		serverSocket.close();
		stdIn.close();
	}
}
