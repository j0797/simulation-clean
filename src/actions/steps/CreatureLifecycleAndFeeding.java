package actions.steps;

import actions.Action;
import entities.creatures.*;
import entities.objects.Grass;
import worldmap.Coordinates;
import worldmap.WorldMap;

import java.util.*;

public class CreatureLifecycleAndFeeding implements Action {
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

        Set<Coordinates> toRemove = new HashSet<>();
        int fedHerbivores = 0;
        int fedPredators = 0;

        for (Map.Entry<Creature, Coordinates> entry : creatures.entrySet()) {
            Creature creature = entry.getKey();
            Coordinates coord = entry.getValue();

            if (creature.isDead()) {
                continue;
            }

            if (creature instanceof Herbivore herbivore) {
                for (Coordinates adj : map.getAdjacentCoordinates(coord)) {
                    if (map.getEntity(adj) instanceof Grass && !toRemove.contains(adj)) {

                        herbivore.consumeFood();
                        toRemove.add(adj);
                        fedHerbivores++;
                        break;
                    }
                }
            }
        }
        for (Map.Entry<Creature, Coordinates> entry : creatures.entrySet()) {
            Creature creature = entry.getKey();
            Coordinates coord = entry.getValue();

            if (creature.isDead()) {
                continue;
            }

            if (creature instanceof Predator predator) {
                for (Coordinates adj : map.getAdjacentCoordinates(coord)) {
                    Object target = map.getEntity(adj);
                    if (target instanceof Herbivore herbivore &&
                            !herbivore.isDead() &&
                            !toRemove.contains(adj)) {
                        predator.consumeFood();
                        toRemove.add(adj);
                        fedPredators++;
                        break;
                    }
                }
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

        for (Coordinates coord : toRemove) {
            map.removeEntity(coord);
        }

        for (Coordinates coord : deadHerbivores) map.removeEntity(coord);
        for (Coordinates coord : deadPredators) map.removeEntity(coord);

        int totalDeaths = deadHerbivores.size() + deadPredators.size();
        if (fedHerbivores > 0 || fedPredators > 0) {
            System.out.printf("Покормлено: %d травоядных, %d хищников%n", fedHerbivores, fedPredators);
        }
        if (totalDeaths > 0) {
            System.out.printf("Умерло существ: %d (травоядных: %d, хищников: %d)%n", totalDeaths, deadHerbivores.size(), deadPredators.size());
        }
    }
}