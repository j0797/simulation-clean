package actions;

import entities.objects.Grass;
import worldmap.WorldMap;
import worldmap.Coordinates;

import java.util.Random;

public class GrassRegrowthAction implements Action {
    private final Random random = new Random();

    @Override
    public void perform(WorldMap map) {
        int grassGrown = 0;
        for (int row = 0; row < map.getHeight(); row++) {
            for (int col = 0; col < map.getWidth(); col++) {
                if (random.nextDouble() < 0.1) {
                    Coordinates coord = new Coordinates(row, col);
                    if (map.isEmptyCell(coord)) {
                        map.placeEntity(coord, new Grass());
                        grassGrown++;
                    }
                }
            }
        }
        if (grassGrown > 0) {
            System.out.printf("Выросло травы: %d%n", grassGrown);
        }
    }
}