import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestManager {

    static HttpClient client = HttpClient.newBuilder().build();

    static String TOKEN = "TOKEN";
    static String STATUS_URL = "https://games-test.datsteam.dev/rounds/magcarp/";
    static String GAME_URL = "https://games-test.datsteam.dev/play/magcarp/player/move";

    static String sendRequest(String body) throws IOException, URISyntaxException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .setHeader("Content-Type", "application/json")
                .uri(new URI(GAME_URL))
                .headers(
                        "Content-Type", "application/json",
                        "X-Auth-Token", TOKEN
                )
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        return response.body();
    }

    static void getStatus() throws IOException, URISyntaxException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .setHeader("Content-Type", "application/json")
                .uri(new URI(STATUS_URL))
                .headers("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        System.out.println(response.body());
    }
}
