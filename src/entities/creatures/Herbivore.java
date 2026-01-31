package entities.creatures;


import entities.Entity;
import entities.objects.Grass;
import worldmap.Coordinates;
import worldmap.WorldMap;

import java.util.Optional;

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
        updateState();

        if (isDead()) {
            return currentPos;
        }
        Optional<Coordinates> grassCoordOptional =
                findAdjacentEntityCoordinates(worldMap, currentPos, Grass.class);

        if (grassCoordOptional.isPresent()) {
            Coordinates grassCoord = grassCoordOptional.get();
            Optional<Entity> entity = worldMap.getEntity(grassCoord);
            if (entity.isPresent() && entity.get() instanceof Grass) {
                worldMap.removeEntity(grassCoord);
                consumeFood();
            }
            return currentPos;
        }
        return getRandomMove(worldMap, currentPos);
    }
}
