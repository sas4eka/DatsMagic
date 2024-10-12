import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Visualizer extends JFrame implements KeyListener, MouseListener {
    private static final long serialVersionUID = -6754436015453195809L;

    private List<GameFrame> frames;
    int frameIndex = 0;

    int shipIndex = 0;

    static final private int ZOOM_LIMIT = 16;
    private int zoom = -2;
    private int lastX = -1;
    private int lastY = -1;

    public void setFrames(List<GameFrame> frameList) {
        frames = frameList;
        frameIndex = 0;
        repaint();
    }

    public Visualizer() {
        super();
        frames = Collections.emptyList();

        JPanel panel = new JPanel() {
            private static final long serialVersionUID = 4370983408153076632L;

            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                double scale = zoom >= 0 ? 1 + zoom * 0.5 : (2.0 / (2 - zoom));
                if (!frames.isEmpty()) {
                    GameFrame gameFrame = frames.get(frameIndex);
                    BufferedImage ship = gameFrame.ships.get(shipIndex);
                    BufferedImage minimap = gameFrame.minimap;
                    Graphics2D g2 = (Graphics2D) graphics;
                    AffineTransform at = new AffineTransform();
                    at.scale(scale, scale);
                    g2.transform(at);
                    g2.drawImage(ship, 5, 0, null);
                    g2.drawImage(minimap, 10 + ship.getWidth(), 0, null);
                    setPreferredSize(new Dimension((int) (minimap.getWidth() * scale), (int) (minimap.getHeight() * scale)));
                }
                getParent().revalidate();
            }
        };
        JScrollPane sp = new JScrollPane(panel);
        sp.setBorder(BorderFactory.createEmptyBorder());
        add(sp);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addKeyListener(this);
        panel.addMouseListener(this);
        setLocation(50, 50);
        setSize(1830, 940);
        setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() >= '1' && e.getKeyChar() <= '5') {
            shipIndex = e.getKeyChar() - '1';
        } else if (e.getKeyChar() == ']') {
            if (zoom < ZOOM_LIMIT) {
                zoom++;
            }
        } else if (e.getKeyChar() == '[') {
            if (zoom > -ZOOM_LIMIT) {
                zoom--;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            frameIndex = Math.max(0, frameIndex - 1);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            frameIndex = Math.min(frames.size() - 1, frameIndex + 1);
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}