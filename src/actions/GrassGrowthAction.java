package actions;

import worldmap.Coordinates;
import worldmap.WorldMap;
import entities.objects.Grass;

import java.util.Random;

public class GrassGrowthAction implements Action {
    private final Random random = new Random();
    private double growthChance = 0.05;

    @Override
    public void perform(WorldMap map) {
        for (int row = 0; row < map.getHeight(); row++) {
            for (int col = 0; col < map.getWidth(); col++) {
                Coordinates coord = new Coordinates(row, col);


                if (map.getEntity(coord) == null && random.nextDouble() < growthChance) {
                    map.placeEntity(coord, new Grass());
                }
            }
        }
    }

    public void setGrowthChance(double chance) {
        this.growthChance = chance;
    }
}