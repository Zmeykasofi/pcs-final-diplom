import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final int PORT = 8989;
    private static final String HOST = "127.0.0.1";

    public static void main(String[] args) {
        Gson gson = new Gson();

        while (true) {
            try (Socket socket = new Socket(HOST, PORT);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                var serverSays = in.readLine();
                System.out.println(serverSays);
                var requestToServer = reader.readLine();
                out.println(requestToServer);
                String jsonFromServer = in.readLine();
                System.out.println(gson.toJson(jsonFromServer));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
