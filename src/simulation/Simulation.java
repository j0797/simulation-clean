package simulation;

import actions.Action;
import worldmap.WorldMap;
import worldmap.Coordinates;
import entities.Entity;
import entities.creatures.Herbivore;
import entities.creatures.Predator;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private final WorldMap map;
    private final List<Action> initActions = new ArrayList<>();
    private final List<Action> turnActions = new ArrayList<>();
    private boolean isRunning = false;
    private boolean isPaused = false;
    private int turnCount = 0;
    private final List<SimulationListener> listeners = new ArrayList<>();

    public interface SimulationListener {
        void onSimulationStarted();

        void onTurnCompleted(int turnNumber);

        void onSimulationStopped(String reason);

        void onEntityCountChanged(int herbivores, int predators, int grass);
    }

    public Simulation(WorldMap map) {
        this.map = map;
    }

    public void addInitAction(Action action) {
        initActions.add(action);
    }

    public void addTurnAction(Action action) {
        turnActions.add(action);
    }

    public void addListener(SimulationListener listener) {
        listeners.add(listener);
    }

    public void removeListener(SimulationListener listener) {
        listeners.remove(listener);
    }

    public void start() {
        if (isRunning) return;

        for (Action action : initActions) {
            action.perform(map);
        }

        isRunning = true;
        isPaused = false;
        turnCount = 0;

        listeners.forEach(SimulationListener::onSimulationStarted);
        notifyEntityCount();
    }

    public void nextTurn() {
        if (!isRunning || isPaused) return;

        for (Action action : turnActions) {
            action.perform(map);
        }

        turnCount++;

        listeners.forEach(l -> l.onTurnCompleted(turnCount));
        notifyEntityCount();

        if (shouldStop()) {
            String reason = getStopReason();
            stop();
            listeners.forEach(l -> l.onSimulationStopped(reason));
        }
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    public void stop() {
        isRunning = false;
        isPaused = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public WorldMap getMap() {
        return map;
    }

    private boolean shouldStop() {
        boolean hasHerbivores = false;
        boolean hasPredators = false;

        for (int row = 0; row < map.getHeight(); row++) {
            for (int col = 0; col < map.getWidth(); col++) {
                Coordinates coords = new Coordinates(row, col);
                var entityOpt = map.getEntity(coords);

                if (entityOpt.isPresent()) {
                    Entity entity = entityOpt.get();
                    if (entity instanceof Herbivore) {
                        hasHerbivores = true;
                    } else if (entity instanceof Predator) {
                        hasPredators = true;
                    }
                }
                if (hasHerbivores && hasPredators) {
                    return false;
                }
            }
        }
        return true;
    }

    private String getStopReason() {
        int herbivores = 0;
        int predators = 0;

        for (int row = 0; row < map.getHeight(); row++) {
            for (int col = 0; col < map.getWidth(); col++) {
                Coordinates coords = new Coordinates(row, col);
                var entityOpt = map.getEntity(coords);

                if (entityOpt.isPresent()) {
                    Entity entity = entityOpt.get();
                    if (entity instanceof Herbivore) {
                        herbivores++;
                    } else if (entity instanceof Predator) {
                        predators++;
                    }
                }
            }
        }

        if (herbivores == 0 && predators == 0) {
            return "Все существа вымерли";
        } else if (herbivores == 0) {
            return "Вымерли все травоядные";
        } else if (predators == 0) {
            return "Вымерли все хищники";
        }

        return "Неизвестная причина";
    }

    private void notifyEntityCount() {
        int herbivores = 0, predators = 0, grass = 0;

        for (int row = 0; row < map.getHeight(); row++) {
            for (int col = 0; col < map.getWidth(); col++) {
                Coordinates coords = new Coordinates(row, col);
                var entityOpt = map.getEntity(coords);

                if (entityOpt.isPresent()) {
                    Entity entity = entityOpt.get();
                    String className = entity.getClass().getSimpleName();

                    switch (className) {
                        case "Herbivore" -> herbivores++;
                        case "Predator" -> predators++;
                        case "Grass" -> grass++;
                    }
                }
            }
        }

        int finalHerbivores = herbivores;
        int finalPredators = predators;
        int finalGrass = grass;
        listeners.forEach(l -> l.onEntityCountChanged(finalHerbivores, finalPredators, finalGrass));
    }
}