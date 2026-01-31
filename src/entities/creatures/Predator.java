package entities.creatures;


import entities.Entity;
import worldmap.Coordinates;
import worldmap.WorldMap;

import java.util.Optional;

public class Predator extends Creature {
    private static final int DEFAULT_SPEED = 2;
    private static final int DEFAULT_MAX_HUNGER = 30;
    private static final int DEFAULT_HP = 15;
    private static final int DEFAULT_ATTACK_POWER = 5;
    private final int attackPower;

    public Predator() {
        this(DEFAULT_SPEED, DEFAULT_MAX_HUNGER, DEFAULT_HP, DEFAULT_ATTACK_POWER);
    }

    public Predator(int speed, int maxHunger, int healthpoints, int attackPower) {
        super(speed, maxHunger, healthpoints);
        this.attackPower = attackPower;
    }

    @Override
    public Coordinates makeMove(WorldMap worldMap, Coordinates currentPos) {
        updateState();

        if (isDead()) {
            return currentPos;
        }
        Optional<Coordinates> herbivoreCoordOptional =
                findAdjacentEntityCoordinates(worldMap, currentPos, Herbivore.class);

        if (herbivoreCoordOptional.isPresent()) {
            Coordinates herbivoreCoord = herbivoreCoordOptional.get();
            Optional<Entity> entity = worldMap.getEntity(herbivoreCoord);
            if (entity.isPresent() && entity.get() instanceof Herbivore herbivore) {
                herbivore.takeDamage(attackPower);

                if (herbivore.isDead()) {
                    worldMap.removeEntity(herbivoreCoord);
                    consumeFood();
                }
            }
            return currentPos;
        }
        return getRandomMove(worldMap, currentPos);
    }

    public int getAttackPower() {
        return attackPower;
    }
}