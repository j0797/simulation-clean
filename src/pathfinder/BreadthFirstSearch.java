package pathfinder;

import entities.Entity;
import entities.objects.Grass;
import entities.creatures.Herbivore;
import map.Coordinates;
import map.WorldMap;

import java.util.*;

public class BreadthFirstSearch {


    public Optional<Coordinates> findNearestGrass(WorldMap map, Coordinates start) {
        return findNearestEntityInternal(map, start, Grass.class, false);
    }

    public Optional<Coordinates> findNearestHerbivore(WorldMap map, Coordinates start) {
        return findNearestEntityInternal(map, start, Herbivore.class, true);
    }

    private Optional<Coordinates> findNearestEntityInternal(WorldMap map, Coordinates start,
                                                            Class<? extends Entity> entityType,
                                                            boolean canWalkOnGrass) {
        if (!map.isValidCoordinate(start)) return Optional.empty();

        Queue<Coordinates> queue = new LinkedList<>();
        Set<Coordinates> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Coordinates current = queue.poll();

            Entity entity = map.getEntity(current);
            if (entityType.isInstance(entity)) {
                return Optional.of(current);
            }

            for (Coordinates neighbor : getPassableNeighbors(map, current, canWalkOnGrass, entityType)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return Optional.empty();
    }

    public Optional<Coordinates> getNextStepToTarget(WorldMap map, Coordinates start,
                                                     Coordinates target, boolean canWalkOnGrass) {
        if (target == null) return Optional.empty();

        Optional<List<Coordinates>> path = findPath(map, start, target, canWalkOnGrass);
        if (path.isPresent() && path.get().size() > 1) {
            return Optional.of(path.get().get(1));
        }
        return Optional.empty();
    }

    private Optional<List<Coordinates>> findPath(WorldMap map, Coordinates start,
                                                 Coordinates target, boolean canWalkOnGrass) {
        if (!map.isValidCoordinate(start) || !map.isValidCoordinate(target)) {
            return Optional.empty();
        }

        if (start.equals(target)) {
            List<Coordinates> path = new ArrayList<>();
            path.add(start);
            return Optional.of(path);
        }

        Queue<Coordinates> queue = new LinkedList<>();
        Map<Coordinates, Coordinates> cameFrom = new HashMap<>();
        Set<Coordinates> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);
        cameFrom.put(start, null);

        while (!queue.isEmpty()) {
            Coordinates current = queue.poll();

            if (current.equals(target)) {
                return Optional.of(reconstructPath(cameFrom, start, target));
            }

            for (Coordinates neighbor : getPassableNeighbors(map, current, canWalkOnGrass, null)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    cameFrom.put(neighbor, current);
                }
            }
        }

        return Optional.empty();
    }

    private List<Coordinates> getPassableNeighbors(WorldMap map, Coordinates current,
                                                   boolean canWalkOnGrass,
                                                   Class<? extends Entity> targetType) {
        List<Coordinates> neighbors = new ArrayList<>();

        for (Coordinates neighbor : map.getAdjacentCoordinates(current)) {
            if (!map.isValidCoordinate(neighbor)) continue;

            Entity entity = map.getEntity(neighbor);

            if (targetType != null && targetType.isInstance(entity)) {
                neighbors.add(neighbor);
                continue;
            }

            if (entity == null) {
                neighbors.add(neighbor);
            } else if (entity instanceof Grass && canWalkOnGrass) {
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

    private List<Coordinates> reconstructPath(Map<Coordinates, Coordinates> cameFrom,
                                              Coordinates start, Coordinates target) {
        LinkedList<Coordinates> path = new LinkedList<>();
        Coordinates current = target;

        while (current != null && !current.equals(start)) {
            path.addFirst(current);
            current = cameFrom.get(current);
        }

        path.addFirst(start);
        return path;
    }
}
