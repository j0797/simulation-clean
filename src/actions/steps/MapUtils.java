package actions.steps;

import entities.creatures.Creature;
import map.Coordinates;
import map.WorldMap;

import java.util.*;

public class MapUtils {
    public static Map<Creature, Coordinates> getAllCreatures(WorldMap map) {
        Map<Creature, Coordinates> creatures = new HashMap<>();
        for (int row = 0; row < map.getHeight(); row++) {
            for (int col = 0; col < map.getWidth(); col++) {
                Coordinates coord = new Coordinates(row, col);
                Object entity = map.getEntity(coord);
                if (entity instanceof Creature creature) {
                    creatures.put(creature, coord);
                }
            }
        }
        return creatures;
    }

}
