package entities.creatures;


public class Predator extends Creature {
    private int attackPower;

    public Predator() {
        super(2, 30, 15);
        this.attackPower = 10;
    }

    public Predator(int speed, int maxHunger, int healthpoints, int attackPower) {
        super(speed, maxHunger, healthpoints);
        this.attackPower = attackPower;
    }

    public int getAttackPower() {
        return attackPower;
    }
}

