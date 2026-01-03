package map;

import entities.Entity;

import java.util.*;
import java.util.stream.Collectors;

public class WorldMap {
    private final int height;
    private final int width;
    private final Map<Coordinates, Entity> entities = new HashMap<>();
    private final Map<Class<? extends Entity>, Set<Coordinates>> entityIndex = new HashMap<>();

    public WorldMap(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public boolean placeEntity(Coordinates coordinates, Entity entity) {

        Entity existing = entities.get(coordinates);
        if (existing != null) {
            removeEntityFromIndex(existing.getClass(), coordinates);
        }
        entities.put(coordinates, entity);
        entityIndex.computeIfAbsent(entity.getClass(), k -> new HashSet<>())
                .add(coordinates);
        return true;
    }

    public boolean moveEntity(Coordinates from, Coordinates to) {
        if (!isValidCoordinate(from) || !isValidCoordinate(to)) return false;
        if (from.equals(to)) return false;

        Entity entity = entities.get(from);
        if (entity == null) return false;

        if (!isEmptyCell(to)) {
            return false;
        }
        removeEntityFromIndex(entity.getClass(), from);

        entities.remove(from);
        entities.put(to, entity);

        entityIndex.computeIfAbsent(entity.getClass(), k -> new HashSet<>())
                .add(to);
        return true;
    }

    public Entity getEntity(Coordinates coordinates) {
        return entities.get(coordinates);
    }

    public void removeEntity(Coordinates coordinates) {
        Entity entity = entities.remove(coordinates);
        if (entity != null) {
            removeEntityFromIndex(entity.getClass(), coordinates);
        }
    }

    private void removeEntityFromIndex(Class<? extends Entity> entityClass, Coordinates coordinates) {
        Set<Coordinates> coordsSet = entityIndex.get(entityClass);
        if (coordsSet != null) {
            coordsSet.remove(coordinates);

            if (coordsSet.isEmpty()) {
                entityIndex.remove(entityClass);
            }
        }
    }

    public boolean isEmptyCell(Coordinates coordinates) {
        return !entities.containsKey(coordinates);
    }

    public boolean isValidCoordinate(Coordinates coordinates) {
        return coordinates.row() >= 0 && coordinates.row() < height &&
                coordinates.column() >= 0 && coordinates.column() < width;
    }

    public List<Entity> getAllEntities() {
        return new ArrayList<>(entities.values());
    }

    public <T extends Entity> List<T> getEntitiesOfType(Class<T> type) {
        Set<Coordinates> coords = entityIndex.getOrDefault(type, Collections.emptySet());
        return coords.stream()
                .map(entities::get)
                .map(type::cast)
                .collect(Collectors.toList());
    }

    public Optional<Coordinates> getRandomEmptyCoordinate(Random random) {
        List<Coordinates> empty = new ArrayList<>();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Coordinates coord = new Coordinates(row, col);
                if (isEmptyCell(coord)) empty.add(coord);
            }
        }
        if (empty.isEmpty()) return Optional.empty();
        return Optional.of(empty.get(random.nextInt(empty.size())));
    }

    public List<Coordinates> getAdjacentCoordinates(Coordinates coordinates) {
        List<Coordinates> adjacent = new ArrayList<>();
        int row = coordinates.row();
        int col = coordinates.column();

        if (row > 0) adjacent.add(new Coordinates(row - 1, col));
        if (row < height - 1) adjacent.add(new Coordinates(row + 1, col));
        if (col > 0) adjacent.add(new Coordinates(row, col - 1));
        if (col < width - 1) adjacent.add(new Coordinates(row, col + 1));

        return adjacent;
    }

    public List<Coordinates> getEmptyAdjacentCoordinates(Coordinates coordinates) {
        List<Coordinates> empty = new ArrayList<>();
        for (Coordinates coord : getAdjacentCoordinates(coordinates)) {
            if (isEmptyCell(coord)) {
                empty.add(coord);
            }
        }
        return empty;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}