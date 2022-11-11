import com.google.gson.Gson;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {
    private static final int PORT = 8989;

    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        Gson gson = new Gson();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер стартовал.");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                    System.out.println("Новое соединение установлено с порта " + PORT);
                    out.println("Введите слово для поиска (например \"бизнес\"):");
                    String word = in.readLine();
                    List<PageEntry> searchResult = engine.search(word);
                    out.println(gson.toJson(searchResult));

                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        } catch (IOException exception) {
            System.out.println("Не могу стартовать сервер");
            exception.printStackTrace();
        }
    }
}