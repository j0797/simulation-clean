package actions.steps;

import actions.Action;
import entities.objects.Grass;
import map.Coordinates;
import map.WorldMap;

import java.util.Random;


public class GrassRegrowth implements Action {
    private final Random random;
    private final double growthChance;
    private int totalGrassGrown = 0;

    public GrassRegrowth() {
        this(new Random(), 0.1);
    }

    public GrassRegrowth(Random random, double growthChance) {
        this.random = random;
        this.growthChance = growthChance;
    }

    @Override
    public void perform(WorldMap map) {
        int newGrassCount = 0;

        int attempts = map.getWidth() * map.getHeight() * 3;


        for (int i = 0; i < attempts; i++) {
            Coordinates coord = getRandomCoordinates(map);

            if (map.isEmptyCell(coord) && random.nextDouble() < growthChance) {
                if (map.placeEntity(coord, new Grass())) {
                    newGrassCount++;
                    totalGrassGrown++;
                }
            }
        }

        if (newGrassCount > 0) {
            System.out.printf("Выросло травы: %d (всего: %d)%n", newGrassCount, totalGrassGrown);
        }
    }

    private Coordinates getRandomCoordinates(WorldMap map) {
        int row = random.nextInt(map.getHeight());
        int col = random.nextInt(map.getWidth());
        return new Coordinates(row, col);
    }

}