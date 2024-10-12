import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class BruteForce {

    public static final double SIM_TIME = 40.0;
    public static final double SIM_TICK = 0.02;
    public static final double SIM_TICK_AFTER_TWO_SEC = 0.1;

    static ExecutorService SERVICE = Executors.newFixedThreadPool(5);

    public static Solution solve(GameState gameState) {
        List<TransportAction> transportActions = new ArrayList<>();
        List<Transport> transports = gameState.getTransports();
        Map<String, List<Vector>> trajectories = new HashMap<>();

        List<Future<Result>> futures = new ArrayList<>();

        for (Transport transport : transports) {
            Future<Result> submit = SERVICE.submit(() -> findBestActions(gameState, transport));
            futures.add(submit);
        }

        for (var f : futures) {
            try {
                Result result = f.get();
                transportActions.add(result.action);
                trajectories.put(result.transportId, result.trajectory);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        PlayerAction playerAction = new PlayerAction();
        playerAction.setTransports(transportActions);

        return new Solution(playerAction, new AnalysisData(trajectories));
    }

    private static Result findBestActions(GameState gameState, Transport transport) {
        Vector acceleration = findBestAcceleration(gameState, transport);
        List<Vector> trajectory = generateTrajectory(gameState, transport, acceleration);

        TransportAction transportAction = new TransportAction();
        transportAction.setAcceleration(acceleration);
        transportAction.setAttack(findAttack(transport, gameState));
        transportAction.setActivateShield(shouldActivateShield(transport, gameState));  // Assuming shield is off by default
        transportAction.setId(transport.getId());  // Use the transport's ID
        return new Result(transport.getId(), transportAction, trajectory);
    }

    static class Result {
        String transportId;
        TransportAction action;
        List<Vector> trajectory;

        public Result(String transportId, TransportAction action, List<Vector> trajectory) {
            this.transportId = transportId;
            this.action = action;
            this.trajectory = trajectory;
        }
    }

    private static IntVector findAttack(Transport transport, GameState gameState) {
        if (transport.getAttackCooldownMs() > 0) {
            return null;
        }

        List<Enemy> shootable = new ArrayList<>();
        for (Enemy enemy : gameState.getEnemies()) {
            double dist = Math.hypot(transport.getX() - enemy.getX(), transport.getY() - enemy.getY());
            if (dist < gameState.getAttackRange() && dist > gameState.getAttackExplosionRadius() + 5) {
                if (enemy.getShieldLeftMs() == 0) {
                    shootable.add(enemy);
                }
            }
        }
        if (shootable.isEmpty()) {
            return null;
        }
        shootable.sort((a, b) -> {
            int ahp = a.getHealth() <= gameState.getAttackDamage() ? 0 : 1;
            int bhp = b.getHealth() <= gameState.getAttackDamage() ? 0 : 1;
            if (ahp != bhp) {
                return Integer.compare(ahp, bhp);
            }
            return Integer.compare(b.getKillBounty(), a.getKillBounty());
        });
        Enemy target = shootable.get(0);
        return new IntVector(target.getX(), target.getY());
    }

    private static boolean shouldActivateShield(Transport transport, GameState gameState) {
        if (transport.getShieldCooldownMs() > 0) {
            return false;
        }
        for (Enemy enemy : gameState.getEnemies()) {
            double dist = Math.hypot(transport.getX() - enemy.getX(), transport.getY() - enemy.getY());
            if (dist < gameState.getAttackRange() + gameState.getAttackExplosionRadius()) {
                return true;
            }
        }
        return false;
    }

    private static List<Vector> generateTrajectory(GameState gameState, Transport transport, Vector acceleration) {
        Moving moving = new Moving(
                transport.getX(),
                transport.getY(),
                transport.getVelocity().getX(),
                transport.getVelocity().getY(),
                acceleration.getX(),
                acceleration.getY(),
                gameState.getMaxSpeed(),
                gameState.getAnomalies()
        );
        return moving.buildTrajectory(SIM_TIME, SIM_TICK, SIM_TICK_AFTER_TWO_SEC);
    }

    private static Vector findBestAcceleration(GameState gameState, Transport transport) {
        int angleSteps = 200;
        double accelCap = 0.99 * gameState.getMaxAccel();
        double bestScore = -1e10;
        Vector bestAccel = new Vector(0, 0);
        for (int angleStep = 0; angleStep < angleSteps; angleStep++) {
            double angle = 2 * Math.PI * angleStep / angleSteps;
            double ax = Math.cos(angle) * accelCap;
            double ay = Math.sin(angle) * accelCap;
            double score = getAngleScore(ax, ay, gameState, transport);
            if (score > bestScore) {
                bestScore = score;
                bestAccel = new Vector(ax, ay);
            }
        }
        return bestAccel;
    }

    private static double getAngleScore(double ax, double ay, GameState gameState, Transport transport) {
        Moving moving = new Moving(
                transport.getX(),
                transport.getY(),
                transport.getVelocity().getX(),
                transport.getVelocity().getY(),
                ax,
                ay,
                gameState.getMaxSpeed(),
                gameState.getAnomalies()
        );
        double moveScore = moving.evaluateMove(gameState, transport, SIM_TIME, SIM_TICK, SIM_TICK_AFTER_TWO_SEC);

        double accelCap = 0.99 * gameState.getMaxAccel();
        double midax = gameState.getMapSize().getX() * 0.5 - transport.getX();
        double miday = gameState.getMapSize().getY() * 0.5 - transport.getY();
        Vector mida = normalize(midax, miday, accelCap);
        return moveScore - 1e-3 * (Math.abs(mida.getX() - ax) + Math.abs(mida.getY() - ay));
    }

    static Vector normalize(double ax, double ay, double norm) {
        double len = Math.hypot(ax, ay);
        if (len > 1e-6) {
            ax = ax * norm / len;
            ay = ay * norm / len;
        }
        return new Vector(ax, ay);
    }
}
