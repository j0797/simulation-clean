package pathfinder;

import entities.Entity;
import worldmap.Coordinates;
import worldmap.WorldMap;

import java.util.*;

public class BreadthFirstSearch implements Path {
    public BreadthFirstSearch() {
    }

    @Override
    public List<Coordinates> findPath(WorldMap map, Coordinates start, Class<? extends Entity> targetType) {
        if (!validateInput(map, start, targetType)) {
            return Collections.emptyList();
        }
        Optional<Entity> startEntity = map.getEntity(start);
        if (startEntity.isPresent() && targetType.isInstance(startEntity.get())) {
            return List.of(start);
        }
        Queue<Coordinates> queue = new LinkedList<>();
        Set<Coordinates> visited = new HashSet<>();
        Map<Coordinates, Coordinates> cameFrom = new HashMap<>();

        queue.offer(start);
        visited.add(start);
        cameFrom.put(start, null);

        while (!queue.isEmpty()) {
            Coordinates current = queue.poll();

            for (Coordinates neighbor : getNeighbors(current, map)) {
                if (visited.contains(neighbor)) {
                    continue;
                }

                Optional<Entity> neighborEntity = map.getEntity(neighbor);

                if (neighborEntity.isPresent() && targetType.isInstance(neighborEntity.get())) {
                    cameFrom.put(neighbor, current);
                    return reconstructPath(cameFrom, neighbor);
                }


                if (isPassable(neighbor, map)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                    cameFrom.put(neighbor, current);
                }
            }
        }
        return Collections.emptyList();
    }

    private boolean validateInput(WorldMap map, Coordinates start, Class<? extends Entity> target) {
        return map != null && start != null && target != null && map.isValidCoordinate(start);
    }

    private List<Coordinates> getNeighbors(Coordinates coord, WorldMap map) {
        List<Coordinates> neighbors = new ArrayList<>();
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        for (int i = 0; i < 4; i++) {
            Coordinates neighbor = new Coordinates(coord.row() + dr[i], coord.column() + dc[i]);
            if (map.isValidCoordinate(neighbor)) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    private boolean isPassable(Coordinates coord, WorldMap map) {
        return map.getEntity(coord).isEmpty();
    }

    private List<Coordinates> reconstructPath(Map<Coordinates, Coordinates> cameFrom, Coordinates current) {
        List<Coordinates> path = new ArrayList<>();
        while (current != null) {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);
        return path;
    }
}
