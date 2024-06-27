import java.io.IOException;
import java.net.Socket;
import java.net.InetAddress;

public class HealthCheckClient {
    public static void main(String[] args) {
        String serverIP = "192.168.1.72"; // IP address
        int serverPort = 5001; // health check port

        try {
            Socket socket = new Socket(serverIP, serverPort);
            System.out.println("Connected to the Health Check Server.");
            socket.close();
        } catch (IOException e) {
            System.out.println("Unable to connect to the Health Check Server.");
            e.printStackTrace();
        }
    }
}
