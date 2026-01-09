package entities.creatures;

import worldmap.Coordinates;
import worldmap.WorldMap;
import java.util.Random;
import entities.objects.Grass;

public class Herbivore extends Creature {
    private static final int DEFAULT_SPEED = 1;
    private static final int DEFAULT_HEALTH_POINTS = 20;

    public Herbivore() {
        this(DEFAULT_SPEED, DEFAULT_HEALTH_POINTS);
    }

    public Herbivore(int speed, int healthPoints) {
        super(speed, healthPoints);
    }

    @Override
    public Coordinates makeMove(WorldMap worldMap, Coordinates currentPosition) {

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                Coordinates check = new Coordinates(currentPosition.row() + dr, currentPosition.column() + dc);

                if (worldMap.isWithinBounds(check)) {
                    var entity = worldMap.getEntity(check);
                    if (entity.isPresent() && entity.get() instanceof Grass) {

                        worldMap.removeEntity(check);

                        return currentPosition;
                    }
                }
            }
        }


        Random rand = new Random();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int i = 0; i < 4; i++) {
            int idx = rand.nextInt(4);
            Coordinates newPos = new Coordinates(
                    currentPosition.row() + directions[idx][0],
                    currentPosition.column() + directions[idx][1]
            );

            if (worldMap.isWithinBounds(newPos) && worldMap.getEntity(newPos).isEmpty()) {
                return newPos;
            }
        }

        return currentPosition;
    }
}
