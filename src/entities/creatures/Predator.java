package entities.creatures;


import worldmap.WorldMap;

public class Predator extends Creature {
    private static final int DEFAULT_SPEED = 2;
    private static final int DEFAULT_MAX_HUNGER = 30;
    private static final int DEFAULT_HP = 15;
    private static final int DEFAULT_ATTACK_POWER = 5;
    private int attackPower;

    public Predator() {
        this(DEFAULT_SPEED, DEFAULT_MAX_HUNGER, DEFAULT_HP, DEFAULT_ATTACK_POWER);
    }

    public Predator(int speed, int maxHunger, int healthpoints, int attackPower) {
        super(speed, maxHunger, healthpoints);
        this.attackPower = attackPower;
    }

    @Override
    public void makeMove(WorldMap worldMap) {

    }

    public int getAttackPower() {
        return attackPower;
    }
}

