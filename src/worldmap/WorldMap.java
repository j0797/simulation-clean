package worldmap;

import entities.Entity;

import java.util.*;
import java.util.stream.Collectors;

public class WorldMap {
    private final int height;
    private final int width;
    private final Map<Coordinates, Entity> entities = new HashMap<>();

    public WorldMap(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public void placeEntity(Coordinates coordinates, Entity entity) {
        validate(coordinates);
        entities.put(coordinates, entity);
    }

    public Optional<Entity> getEntity(Coordinates coordinates) {
        return Optional.ofNullable(entities.get(coordinates));
    }

    public Collection<Entity> getAllEntities() {
        return Collections.unmodifiableCollection(entities.values());
    }

    public boolean removeEntity(Coordinates coordinates) {
        validate(coordinates);
        return entities.remove(coordinates) != null;
    }

    private void validate(Coordinates coordinates) {
        if (!isValidCoordinate(coordinates)) {
            throw new IllegalArgumentException(String.format("Координаты [%d, %d] вне границ карты %dx%d", coordinates.row(), coordinates.column(), height, width));
        }
    }

    public boolean isValidCoordinate(Coordinates coordinates) {
        return coordinates.row() >= 0 && coordinates.row() < height &&
                coordinates.column() >= 0 && coordinates.column() < width;
    }

    public <T extends Entity> List<T> getEntitiesOfType(Class<T> entityType) {
        return entities.values().stream()
                .filter(entityType::isInstance)
                .map(entityType::cast)
                .collect(Collectors.toList());
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}