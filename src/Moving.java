import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Moving {
    static final double EPS = 1e-9;
    public static final double TWO_SECONDS = 10.0;
    public static final double COIN_GROWTH_SECONDS = 5.0;

    double x;
    double y;
    double vx;
    double vy;
    double ax;
    double ay;
    double maxSpeed;
    List<Anomaly> anomalies;

    public Moving(double x, double y, double vx, double vy, double ax, double ay, double maxSpeed, List<Anomaly> anomalies) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.ax = ax;
        this.ay = ay;
        this.maxSpeed = maxSpeed;
        this.anomalies = anomalies;
    }

    static Vector predictAnomalyAcceleration(double x, double y, double seconds, List<Anomaly> anomalies) {
        double ax = 0;
        double ay = 0;
        for (Anomaly anomaly : anomalies) {
            double nx = anomaly.getX() + anomaly.getVelocity().getX() * seconds;
            double ny = anomaly.getY() + anomaly.getVelocity().getY() * seconds;
            double dx = nx - x;
            double dy = ny - y;
            double dist = Math.hypot(dx, dy);
            if (dist > 1e-3 && dist <= anomaly.getEffectiveRadius()) {
                double normx = dx / dist;
                double normy = dy / dist;
                double power = Math.signum(anomaly.getStrength()) * anomaly.getStrength() * anomaly.getStrength() / dist / dist;
                ax += normx * power;
                ay += normy * power;
            }
        }
        return new Vector(ax, ay);
    }

    void move(double seconds, double tickSize) {
        for (double tm = 0.0; tm <= seconds + EPS; tm += tickSize) {
            Vector aa = predictAnomalyAcceleration(x, y, tm, anomalies);
            double nvx = vx + (ax + aa.getX()) * tickSize;
            double nvy = vy + (ay + aa.getY()) * tickSize;
            double speed = Math.hypot(nvx, nvy);
            if (speed > maxSpeed) {
                nvx = nvx * maxSpeed / speed;
                nvy = nvy * maxSpeed / speed;
            }
            x += (vx + nvx) / 2 * tickSize;
            y += (vy + nvy) / 2 * tickSize;
            vx = nvx;
            vy = nvy;
        }
    }

    double evaluateMove(GameState gameState, Transport transport, double seconds, double tickSizeFirstTwoSec, double tickSizeAfterTwoSec) {
        double score = 0;
        List<Bounty> closeBounties = new ArrayList<>();
        for (Bounty bounty : gameState.getBounties()) {
            double dist = Math.hypot(x - bounty.getX(), y - bounty.getY());
            if (dist < 500) {
                closeBounties.add(bounty);
            }
        }

        List<Enemy> closeEnemies = new ArrayList<>();
        for (Enemy enemy : gameState.getEnemies()) {
            double dist = Math.hypot(x - enemy.getX(), y - enemy.getY());
            if (dist < 500) {
                closeEnemies.add(enemy);
            }
        }

        double tickSize = tickSizeFirstTwoSec;
        for (double tm = 0.0; tm <= seconds + EPS; tm += tickSize) {
            if (tm >= TWO_SECONDS) tickSize = tickSizeAfterTwoSec;
            Vector aa = predictAnomalyAcceleration(x, y, tm, anomalies);
            double nvx = vx + (ax + aa.getX()) * tickSize;
            double nvy = vy + (ay + aa.getY()) * tickSize;
            double speed = Math.hypot(nvx, nvy);
            if (speed > maxSpeed) {
                nvx = nvx * maxSpeed / speed;
                nvy = nvy * maxSpeed / speed;
            }
            x += (vx + nvx) / 2 * tickSize;
            y += (vy + nvy) / 2 * tickSize;
            vx = nvx;
            vy = nvy;

            double edge = 50;
            if (x < edge || x > gameState.getMapSize().getX() - edge || y < edge || y > gameState.getMapSize().getY() - edge) {
                return -1e20;
            }

            List<Bounty> toRem = new LinkedList<>();
            for (Bounty bounty : closeBounties) {
                double dist = Math.hypot(x - bounty.getX(), y - bounty.getY());
                double effectiveRadius = tm <= COIN_GROWTH_SECONDS ? bounty.getRadius() * 0.5 : bounty.getRadius() * 2.5;
                if (dist < effectiveRadius) {
                    double timeCoeff = 1.0 / Math.max(tickSize, tm);
                    score += bounty.getPoints() * timeCoeff;
                    toRem.add(bounty);
                }
            }
            for (Anomaly anomaly : anomalies) {
                double nx = anomaly.getX() + anomaly.getVelocity().getX() * tm;
                double ny = anomaly.getY() + anomaly.getVelocity().getY() * tm;
                double dist = Math.hypot(nx - x, ny - y);
                if (dist < anomaly.getRadius() * 1.5 + 5) {
                    return -1e20;
                }
            }
            for (Enemy enemy : closeEnemies) {
                double nx = enemy.getX() + enemy.getVelocity().getX() * tm;
                double ny = enemy.getY() + enemy.getVelocity().getY() * tm;
                double dist = Math.hypot(nx - x, ny - y);
                if (dist < 17) {
                    return -1e30;
                }
                if (dist < 34) {
                    return -1e20;
                }
            }
            for (Bounty bounty : toRem) {
                closeBounties.remove(bounty);
            }
        }
        return score;
    }

    public List<Vector> buildTrajectory(double seconds, double tickSizeFirstTwoSec, double tickSizeAfterTwoSec) {
        List<Vector> trajectory = new ArrayList<>();
        double tickSize = tickSizeFirstTwoSec;
        for (double tm = 0.0; tm <= seconds + EPS; tm += tickSize) {
            if (tm >= TWO_SECONDS) tickSize = tickSizeAfterTwoSec;
            Vector aa = predictAnomalyAcceleration(x, y, tm, anomalies);
            double nvx = vx + (ax + aa.getX()) * tickSize;
            double nvy = vy + (ay + aa.getY()) * tickSize;
            double speed = Math.hypot(nvx, nvy);
            if (speed > maxSpeed) {
                nvx = nvx * maxSpeed / speed;
                nvy = nvy * maxSpeed / speed;
            }
            x += (vx + nvx) / 2 * tickSize;
            y += (vy + nvy) / 2 * tickSize;
            vx = nvx;
            vy = nvy;
            trajectory.add(new Vector(x, y));
        }
        return trajectory;
    }
}
