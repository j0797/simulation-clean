package simulation;

import actions.Action;
import entities.Entity;
import entities.creatures.Herbivore;
import entities.creatures.Predator;
import worldmap.Coordinates;
import worldmap.WorldMap;
import renderer.Renderer;
import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private final WorldMap map;
    private final Renderer renderer;
    private final List<Action> initActions = new ArrayList<>();
    private final List<Action> turnActions = new ArrayList<>();
    private volatile boolean isRunning = false;
    private volatile boolean isPaused = false;
    private int turnCount = 0;
    private long turnDelayMs = 1000;
    private Thread simulationThread;
    private boolean autoRender = false;

    public Simulation(WorldMap map, Renderer renderer) {
        this.map = map;
        this.renderer = renderer;
    }

    public void addInitAction(Action action) {
        initActions.add(action);
    }

    public void addTurnAction(Action action) {
        turnActions.add(action);
    }

    public void setAutoRender(boolean autoRender) {
        this.autoRender = autoRender;
    }

    public void setTurnDelayMs(long delayMs) {
        this.turnDelayMs = delayMs;
    }

    public void startSimulation() {
        if (isRunning) return;

        for (Action action : initActions) {
            action.perform(map);
        }

        isRunning = true;
        isPaused = false;
        turnCount = 0;

        simulationThread = new Thread(this::runSimulationLoop);
        simulationThread.start();
    }

    public void nextTurn() {
        if (!isRunning || isPaused) return;

        for (Action action : turnActions) {
            action.perform(map);
        }

        turnCount++;

        if (shouldStopSimulation()) {
            System.out.println("\n=================================");
            System.out.println("СИМУЛЯЦИЯ ЗАВЕРШЕНА!");
            System.out.println("Причина: " + getStopReason());
            System.out.println("Всего ходов: " + turnCount);
            System.out.println("=================================\n");

            renderer.render(map);
            stopSimulation();
            return;
        }

        if (autoRender) {
            renderer.render(map);
        }

        System.out.println("Ход #" + turnCount);
    }

    public void pauseSimulation() {
        isPaused = true;
    }

    public void resumeSimulation() {
        isPaused = false;
    }

    public void stopSimulation() {
        isRunning = false;
        isPaused = false;
        if (simulationThread != null) {
            simulationThread.interrupt();
        }
    }

    private void runSimulationLoop() {
        while (isRunning) {
            if (!isPaused) {
                nextTurn();
                try {
                    Thread.sleep(turnDelayMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    private boolean shouldStopSimulation() {
        boolean hasHerbivores = false;
        boolean hasPredators = false;

        for (int row = 0; row < map.getHeight(); row++) {
            for (int col = 0; col < map.getWidth(); col++) {
                var entityOpt = map.getEntity(new Coordinates(row, col));

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
                var entityOpt = map.getEntity(new Coordinates(row, col));

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
}
