package entities.creatures;


import worldmap.Coordinates;
import worldmap.WorldMap;

public class Herbivore extends Creature {
    private static final int DEFAULT_SPEED = 1;
    private static final int DEFAULT_MAX_HUNGER = 20;
    private static final int DEFAULT_HP = 10;


    public Herbivore() {
        this(DEFAULT_SPEED, DEFAULT_MAX_HUNGER, DEFAULT_HP);
    }

    public Herbivore(int speed, int maxHunger, int healthpoints) {
        super(speed, maxHunger, healthpoints);
    }

    @Override
    public Coordinates makeMove(WorldMap worldMap, Coordinates currentPos) {
        return currentPos;
    }
}
