package actions.steps;

import actions.Action;
import entities.creatures.Creature;
import entities.creatures.Herbivore;
import entities.creatures.Predator;
import worldmap.Coordinates;
import worldmap.WorldMap;

import java.util.*;

public class DeathProcessing implements Action {
    @Override
    public void perform(WorldMap map) {
        List<Coordinates> deadHerbivores = new ArrayList<>();
        List<Coordinates> deadPredators = new ArrayList<>();

        for (int row = 0; row < map.getHeight(); row++) {
            for (int col = 0; col < map.getWidth(); col++) {
                Coordinates coord = new Coordinates(row, col);
                Object entity = map.getEntity(coord);

                if (entity instanceof Creature creature && creature.isDead()) {
                    if (creature instanceof Herbivore) {
                        deadHerbivores.add(coord);
                    } else if (creature instanceof Predator) {
                        deadPredators.add(coord);
                    }
                }
            }
        }

        int totalDeaths = deadHerbivores.size() + deadPredators.size();
        for (Coordinates coord : deadHerbivores) map.removeEntity(coord);
        for (Coordinates coord : deadPredators) map.removeEntity(coord);

        if (totalDeaths > 0) {
            System.out.printf("Умерло существ: %d (травоядных: %d, хищников: %d)%n", totalDeaths, deadHerbivores.size(), deadPredators.size());
        }
    }
}