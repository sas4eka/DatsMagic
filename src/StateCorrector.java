import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StateCorrector {

    private static final double TICK_SIZE = 0.001;

    static GameState correct(GameState state, long millis) {
        double seconds = millis / 1000.0;
        double maxSpeed = state.getMaxSpeed();
        GameState correctedState = new GameState(state);
        List<Anomaly> anomalies = state.getAnomalies();
        correctedState.setAnomalies(correctAnomalies(anomalies, maxSpeed, seconds));
        correctedState.setTransports(correctTransports(state.getTransports(), maxSpeed, seconds, anomalies));
        correctedState.setEnemies(correctEnemies(state.getEnemies(), maxSpeed, seconds, anomalies));
        return correctedState;
    }

    private static Anomaly correctAnomaly(Anomaly anomaly, double maxSpeed, double seconds) {
        Moving moving = new Moving(
                anomaly.getX(),
                anomaly.getY(),
                anomaly.getVelocity().getX(),
                anomaly.getVelocity().getY(),
                0,
                0,
                maxSpeed,
                Collections.emptyList()
        );
        moving.move(seconds, TICK_SIZE);
        Anomaly corrected = new Anomaly(anomaly);
        corrected.setVelocity(new Vector(moving.vx, moving.vy));
        corrected.setX((int) moving.x);
        corrected.setY((int) moving.y);
        return corrected;
    }

    private static List<Anomaly> correctAnomalies(List<Anomaly> anomalies, double maxSpeed, double seconds) {
        return anomalies.stream().map(anomaly -> correctAnomaly(anomaly, maxSpeed, seconds)).collect(Collectors.toList());
    }

    private static Transport correctTransport(Transport transport, double maxSpeed, double seconds, List<Anomaly> anomalies) {
        Moving moving = new Moving(
                transport.getX(),
                transport.getY(),
                transport.getVelocity().getX(),
                transport.getVelocity().getY(),
                transport.getSelfAcceleration().getX(),
                transport.getSelfAcceleration().getY(),
                maxSpeed,
                anomalies
        );
        moving.move(seconds, TICK_SIZE);
        Transport corrected = new Transport(transport);
        corrected.setVelocity(new Vector(moving.vx, moving.vy));
        corrected.setX((int) moving.x);
        corrected.setY((int) moving.y);
        corrected.setAnomalyAcceleration(Moving.predictAnomalyAcceleration(moving.x, moving.y, seconds, anomalies));
        return corrected;
    }

    private static List<Transport> correctTransports(List<Transport> transports, double maxSpeed, double seconds, List<Anomaly> anomalies) {
        return transports.stream().map(transport -> correctTransport(transport, maxSpeed, seconds, anomalies)).collect(Collectors.toList());
    }


    private static Enemy correctEnemy(Enemy enemy, double maxSpeed, double seconds, List<Anomaly> anomalies) {
        Moving moving = new Moving(
                enemy.getX(),
                enemy.getY(),
                enemy.getVelocity().getX(),
                enemy.getVelocity().getY(),
                0,
                0,
                maxSpeed,
                anomalies
        );
        moving.move(seconds, TICK_SIZE);
        Enemy corrected = new Enemy(enemy);
        corrected.setVelocity(new Vector(moving.vx, moving.vy));
        corrected.setX((int) moving.x);
        corrected.setY((int) moving.y);
        return corrected;
    }

    private static List<Enemy> correctEnemies(List<Enemy> enemies, double maxSpeed, double seconds, List<Anomaly> anomalies) {
        return enemies.stream().map(enemy -> correctEnemy(enemy, maxSpeed, seconds, anomalies)).collect(Collectors.toList());
    }

}
