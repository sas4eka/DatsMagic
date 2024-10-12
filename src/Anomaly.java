public class Anomaly {
    private int x;
    private int y;
    private String id;
    private double radius;
    private double effectiveRadius;
    private int strength;
    private Vector velocity;

    public Anomaly() {
    }

    public Anomaly(Anomaly other) {
        this.x = other.x;
        this.y = other.y;
        this.id = other.id;
        this.radius = other.radius;
        this.effectiveRadius = other.effectiveRadius;
        this.strength = other.strength;
        this.velocity = other.velocity;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getEffectiveRadius() {
        return effectiveRadius;
    }

    public void setEffectiveRadius(double effectiveRadius) {
        this.effectiveRadius = effectiveRadius;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }
}
