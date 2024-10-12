import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShipRenderer {
    static BufferedImage drawShip(int index, Transport me, GameState gameState, PlayerAction action, List<Vector> trajectory, int shipArea, double scaleFactor) {
        int size = shipArea;
        int offsetX = size / 2 - me.getX();
        int offsetY = size / 2 - me.getY();

        int scaledWidth = (int) (size / scaleFactor);
        int scaledHeight = (int) (size / scaleFactor);
        BufferedImage mapImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = mapImage.createGraphics();

        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 0, scaledWidth, scaledHeight);

        g2d.setColor(Color.GRAY);
        Font currentFont = g2d.getFont();
        Font newFont = currentFont.deriveFont(216.0F);
        g2d.setFont(newFont);

        g2d.drawString("" + index, scaledWidth - 140, 180);

        // Draw anomalies
        g2d.setColor(Color.RED);
        for (Anomaly anomaly : gameState.getAnomalies()) {
            int scaledX = (int) ((anomaly.getX() + offsetX) / scaleFactor);
            int scaledY = (int) ((anomaly.getY() + offsetY) / scaleFactor);
            int scaledRadius = (int) (anomaly.getRadius() / scaleFactor);
            g2d.fillOval(scaledX - scaledRadius, scaledY - scaledRadius, scaledRadius * 2, scaledRadius * 2);
        }

        // Draw anomaly effective radius
        g2d.setColor(Color.GRAY);
        for (Anomaly anomaly : gameState.getAnomalies()) {
            int scaledX = (int) ((anomaly.getX() + offsetX) / scaleFactor);
            int scaledY = (int) ((anomaly.getY() + offsetY) / scaleFactor);
            int effectiveRadius = (int) (anomaly.getEffectiveRadius() / scaleFactor);
            g2d.drawOval(scaledX - effectiveRadius, scaledY - effectiveRadius, effectiveRadius * 2, effectiveRadius * 2);
        }

        // Draw enemies
        g2d.setColor(Color.BLUE);
        for (Enemy enemy : gameState.getEnemies()) {
            int scaledX = (int) ((enemy.getX() + offsetX) / scaleFactor);
            int scaledY = (int) ((enemy.getY() + offsetY) / scaleFactor);
            int enemySize = (int) (5 / scaleFactor);
            g2d.fillOval(scaledX - enemySize, scaledY - enemySize, enemySize * 2, enemySize * 2);
        }

        Map<String, Transport> transports = new HashMap<>();
        for (Transport transport : gameState.getTransports()) {
            transports.put(transport.getId(), transport);
        }

        // Draw velocity and acceleration
        for (TransportAction ta : action.getTransports()) {
            Transport transport = transports.get(ta.getId());
            int scaledX = (int) ((transport.getX() + offsetX) / scaleFactor);
            int scaledY = (int) ((transport.getY() + offsetY) / scaleFactor);

            Stroke oldStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(5));

            g2d.setColor(Color.BLUE);
            int selfX = (int) (11 * transport.getSelfAcceleration().getX() / scaleFactor);
            int selfY = (int) (11 * transport.getSelfAcceleration().getY() / scaleFactor);
            g2d.drawLine(scaledX, scaledY, scaledX + selfX, scaledY + selfY);

            g2d.setColor(Color.RED);
            int anomalyX = (int) (11 * transport.getAnomalyAcceleration().getX() / scaleFactor);
            int anomalyY = (int) (11 * transport.getAnomalyAcceleration().getY() / scaleFactor);
            g2d.drawLine(scaledX, scaledY, scaledX + anomalyX, scaledY + anomalyY);

            g2d.setColor(Color.WHITE);
            int velocityX = (int) (transport.getVelocity().getX() / scaleFactor);
            int velocityY = (int) (transport.getVelocity().getY() / scaleFactor);
            g2d.drawLine(scaledX, scaledY, scaledX + velocityX, scaledY + velocityY);

            g2d.setColor(Color.YELLOW);
            int accelerationX = (int) (11 * ta.getAcceleration().getX() / scaleFactor);
            int accelerationY = (int) (11 * ta.getAcceleration().getY() / scaleFactor);
            g2d.drawLine(scaledX, scaledY, scaledX + accelerationX, scaledY + accelerationY);

            g2d.setStroke(oldStroke);
        }

        // Draw transports
        g2d.setColor(Color.WHITE);
        for (Transport transport : gameState.getTransports()) {
            int scaledX = (int) ((transport.getX() + offsetX) / scaleFactor);
            int scaledY = (int) ((transport.getY() + offsetY) / scaleFactor);
            int transportRadius = (int) (5 / scaleFactor);
            g2d.fillOval(scaledX - transportRadius, scaledY - transportRadius, transportRadius * 2, transportRadius * 2);
            if (transport.getShieldLeftMs() > 0) {
                g2d.drawOval(scaledX - transportRadius * 2, scaledY - transportRadius * 2, transportRadius * 4, transportRadius * 4);
            }
        }

        // Draw bounties
        g2d.setColor(Color.YELLOW);
        g2d.setFont(currentFont.deriveFont(17.0F));
        for (Bounty bounty : gameState.getBounties()) {
            int scaledX = (int) ((bounty.getX() + offsetX) / scaleFactor);
            int scaledY = (int) ((bounty.getY() + offsetY) / scaleFactor);
            int scaledRadius = (int) (Math.max(2, bounty.getRadius() / scaleFactor));
            g2d.drawOval(scaledX - scaledRadius, scaledY - scaledRadius, scaledRadius * 2, scaledRadius * 2);
            g2d.drawString("" + bounty.getPoints(), scaledX - scaledRadius + 1, scaledY - scaledRadius - 2);
        }

        // Draw trajectory
        g2d.setColor(Color.YELLOW);
        for (Vector point : trajectory) {
            int scaledX = (int) ((point.getX() + offsetX) / scaleFactor);
            int scaledY = (int) ((point.getY() + offsetY) / scaleFactor);
            int scaledRadius = 2;
            g2d.drawOval(scaledX - scaledRadius, scaledY - scaledRadius, scaledRadius * 2, scaledRadius * 2);
        }

        // Draw stats
        g2d.setColor(Color.WHITE);
        g2d.setFont(currentFont.deriveFont(30.0F));
        g2d.drawString("HP: " + me.getHealth(), 25, 50);
        g2d.drawString("Attack CD: " + me.getAttackCooldownMs(), 25, 100);
        g2d.drawString("Shield CD: " + me.getShieldCooldownMs(), 25, 150);

        g2d.dispose();
        return mapImage;
    }
}
