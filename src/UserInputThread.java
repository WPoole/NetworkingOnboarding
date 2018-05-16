import java.io.*;
import java.net.*;

public class UserInputThread extends Thread {

    // Fields.
    private PrintWriter out = null;
    private BufferedReader stdIn = null;

    // Constructor.
    UserInputThread(PrintWriter out, BufferedReader stdIn) {
        this.out = out;
        this.stdIn = stdIn;
    }

    // Methods.
    public void run() {
        while(true) {
            String fromClient = null;
			try {
				fromClient = stdIn.readLine();
			} catch (IOException e) {
				// Do nothing for now, may need to change this later...
			}
			
			if(fromClient != null) {
//				System.out.println("Client: " + fromClient);
				out.println(fromClient);
			}
        }
    }
}