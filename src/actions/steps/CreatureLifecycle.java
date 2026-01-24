package actions.steps;

import actions.Action;
import entities.creatures.*;
import worldmap.Coordinates;
import worldmap.WorldMap;

import java.util.*;

public class CreatureLifecycle implements Action {
    @Override
    public void perform(WorldMap map) {
        Map<Creature, Coordinates> creatures = map.getAllCreatures(map);
        List<Coordinates> deadHerbivores = new ArrayList<>();
        List<Coordinates> deadPredators = new ArrayList<>();

        for (Map.Entry<Creature, Coordinates> entry : creatures.entrySet()) {
            Creature creature = entry.getKey();
            if (!creature.isDead()) {
                creature.updateState();
            }
        }
        for (Map.Entry<Creature, Coordinates> entry : creatures.entrySet()) {
            Creature creature = entry.getKey();
            Coordinates coord = entry.getValue();

            if (creature.isDead()) {
                if (creature instanceof Herbivore) {
                    deadHerbivores.add(coord);
                } else if (creature instanceof Predator) {
                    deadPredators.add(coord);
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