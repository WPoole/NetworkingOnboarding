import java.io.*;
import java.net.*;

public class UserDataReceivingThread extends Thread {
	
	// Fields.
	private BufferedReader in = null;
	
	// Constructor.
	UserDataReceivingThread(BufferedReader in) {
		this.in = in;
	}
	
	// Methods.
	public void run() {
		while(true) {
			String fromServer = null;
			try {
				fromServer = in.readLine();
			} catch (IOException e) {
				// Do nothing for now, may need to change this later...
			}
			
			System.out.println("From Server: " + fromServer);
		}
	}
}
