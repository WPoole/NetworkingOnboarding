import java.io.*;
import java.net.*;
import java.util.Hashtable;
import java.util.Set;

public class ServerThread extends Thread {

	// Fields.
	private Socket clientSocket = null;
	private Hashtable table = null;

	// Constructor.
	ServerThread(Socket clientSocket, Hashtable table) {
		this.clientSocket = clientSocket;
		this.table = table;
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
		
		out.println("The client's IP address is:" + clientSocket.getRemoteSocketAddress().toString());
		
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			// Do nothing for now, may need to change this later...
		}
		

		while(true) {
			String inputLine = null;
			try {
				inputLine = in.readLine();
			} catch (IOException e) {
				// Do nothing for now, may need to change this later...
			}
			
			if(inputLine != null) {
				if(inputLine.contains("/name")) {
					String[] array = inputLine.split(" ");
					if(table.containsValue(array[1])) {
						out.println("Name, " + array[1] + ", is already taken. Please choose another.");
						continue;
					} else if(table.containsKey(clientSocket)) {
						out.println("You have already been assigned a name. It is: " + table.get(clientSocket));
					} else {
						table.put(clientSocket, array[1]);
					}
				} else if(inputLine.contains("/users")) {
					if (table.isEmpty()) {
						out.println("There are no active users.");
						continue;
					}
					for(Object user : table.values()) {
						String username = user.toString();
						out.println(username);
					}
				} else if(inputLine.contains("/msg")){
					String[] array = inputLine.split(" ");
					if(!table.containsValue(array[1])) {
						out.println("The user, " + array[1] + ", could not be found.");
					} else {
						for(Object socket : table.keySet()) {
							// If we have found the socket corresponding to the name to which the message is being sent...
							if(array[1].equals(table.get(socket))) {
								PrintWriter tempWriter = null;
								try {
									tempWriter = new PrintWriter(((Socket) socket).getOutputStream(), true);
								} catch (IOException e) {
									// Do nothing for now, may need to change this later...
								}
								
								tempWriter.println(array[2]);
							}
						}
					}
				} else {
					out.println("Please enter a valid command.");
				}
			}
		}
	}
}
