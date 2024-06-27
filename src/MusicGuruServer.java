import java.io.*;
import java.net.*;
import java.util.*;

public class MusicGuruServer {

    private static final int NUM_SONGS_PER_YEAR = 10;

    private static Map<Integer, List<String>> musicDatabase = new LinkedHashMap<>();

    public static void main(String[] args) {
        initializeMusicDatabase();

        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Music Guru server is listening on port 5000....");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("The client connection is successful!！");

                // Handle client requests
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeMusicDatabase() {
        // load music data file from external file
        loadMusicDataFromFile();
    }

    private static void loadMusicDataFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("music_database.txt"))) {
            String line;
            int currentYear = 0;
            List<String> songs = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                if (line.matches("\\d{4}:")) {
                    //
                    if (currentYear != 0) {
                        musicDatabase.put(currentYear, songs);
                        songs = new ArrayList<>();
                    }
                    currentYear = Integer.parseInt(line.substring(0, 4));
                } else if (line.matches("\\d+\\. .*")) {
                    //
                    songs.add(line.substring(line.indexOf(" ") + 1));
                }
            }

            // add songs from last year
            if (currentYear != 0) {
                musicDatabase.put(currentYear, songs);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            int MIN_YEAR;
            int MAX_YEAR;
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                // year range
                Integer[] yearsArray = musicDatabase.keySet().toArray(new Integer[0]);
                MIN_YEAR = yearsArray[0];
                MAX_YEAR = yearsArray[yearsArray.length - 1];
                out.println(MIN_YEAR + "-" + MAX_YEAR);

                // receive client request
                String request = in.readLine();
                System.out.println("The client request: " + request);

                // get the selected year from the request analysis.
                int selectedYear = Integer.parseInt(request);

                // Get the server's IP address
                String serverIPAddress = InetAddress.getLocalHost().getHostAddress();
                
                if (selectedYear >=  MIN_YEAR && selectedYear <= MAX_YEAR) {
                    List<String> songs = musicDatabase.get(selectedYear);
                    int randomIndex = new Random().nextInt(NUM_SONGS_PER_YEAR);
                    String selectedSong = songs.get(randomIndex);
                    out.println("Recommend songs from " + selectedYear + " : " + selectedSong
                            + " (Server IP: " + serverIPAddress + ")");

                } else {
                    int randomYear = MIN_YEAR + new Random().nextInt(MAX_YEAR - MIN_YEAR + 1);
                    List<String> songs = musicDatabase.get(randomYear);
                    int randomIndex = new Random().nextInt(NUM_SONGS_PER_YEAR);
                    String selectedSong = songs.get(randomIndex);
                    out.println("Specified year out of range (" + MIN_YEAR + "-" + MAX_YEAR
                            + "), using random data instead: "+randomYear
                            + " The song is: " + selectedSong
                    +" (Server IP: " + serverIPAddress + ")");
                }

                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
