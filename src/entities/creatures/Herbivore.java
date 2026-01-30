package entities.creatures;


import entities.Entity;
import entities.objects.Grass;
import worldmap.Coordinates;
import worldmap.WorldMap;

import java.util.Random;

public class Herbivore extends Creature {
    private static final int DEFAULT_SPEED = 1;
    private static final int DEFAULT_MAX_HUNGER = 20;
    private static final int DEFAULT_HP = 10;
    private final Random random = new Random();


    public Herbivore() {
        this(DEFAULT_SPEED, DEFAULT_MAX_HUNGER, DEFAULT_HP);
    }

    public Herbivore(int speed, int maxHunger, int healthpoints) {
        super(speed, maxHunger, healthpoints);
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
                    Entity entity = worldMap.getEntity(check);
                    if (entity instanceof Grass) {
                        worldMap.removeEntity(check);
                        consumeFood();
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

            if (worldMap.isValidCoordinate(newPos) && worldMap.getEntity(newPos) == null) {
                return newPos;
            }
        }

        return currentPos;
    }
}