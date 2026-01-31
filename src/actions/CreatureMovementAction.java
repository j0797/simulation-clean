package actions;

import entities.Entity;
import entities.creatures.Creature;
import worldmap.Coordinates;
import worldmap.WorldMap;

import java.util.*;

public class CreatureMovementAction implements Action {
    @Override
    public void perform(WorldMap map) {
        List<Coordinates> creatureCoordinates = new ArrayList<>();

        for (int row = 0; row < map.getHeight(); row++) {
            for (int col = 0; col < map.getWidth(); col++) {
                Coordinates currentPos = new Coordinates(row, col);
                Optional<Entity> entity = map.getEntity(currentPos);

                if (entity.isPresent() && entity.get() instanceof Creature creature) {
                    if (creature.isAlive()) {
                        creatureCoordinates.add(currentPos);
                    }
                }
            }
        }
        for (Coordinates currentPos : creatureCoordinates) {
            Optional<Entity> entityOpt = map.getEntity(currentPos);

            if (entityOpt.isEmpty() || !(entityOpt.get() instanceof Creature creature) || !creature.isAlive()) {
                continue;
            }

            Coordinates desiredPos = creature.makeMove(map, currentPos);


            if (desiredPos.equals(currentPos)) {
                continue;
            }


            if (map.isValidCoordinate(desiredPos)) {

                if (map.getEntity(desiredPos).isEmpty()) {
                    map.removeEntity(currentPos);
                    map.placeEntity(desiredPos, creature);
                }
            }
        }
    }
}