package actions;

import entities.creatures.*;
import entities.objects.Grass;
import worldmap.WorldMap;
import worldmap.Coordinates;
import java.util.Map;

public class CreatureFeedingAction implements Action {
    @Override
    public void perform(WorldMap map) {
        Map<Creature, Coordinates> creatures = map.getAllCreatures(map);
        int fedHerbivores = 0;
        int fedPredators = 0;

        for (var entry : creatures.entrySet()) {
            if (entry.getKey() instanceof Herbivore herbivore && !herbivore.isDead()) {
                Coordinates coord = entry.getValue();
                for (Coordinates adj : map.getAdjacentCoordinates(coord)) {
                    if (map.getEntity(adj) instanceof Grass) {
                        herbivore.consumeFood();
                        map.removeEntity(adj);
                        fedHerbivores++;
                        break;
                    }
                }
            }
        }

        for (var entry : creatures.entrySet()) {
            if (entry.getKey() instanceof Predator predator && !predator.isDead()) {
                Coordinates coord = entry.getValue();
                for (Coordinates adj : map.getAdjacentCoordinates(coord)) {
                    Object target = map.getEntity(adj);
                    if (target instanceof Herbivore herbivore && !herbivore.isDead()) {
                        predator.consumeFood();
                        map.removeEntity(adj);
                        fedPredators++;
                        break;
                    }
                }
            }
        }

        if (fedHerbivores > 0 || fedPredators > 0) {
            System.out.printf("Покормлено: %d травоядных, %d хищников%n",
                    fedHerbivores, fedPredators);
        }
    }
}