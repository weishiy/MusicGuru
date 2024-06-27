import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HealthCheckServer {
    public static void main(String[] args){
        try {
            int port = 5001;
            ServerSocket healthCheckSocket = new ServerSocket(port);
            System.out.println("Health Check Server is listening on port " + port);

            while (true) {
                Socket clientSocket = healthCheckSocket.accept(); // wait client to connect
                System.out.println("Received a connection from: " + clientSocket.getInetAddress());
                clientSocket.close(); // close connection
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
