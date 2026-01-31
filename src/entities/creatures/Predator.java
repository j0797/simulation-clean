package entities.creatures;


import entities.Entity;
import worldmap.Coordinates;
import worldmap.WorldMap;

import java.util.Optional;
import java.util.Random;

public class Predator extends Creature {
    private static final int DEFAULT_SPEED = 2;
    private static final int DEFAULT_MAX_HUNGER = 30;
    private static final int DEFAULT_HP = 15;
    private static final int DEFAULT_ATTACK_POWER = 5;
    private final Random random = new Random();
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
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                Coordinates check = new Coordinates(currentPos.row() + dr, currentPos.column() + dc);

                if (worldMap.isValidCoordinate(check)) {
                    Optional<Entity> entity = worldMap.getEntity(check);
                    if (entity.isPresent() && entity.get() instanceof Herbivore herbivore) {
                        herbivore.takeDamage(attackPower);

                        if (herbivore.isDead()) {
                            worldMap.removeEntity(check);
                            consumeFood();
                        }
                        return currentPos;
                    }
                }
            }
        }

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int i = 0; i < 4; i++) {
            int idx = random.nextInt(4);
            Coordinates newPos = new Coordinates(
                    currentPos.row() + directions[idx][0],
                    currentPos.column() + directions[idx][1]
            );

            if (worldMap.isValidCoordinate(newPos) && worldMap.getEntity(newPos).isEmpty()) {
                return newPos;
            }
        }
        return currentPos;
    }

    public int getAttackPower() {
        return attackPower;
    }
}

