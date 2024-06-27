import java.io.*;
import java.net.*;

public class MusicGuruClient {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Missing value: java MusicGuruClient <hostname> <port> <year>");
            return; // end program
        }

        String hostname = args[0];
        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("port number must be an integer");
            return; // end program
        }

        int year;
        try {
            year = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("year must be an integer");
            return; // end program
        }

        MusicGuruClient.run(hostname, port, year);

    }


    public static void run(String hostname, int port, int year) {
        int minYear = 0;
        int maxYear = 0;

        try {
            Socket socket = new Socket(hostname, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            //System.out.println("Year range:" + in.readLine());
            String yearRange = in.readLine();
            String[] rangeParts = yearRange.split("-");
            if (rangeParts.length == 2) {
                minYear = Integer.parseInt(rangeParts[0]);
                maxYear = Integer.parseInt(rangeParts[1]);
            }
            System.out.println("Year Range: " +minYear + "-" + maxYear);

            // sent year to server
            out.println(year);

            if((year < minYear)||(year > maxYear)){
                System.out.println("input year is not in the database, will random year... ");
            }

            // receive response from server
            String response = in.readLine();
            System.out.println("server response: " + response);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
