package actions.steps;

import actions.Action;
import entities.creatures.Herbivore;
import entities.creatures.Predator;
import entities.objects.Grass;
import map.Coordinates;
import map.WorldMap;

import java.util.*;

public class CreatureFeeding implements Action {
    @Override
    public void perform(WorldMap map) {
        Set<Coordinates> toRemove = new HashSet<>();
        int fedHerbivores = 0;
        int fedPredators = 0;

        Map<Coordinates, Object> creaturePositions = new HashMap<>();
        for (int row = 0; row < map.getHeight(); row++) {
            for (int col = 0; col < map.getWidth(); col++) {
                Coordinates coord = new Coordinates(row, col);
                Object entity = map.getEntity(coord);
                if (entity instanceof Herbivore || entity instanceof Predator) {
                    creaturePositions.put(coord, entity);
                }
            }
        }

        for (Map.Entry<Coordinates, Object> entry : creaturePositions.entrySet()) {
            Coordinates coord = entry.getKey();
            Object entity = entry.getValue();

            if (entity instanceof Herbivore herbivore && !herbivore.isDead()) {
                for (Coordinates adj : map.getAdjacentCoordinates(coord)) {
                    if (map.getEntity(adj) instanceof Grass && !toRemove.contains(adj)) {
                        herbivore.consumeFood();
                        toRemove.add(adj);
                        fedHerbivores++;
                        break;
                    }
                }
            } else if (entity instanceof Predator predator && !predator.isDead()) {
                for (Coordinates adj : map.getAdjacentCoordinates(coord)) {
                    Object target = map.getEntity(adj);
                    if (target instanceof Herbivore herbivore && !herbivore.isDead() && !toRemove.contains(adj)) {
                        predator.consumeFood();
                        toRemove.add(adj);
                        fedPredators++;
                        break;
                    }
                }
            }
        }

        for (Coordinates coord : toRemove) {
            map.removeEntity(coord);
        }

        System.out.println("Покормлено: " + fedHerbivores + " травоядных, " + fedPredators + " хищников");
    }
}