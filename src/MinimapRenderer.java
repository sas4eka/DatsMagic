import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class MinimapRenderer {
    static BufferedImage drawMinimap(GameState gameState, PlayerAction action, int shipArea, int scaleFactor) {
        MapSize mapSize = gameState.getMapSize();

        int scaledWidth = mapSize.getX() / scaleFactor;
        int scaledHeight = mapSize.getY() / scaleFactor;
        BufferedImage mapImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = mapImage.createGraphics();

        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 0, scaledWidth, scaledHeight);

        // Draw ship areas
        g2d.setColor(Color.GRAY);
        int index = 0;
        Font currentFont = g2d.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 18.0F);
        g2d.setFont(newFont);
        for (Transport transport : gameState.getTransports()) {
            index++;
            int scaledX = transport.getX() / scaleFactor;
            int scaledY = transport.getY() / scaleFactor;
            int scaledShipArea = shipArea / scaleFactor;
            g2d.drawRect(scaledX - scaledShipArea / 2, scaledY - scaledShipArea / 2, scaledShipArea, scaledShipArea);
            g2d.drawString("" + index, scaledX - scaledShipArea / 2 + 28, scaledY + scaledShipArea / 2 - 10);
        }

        // Draw anomalies
        g2d.setColor(Color.RED);
        for (Anomaly anomaly : gameState.getAnomalies()) {
            int scaledX = anomaly.getX() / scaleFactor;
            int scaledY = anomaly.getY() / scaleFactor;
            int scaledRadius = (int) (anomaly.getRadius() / scaleFactor);
            g2d.fillOval(scaledX - scaledRadius, scaledY - scaledRadius, scaledRadius * 2, scaledRadius * 2);
        }

        // Draw anomaly effective radius
        g2d.setColor(Color.GRAY);
        for (Anomaly anomaly : gameState.getAnomalies()) {
            int scaledX = anomaly.getX() / scaleFactor;
            int scaledY = anomaly.getY() / scaleFactor;
            int effectiveRadius = (int) (anomaly.getEffectiveRadius() / scaleFactor);
            g2d.drawOval(scaledX - effectiveRadius, scaledY - effectiveRadius, effectiveRadius * 2, effectiveRadius * 2);
        }

        // Draw enemies
        g2d.setColor(Color.BLUE);
        for (Enemy enemy : gameState.getEnemies()) {
            int scaledX = enemy.getX() / scaleFactor;
            int scaledY = enemy.getY() / scaleFactor;
            int enemySize = 25 / scaleFactor;
            g2d.fillOval(scaledX - enemySize, scaledY - enemySize, enemySize * 2, enemySize * 2);
        }

        // Draw bounties
        g2d.setColor(Color.YELLOW);
        for (Bounty bounty : gameState.getBounties()) {
            int scaledX = bounty.getX() / scaleFactor;
            int scaledY = bounty.getY() / scaleFactor;
            int scaledRadius = Math.max(2, bounty.getRadius() / scaleFactor);
            g2d.fillOval(scaledX - scaledRadius, scaledY - scaledRadius, scaledRadius * 2, scaledRadius * 2);
        }

        Map<String, Transport> transports = new HashMap<>();
        for (Transport transport : gameState.getTransports()) {
            transports.put(transport.getId(), transport);
        }

        // Draw velocity and acceleration
        for (TransportAction ta : action.getTransports()) {
            Transport transport = transports.get(ta.getId());
            int scaledX = (int) (transport.getX() / scaleFactor);
            int scaledY = (int) (transport.getY() / scaleFactor);

            Stroke oldStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(4));

            int mult = 3;

            g2d.setColor(Color.RED);
            int anomalyX = (int) (mult * 11 * transport.getAnomalyAcceleration().getX() / scaleFactor);
            int anomalyY = (int) (mult * 11 * transport.getAnomalyAcceleration().getY() / scaleFactor);
            g2d.drawLine(scaledX, scaledY, scaledX + anomalyX, scaledY + anomalyY);

            g2d.setColor(Color.WHITE);
            int velocityX = (int) (mult * transport.getVelocity().getX() / scaleFactor);
            int velocityY = (int) (mult * transport.getVelocity().getY() / scaleFactor);
            g2d.drawLine(scaledX, scaledY, scaledX + velocityX, scaledY + velocityY);

            g2d.setColor(Color.YELLOW);
            int accelerationX = (int) (mult * 11 * ta.getAcceleration().getX() / scaleFactor);
            int accelerationY = (int) (mult * 11 * ta.getAcceleration().getY() / scaleFactor);
            g2d.drawLine(scaledX, scaledY, scaledX + accelerationX, scaledY + accelerationY);

            g2d.setStroke(oldStroke);
        }

        // Draw transports
        g2d.setColor(Color.WHITE);
        for (Transport transport : gameState.getTransports()) {
            int scaledX = transport.getX() / scaleFactor;
            int scaledY = transport.getY() / scaleFactor;
            int transportRadius = 30 / scaleFactor;
            g2d.fillOval(scaledX - transportRadius, scaledY - transportRadius, transportRadius * 2, transportRadius * 2);
        }

        g2d.dispose();
        return mapImage;
    }
}
