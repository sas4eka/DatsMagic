import com.fasterxml.jackson.databind.ObjectMapper;

import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    static final boolean SHOW_VISUALIZATION = true;
    static final boolean PLAY_SOUND = true;
    static final boolean LOAD_FROM_FILE = true;
    static final boolean SEND_REQUESTS = false;
    static final boolean DEBUG = false;
    static final int LOGS_LIMIT = 100;

    static Visualizer v;
    static ObjectMapper MAPPER = new ObjectMapper();
    static List<String> logs = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException, IOException, LineUnavailableException, UnsupportedAudioFileException {
        if (SHOW_VISUALIZATION) {
            v = new Visualizer();
            if (LOAD_FROM_FILE) {
                List<GameState> gameStates = readGameStates("response_pack.txt");
                List<GameFrame> frames = new ArrayList<>();
                for (GameState gameState : gameStates) {
                    GameState correctedState = StateCorrector.correct(gameState, 0);
                    v.setTitle(gameState.getName());
                    Solution solution = BruteForce.solve(correctedState);
                    PlayerAction action = solution.getPlayerAction();
                    GameFrame frame = getGameFrame(correctedState, action, solution.getAnalysisData());
                    frames.add(frame);
                }
                v.setFrames(frames);
            }
            if (PLAY_SOUND) {
                File soundFile = new File("piano.wav");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
            }
        }
        if (SEND_REQUESTS) {
            String chosenAction = "";
            while (true) {
                try {
                    long requestTime = System.currentTimeMillis();
                    String response = RequestManager.sendRequest(chosenAction);
                    long responseTime = System.currentTimeMillis();
                    processResponse(response);
                    GameState state = MAPPER.readValue(response, GameState.class);
                    if (state.getError() != null) {
                        Thread.sleep(5000);
                        continue;
                    }
                    state = StateCorrector.correct(state, 350 + 25);
                    Solution solution = BruteForce.solve(state);
                    PlayerAction action = solution.getPlayerAction();
                    chosenAction = MAPPER.writeValueAsString(action);
                    // System.out.println(chosenAction);
                    if (SHOW_VISUALIZATION) {
                        v.setFrames(List.of(getGameFrame(state, action, solution.getAnalysisData())));
                    }

                    long calculation = System.currentTimeMillis() - responseTime;
                    long latency = responseTime - requestTime;
                    long elapsedTime = calculation + latency;
                    System.out.println("Time: " + elapsedTime + " ms. Latency: " + latency + " ms, calc: " + calculation + " ms");
                    long remainingTime = Math.max(1, 350 - elapsedTime);
                    Thread.sleep(remainingTime);
                } catch (Exception e) {
                    System.out.println("FAILED " + e);
                    if (!e.toString().contains("GOAWAY")) {
                        Thread.sleep(5000);
                    }
                }
            }
        }
        Runtime.getRuntime().addShutdownHook(new Thread(Main::flushLogs));
    }

    static void processResponse(String response) {
        // System.out.println(response);
        logs.add(response);
        if (logs.size() == LOGS_LIMIT) {
            flushLogs();
        }
    }

    private static void flushLogs() {
        if (logs.isEmpty()) return;
        long tm = System.currentTimeMillis();
        try {
            PrintWriter writer = new PrintWriter("logs/log" + tm + ".txt");
            for (String log : logs) {
                writer.println(log);
            }
            writer.close();
            logs.clear();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<GameState> readGameStates(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        List<GameState> gameStates = new ArrayList<>();
        while (reader.ready()) {
            String line = reader.readLine();
            if (!line.isEmpty()) {
                gameStates.add(MAPPER.readValue(line, GameState.class));
            }
        }
        reader.close();
        return gameStates;
    }

    static private GameFrame getGameFrame(GameState gameState, PlayerAction action, AnalysisData analysisData) {
        long start = System.currentTimeMillis();
        int shipArea = 900;
        int minimapScale = (gameState.getMapSize().getX() + 1799) / 1800;
        BufferedImage minimap = MinimapRenderer.drawMinimap(gameState, action, shipArea, minimapScale);
        List<BufferedImage> ships = new ArrayList<>();
        int index = 0;
        for (Transport transport : gameState.getTransports()) {
            index++;
            List<Vector> trajectory = analysisData.trajectories.getOrDefault(transport.getId(), Collections.emptyList());
            ships.add(ShipRenderer.drawShip(index, transport, gameState, action, trajectory, shipArea, 0.5));
        }
        if (DEBUG) {
            System.out.println("Rendered in " + (System.currentTimeMillis() - start));
        }
        return new GameFrame(minimap, ships);
    }
}
