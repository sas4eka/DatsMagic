public class Enemy {
    private int x;
    private int y;
    private Vector velocity;
    private int health;
    private String status;
    private int killBounty;
    private int shieldLeftMs;

    public Enemy() {
    }

    public Enemy(Enemy other) {
        this.x = other.x;
        this.y = other.y;
        this.velocity = other.velocity;
        this.health = other.health;
        this.status = other.status;
        this.killBounty = other.killBounty;
        this.shieldLeftMs = other.shieldLeftMs;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getKillBounty() {
        return killBounty;
    }

    public void setKillBounty(int killBounty) {
        this.killBounty = killBounty;
    }

    public int getShieldLeftMs() {
        return shieldLeftMs;
    }

    public void setShieldLeftMs(int shieldLeftMs) {
        this.shieldLeftMs = shieldLeftMs;
    }
}
