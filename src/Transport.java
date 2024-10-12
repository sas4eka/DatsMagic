public class Transport {
    private int x;
    private int y;
    private String id;
    private Vector velocity;
    private Vector selfAcceleration;
    private Vector anomalyAcceleration;
    private int health;
    private String status;
    private int deathCount;
    private int shieldLeftMs;
    private int shieldCooldownMs;
    private int attackCooldownMs;

    public Transport() {
    }

    public Transport(Transport other) {
        this.x = other.x;
        this.y = other.y;
        this.id = other.id;
        this.velocity = other.velocity;
        this.selfAcceleration = other.selfAcceleration;
        this.anomalyAcceleration = other.anomalyAcceleration;
        this.health = other.health;
        this.status = other.status;
        this.deathCount = other.deathCount;
        this.shieldLeftMs = other.shieldLeftMs;
        this.shieldCooldownMs = other.shieldCooldownMs;
        this.attackCooldownMs = other.attackCooldownMs;
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

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public Vector getSelfAcceleration() {
        return selfAcceleration;
    }

    public void setSelfAcceleration(Vector selfAcceleration) {
        this.selfAcceleration = selfAcceleration;
    }

    public Vector getAnomalyAcceleration() {
        return anomalyAcceleration;
    }

    public void setAnomalyAcceleration(Vector anomalyAcceleration) {
        this.anomalyAcceleration = anomalyAcceleration;
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

    public int getDeathCount() {
        return deathCount;
    }

    public void setDeathCount(int deathCount) {
        this.deathCount = deathCount;
    }

    public int getShieldLeftMs() {
        return shieldLeftMs;
    }

    public void setShieldLeftMs(int shieldLeftMs) {
        this.shieldLeftMs = shieldLeftMs;
    }

    public int getShieldCooldownMs() {
        return shieldCooldownMs;
    }

    public void setShieldCooldownMs(int shieldCooldownMs) {
        this.shieldCooldownMs = shieldCooldownMs;
    }

    public int getAttackCooldownMs() {
        return attackCooldownMs;
    }

    public void setAttackCooldownMs(int attackCooldownMs) {
        this.attackCooldownMs = attackCooldownMs;
    }
}
