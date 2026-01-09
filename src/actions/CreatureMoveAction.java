package actions;

import worldmap.Coordinates;
import worldmap.WorldMap;
import entities.creatures.Creature;
import entities.Entity;

import java.util.*;

public class CreatureMoveAction implements Action {
    @Override
    public void perform(WorldMap map) {

        Map<Creature, Coordinates> creaturesToMove = new HashMap<>();


        for (int row = 0; row < map.getHeight(); row++) {
            for (int col = 0; col < map.getWidth(); col++) {
                Coordinates currentPos = new Coordinates(row, col);
                Optional<Entity> entity = map.getEntity(currentPos);

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


            if (map.isWithinBounds(desiredPos) &&
                    map.getEntity(desiredPos).isEmpty()) {

                map.removeEntity(currentPos);
                map.putEntity(desiredPos, creature);
            }
        }
    }
}