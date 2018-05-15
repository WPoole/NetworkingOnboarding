import java.io.*;
import java.net.*;

public class ServerThread extends Thread {

	// Fields.
	private Socket clientSocket = null;

	// Constructor.
	ServerThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	// Methods.
	public void run() {
		// Set up writer (for writing to client) and reader(for reading from client).
		PrintWriter out = null;
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
		} catch (IOException e) {
			// Do nothing for now, may need to change this later...
		}
		
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			// Do nothing for now, may need to change this later...
		}
		
		String inputLine, outputLine;

		KnockKnockProtocol kkp = new KnockKnockProtocol();

		outputLine = kkp.processInput(null);
		out.println(outputLine);

		// Read in line from client. If it's not null, we good to go.
		try {
			while((inputLine = in.readLine()) != null) {
				outputLine = kkp.processInput(inputLine);
				out.println(outputLine);
				if(outputLine.equals("Bye.")) {
					break;
				}
			}
		} catch (IOException e1) {
			// Do nothing for now, may need to change this later...
		}

		// Close everything.
		out.close();
		try {
			in.close();
		} catch (IOException e) {
			// Do nothing for now, may need to change this later...
		}
		
		try {
			clientSocket.close();
		} catch (IOException e) {
			// Do nothing for now, may need to change this later...
		}
	}
}
