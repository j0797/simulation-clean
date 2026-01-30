package actions;

import entities.Entity;
import entities.creatures.Creature;
import worldmap.Coordinates;
import worldmap.WorldMap;

import java.util.*;

public class CreatureMovementAction implements Action {
    @Override
    public void perform(WorldMap map) {
        Map<Creature, Coordinates> creaturesToMove = new HashMap<>();


        for (int row = 0; row < map.getHeight(); row++) {
            for (int col = 0; col < map.getWidth(); col++) {
                Coordinates currentPos = new Coordinates(row, col);
                Optional<Entity> entity = Optional.ofNullable(map.getEntity(currentPos));

                if (entity.isPresent() && entity.get() instanceof Creature creature) {
                    if (creature.isAlive()) {
                        creaturesToMove.put(creature, currentPos);
                    }
                }
            }
        }


        for (Map.Entry<Creature, Coordinates> entry : creaturesToMove.entrySet()) {
            Creature creature = entry.getKey();
            Coordinates currentPos = entry.getValue();


            Coordinates desiredPos = creature.makeMove(map, currentPos);


            if (desiredPos.equals(currentPos)) {
                continue;
            }


            if (map.isValidCoordinate(desiredPos)) {
                Optional<Entity> entityAtDesiredPos = Optional.ofNullable(map.getEntity(desiredPos));
                if (entityAtDesiredPos.isEmpty()) {
                    map.removeEntity(currentPos);
                    map.placeEntity(desiredPos, creature);
                }
            }
        }
    }
}