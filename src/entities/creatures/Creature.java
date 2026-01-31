package entities.creatures;

import entities.Entity;
import pathfinder.BreadthFirstSearch;
import pathfinder.Path;
import worldmap.Coordinates;
import worldmap.WorldMap;

import java.util.*;


public abstract class Creature extends Entity {
    protected int healthPoints;
    protected int speed;
    protected int hunger;
    protected int maxHunger;
    protected final Random random = new Random();

    private static final Path pathFinder = new BreadthFirstSearch();

    public Creature(int speed, int maxHunger, int healthPoints) {
        this.speed = speed;
        this.maxHunger = maxHunger;
        this.hunger = 0;
        this.healthPoints = healthPoints;
    }

    public abstract Coordinates makeMove(WorldMap worldMap, Coordinates currentPos);

    public void updateState() {
        hunger++;
        if (hunger > maxHunger) {
            healthPoints--;
        }
    }

    public void consumeFood() {
        hunger = 0;
        healthPoints++;
    }

    public boolean isAlive() {
        return healthPoints > 0;
    }

    public boolean isDead() {
        return healthPoints <= 0;
    }

    public void takeDamage(int damage) {
        this.healthPoints = Math.max(0, this.healthPoints - damage);
    }

    protected Optional<Coordinates> findPathToTarget(WorldMap map,
                                                     Coordinates current,
                                                     Class<? extends Entity> targetType) {
        List<Coordinates> path = pathFinder.findPath(map, current, targetType);

        if (path.size() > 1) {
            return Optional.of(path.get(1));
        }
        return Optional.empty();
    }

    protected Optional<Coordinates> findAdjacentEntityCoordinates(
            WorldMap worldMap, Coordinates currentPos, Class<? extends Entity> entityType) {

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;

                Coordinates check = new Coordinates(currentPos.row() + dr, currentPos.column() + dc);

                if (worldMap.isValidCoordinate(check)) {
                    Optional<Entity> entity = worldMap.getEntity(check);
                    if (entity.isPresent() && entityType.isInstance(entity.get())) {
                        return Optional.of(check);
                    }
                }
            }
        }
        return Optional.empty();
    }

    protected <T extends Entity> Optional<T> findAdjacentEntity(
            WorldMap worldMap, Coordinates currentPos, Class<T> entityType) {

        Optional<Coordinates> coordinates = findAdjacentEntityCoordinates(worldMap, currentPos, entityType);
        if (coordinates.isPresent()) {
            Optional<Entity> entity = worldMap.getEntity(coordinates.get());
            if (entity.isPresent() && entityType.isInstance(entity.get())) {
                return Optional.of(entityType.cast(entity.get()));
            }
        }
        return Optional.empty();
    }

    protected Coordinates getRandomMove(WorldMap worldMap, Coordinates currentPos) {
        List<Coordinates> possibleMoves = getAvailableMoves(worldMap, currentPos);

        if (!possibleMoves.isEmpty()) {
            return possibleMoves.get(random.nextInt(possibleMoves.size()));
        }

        return currentPos;
    }

    protected List<Coordinates> getAvailableMoves(WorldMap worldMap, Coordinates currentPos) {
        List<Coordinates> availableMoves = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            Coordinates newPos = new Coordinates(
                    currentPos.row() + dir[0],
                    currentPos.column() + dir[1]
            );

            if (worldMap.isValidCoordinate(newPos) && worldMap.getEntity(newPos).isEmpty()) {
                availableMoves.add(newPos);
            }
        }

        return availableMoves;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getSpeed() {
        return speed;
    }

    public int getHunger() {
        return hunger;
    }

    public int getMaxHunger() {
        return maxHunger;
    }
}