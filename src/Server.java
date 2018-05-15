import java.io.*;
import java.net.*;

public class Server {

	public static void main(String[] args) throws IOException {

		// Set up socket.
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(9999);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 9999.");
			System.exit(1);
		}

		// Get socket from client and accept connection.
		Socket clientSocket = null;
		try {
			clientSocket = serverSocket.accept();
		} catch (IOException e) {
			System.err.println("Accept failed.");
			System.exit(1);
		}

		// Set up writer (for writing to client) and reader(for reading from client).
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String inputLine, outputLine;

		KnockKnockProtocol kkp = new KnockKnockProtocol();

		outputLine = kkp.processInput(null);
		out.println(outputLine);

		// Read in line from client. If it's not null, we good to go.
		while((inputLine = in.readLine()) != null) {
			outputLine = kkp.processInput(inputLine);
			out.println(outputLine);
			if(outputLine.equals("Bye.")) {
				break;
			}
		}
		
		out.close();
		in.close();
		clientSocket.close();
		serverSocket.close();
	}
}

