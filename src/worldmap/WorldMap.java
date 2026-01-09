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

    public void putEntity(Coordinates coordinates, Entity entity) {
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
        if (!isWithinBounds(coordinates)) {
            throw new IllegalArgumentException(String.format("Координаты [%d, %d] вне границ карты %dx%d",coordinates.row(), coordinates.column(), height, width));
        }
    }

    public boolean isWithinBounds(Coordinates coordinates) {
        return coordinates.row() >= 0 && coordinates.row() < height &&
                coordinates.column() >= 0 && coordinates.column() < width;
    }
}