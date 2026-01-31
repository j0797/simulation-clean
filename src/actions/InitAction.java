package actions;

import entities.Entity;
import entities.creatures.Herbivore;
import entities.creatures.Predator;
import entities.objects.Grass;
import entities.objects.Rock;
import entities.objects.Tree;
import worldmap.Coordinates;
import worldmap.WorldMap;

import java.util.Random;

public class InitAction implements Action {
    private final Random random = new Random();
    private static final int INITIAL_TREES = 15;
    private static final int INITIAL_ROCKS = 10;
    private static final int INITIAL_GRASS = 10;
    private static final int INITIAL_HERBIVORES = 10;
    private static final int INITIAL_PREDATORS = 5;

    @Override
    public void perform(WorldMap map) {


        for (int i = 0; i < INITIAL_TREES; i++) placeRandomEntity(map, new Tree());
        for (int i = 0; i < INITIAL_ROCKS; i++) placeRandomEntity(map, new Rock());
        for (int i = 0; i < INITIAL_GRASS; i++) placeRandomEntity(map, new Grass());
        for (int i = 0; i < INITIAL_HERBIVORES; i++) placeRandomEntity(map, new Herbivore());
        for (int i = 0; i < INITIAL_PREDATORS; i++) placeRandomEntity(map, new Predator());

    }

    private void placeRandomEntity(WorldMap map, Entity entity) {
        int attempts = 0;
        int maxAttempts = 100;

        while (attempts < maxAttempts) {

            int row = random.nextInt(map.getHeight());
            int col = random.nextInt(map.getWidth());
            Coordinates coordinates = new Coordinates(row, col);


            if (map.getEntity(coordinates).isEmpty()) {
                map.placeEntity(coordinates, entity);
                return;
            }
            attempts++;
        }
    }
}
