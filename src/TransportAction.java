public class TransportAction {
    private Vector acceleration;
    private boolean activateShield;
    private IntVector attack;
    private String id;

    public Vector getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector acceleration) {
        this.acceleration = acceleration;
    }

    public boolean isActivateShield() {
        return activateShield;
    }

    public void setActivateShield(boolean activateShield) {
        this.activateShield = activateShield;
    }

    public IntVector getAttack() {
        return attack;
    }

    public void setAttack(IntVector attack) {
        this.attack = attack;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
