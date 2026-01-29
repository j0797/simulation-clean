package pathfinder;

import entities.Entity;
import worldmap.Coordinates;
import worldmap.WorldMap;

import java.util.List;

public interface Path {
    List<Coordinates> findPath(WorldMap map, Coordinates start, Class<? extends Entity> targetType);
}