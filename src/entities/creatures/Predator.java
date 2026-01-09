package entities.creatures;

import worldmap.Coordinates;
import worldmap.WorldMap;
import java.util.Random;

public class Predator extends Creature {
    private static final int DEFAULT_SPEED = 2;
    private static final int DEFAULT_HEALTH_POINTS = 30;
    private static final int DEFAULT_ATTACK_POWER = 10;

    private int attackPower;

    public Predator() {
        this(DEFAULT_SPEED, DEFAULT_HEALTH_POINTS, DEFAULT_ATTACK_POWER);
    }

    public Predator(int speed, int healthPoints, int attackPower) {
        super(speed, healthPoints);
        this.attackPower = attackPower;
    }

    @Override
    public Coordinates makeMove(WorldMap worldMap, Coordinates currentPosition) {

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                Coordinates check = new Coordinates(currentPosition.row() + dr, currentPosition.column() + dc);

                if (worldMap.isWithinBounds(check)) {
                    var entity = worldMap.getEntity(check);
                    if (entity.isPresent() && entity.get() instanceof Herbivore) {
                        Herbivore herbivore = (Herbivore) entity.get();
                        herbivore.takeDamage(attackPower);

                        if (!herbivore.isAlive()) {
                            worldMap.removeEntity(check);
                        }
                        // Атаковали - остаемся на месте
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

    public int getAttackPower() {
        return attackPower;
    }
}
