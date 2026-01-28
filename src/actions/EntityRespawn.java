package actions;

import entities.creatures.Herbivore;
import entities.creatures.Predator;
import entities.objects.Grass;
import worldmap.Coordinates;
import worldmap.WorldMap;

import java.util.Optional;
import java.util.Random;

public class EntityRespawn implements Action {
    private final Random random;
    private final double grassGrowthChance;
    private int totalGrassGrown = 0;
    private static final int MIN_HERBIVORES = 3;
    private static final int MIN_PREDATORS = 1;
    private static final double HERBIVORE_ADD_CHANCE = 0.05;
    private static final double PREDATOR_ADD_CHANCE = 0.02;

    public EntityRespawn() {
        this(new Random(), 0.1);
    }

    public EntityRespawn(Random random, double grassGrowthChance) {
        this.random = random;
        this.grassGrowthChance = grassGrowthChance;
    }

    @Override
    public void perform(WorldMap map) {
        int grassGrown = 0;
        int herbivoresAdded = 0;
        int predatorsAdded = 0;

        int attempts = map.getWidth() * map.getHeight() * 3;
        for (int i = 0; i < attempts; i++) {
            Coordinates coord = getRandomCoordinates(map);

            if (map.isEmptyCell(coord) && random.nextDouble() < grassGrowthChance) {
                if (map.placeEntity(coord, new Grass())) {
                    grassGrown++;
                    totalGrassGrown++;
                }
            }
        }
        if (shouldAddHerbivore(map)) {
            herbivoresAdded = tryAddHerbivore(map);
        }

        if (shouldAddPredator(map)) {
            predatorsAdded = tryAddPredator(map);
        }


        if (grassGrown > 0 || herbivoresAdded > 0 || predatorsAdded > 0) {
            System.out.printf("Появилось: +%d травы (всего %d), +%d травоядных, +%d хищников %n", grassGrown, totalGrassGrown, herbivoresAdded, predatorsAdded);
        }
    }

    private Coordinates getRandomCoordinates(WorldMap map) {
        int row = random.nextInt(map.getHeight());
        int col = random.nextInt(map.getWidth());
        return new Coordinates(row, col);
    }

    private boolean shouldAddHerbivore(WorldMap map) {
        return map.getEntitiesOfType(Herbivore.class).size() < MIN_HERBIVORES && random.nextDouble() < HERBIVORE_ADD_CHANCE;
    }

    private int tryAddHerbivore(WorldMap map) {

        Optional<Coordinates> coord = map.getRandomEmptyCoordinate(random);
        if (coord.isPresent() && map.placeEntity(coord.get(), new Herbivore())) {
            return 1;
        }
        return 0;
    }

    private boolean shouldAddPredator(WorldMap map) {
        return map.getEntitiesOfType(Predator.class).size() < MIN_PREDATORS && random.nextDouble() < PREDATOR_ADD_CHANCE;
    }

    private int tryAddPredator(WorldMap map) {

        Optional<Coordinates> coord = map.getRandomEmptyCoordinate(random);
        if (coord.isPresent() && map.placeEntity(coord.get(), new Predator())) {
            return 1;
        }
        return 0;
    }
}