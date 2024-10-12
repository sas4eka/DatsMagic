import java.util.List;

public class GameState {
    private MapSize mapSize;
    private String name;
    private int points;
    private List<Anomaly> anomalies;
    private List<Transport> transports;
    private List<Enemy> enemies;
    private List<Object> wantedList; // Could be further defined if needed
    private List<Bounty> bounties;
    private int maxSpeed;
    private int maxAccel;
    private int attackRange;
    private int attackCooldownMs;
    private int attackDamage;
    private int attackExplosionRadius;
    private int reviveTimeoutSec;
    private int shieldTimeMs;
    private int shieldCooldownMs;
    private int transportRadius;
    private List<Object> errors; // Could be further defined if needed
    private String error;
    private String errCode;

    public GameState() {
    }

    public GameState(GameState other) {
        this.mapSize = other.mapSize;
        this.name = other.name;
        this.points = other.points;
        this.anomalies = other.anomalies;
        this.transports = other.transports;
        this.enemies = other.enemies;
        this.wantedList = other.wantedList;
        this.bounties = other.bounties;
        this.maxSpeed = other.maxSpeed;
        this.maxAccel = other.maxAccel;
        this.attackRange = other.attackRange;
        this.attackCooldownMs = other.attackCooldownMs;
        this.attackDamage = other.attackDamage;
        this.attackExplosionRadius = other.attackExplosionRadius;
        this.reviveTimeoutSec = other.reviveTimeoutSec;
        this.shieldTimeMs = other.shieldTimeMs;
        this.shieldCooldownMs = other.shieldCooldownMs;
        this.transportRadius = other.transportRadius;
        this.errors = other.errors;
        this.error = other.error;
        this.errCode = other.errCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public MapSize getMapSize() {
        return mapSize;
    }

    public void setMapSize(MapSize mapSize) {
        this.mapSize = mapSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<Anomaly> getAnomalies() {
        return anomalies;
    }

    public void setAnomalies(List<Anomaly> anomalies) {
        this.anomalies = anomalies;
    }

    public List<Transport> getTransports() {
        return transports;
    }

    public void setTransports(List<Transport> transports) {
        this.transports = transports;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public List<Object> getWantedList() {
        return wantedList;
    }

    public void setWantedList(List<Object> wantedList) {
        this.wantedList = wantedList;
    }

    public List<Bounty> getBounties() {
        return bounties;
    }

    public void setBounties(List<Bounty> bounties) {
        this.bounties = bounties;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getMaxAccel() {
        return maxAccel;
    }

    public void setMaxAccel(int maxAccel) {
        this.maxAccel = maxAccel;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public int getAttackCooldownMs() {
        return attackCooldownMs;
    }

    public void setAttackCooldownMs(int attackCooldownMs) {
        this.attackCooldownMs = attackCooldownMs;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getAttackExplosionRadius() {
        return attackExplosionRadius;
    }

    public void setAttackExplosionRadius(int attackExplosionRadius) {
        this.attackExplosionRadius = attackExplosionRadius;
    }

    public int getReviveTimeoutSec() {
        return reviveTimeoutSec;
    }

    public void setReviveTimeoutSec(int reviveTimeoutSec) {
        this.reviveTimeoutSec = reviveTimeoutSec;
    }

    public int getShieldTimeMs() {
        return shieldTimeMs;
    }

    public void setShieldTimeMs(int shieldTimeMs) {
        this.shieldTimeMs = shieldTimeMs;
    }

    public int getShieldCooldownMs() {
        return shieldCooldownMs;
    }

    public void setShieldCooldownMs(int shieldCooldownMs) {
        this.shieldCooldownMs = shieldCooldownMs;
    }

    public int getTransportRadius() {
        return transportRadius;
    }

    public void setTransportRadius(int transportRadius) {
        this.transportRadius = transportRadius;
    }

    public List<Object> getErrors() {
        return errors;
    }

    public void setErrors(List<Object> errors) {
        this.errors = errors;
    }
}
