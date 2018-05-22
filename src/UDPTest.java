import java.io.IOException;

public class UDPTest {
	public static void main(String[] args) throws IOException {
		EchoClient client;

		new EchoServer().start();
		client = new EchoClient();

		client.sendEcho("hello server");
		client.sendEcho("ser");

		client.sendEcho("end");
		client.close();
	}
}