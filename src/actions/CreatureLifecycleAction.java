package actions;

import entities.creatures.*;
import worldmap.Coordinates;
import worldmap.WorldMap;

import java.util.*;

public class CreatureLifecycleAction implements Action {
    @Override
    public void perform(WorldMap map) {

        Map<Creature, Coordinates> creatures = map.getAllCreatures(map);
        int deadHerbivores = 0;
        int deadPredators = 0;
        List<Coordinates> toRemove = new ArrayList<>();

        for (Creature creature : creatures.keySet()) {
            if (!creature.isDead()) {
                creature.updateState();
            }
        }

        for (var entry : creatures.entrySet()) {
            Creature creature = entry.getKey();
            if (creature.isDead()) {
                toRemove.add(entry.getValue());
                if (creature instanceof Herbivore) {
                    deadHerbivores++;
                } else if (creature instanceof Predator) {
                    deadPredators++;
                }
            }
        }

        for (Coordinates coord : toRemove) {
            map.removeEntity(coord);
        }

        int totalDeaths = deadHerbivores + deadPredators;
        if (totalDeaths > 0) {
            System.out.printf("Умерло существ: %d (травоядных: %d, хищников: %d)%n",
                    totalDeaths, deadHerbivores, deadPredators);
        }
    }
}