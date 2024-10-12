import java.awt.image.BufferedImage;
import java.util.List;

public class GameFrame {
    BufferedImage minimap;
    List<BufferedImage> ships;

    public GameFrame(BufferedImage minimap, List<BufferedImage> ships) {
        this.minimap = minimap;
        this.ships = ships;
    }
}
