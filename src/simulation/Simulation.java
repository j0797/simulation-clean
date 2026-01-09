package simulation;

import actions.*;
import worldmap.WorldMap;
import renderer.Renderer;
import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private final WorldMap map;
    private final Renderer renderer;
    private final List<Action> initActions = new ArrayList<>();
    private final List<Action> turnActions = new ArrayList<>();
    private boolean isPaused  = true;
    private boolean isRunning = false;
    private int turnCount = 0;
    private long turnDelayMs = 1000;
    private Thread simulationThread;

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
    public void startSimulation() {
        if (isRunning) {
            return;
        }

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
        if (isPaused || !isRunning) {
            return;
        }
        for (Action action : turnActions) {
            action.perform(map);
        }

        turnCount++;

        renderer.render(map);

        System.out.println("Ход #" + turnCount);
    }

    public void pauseSimulation() {
        isPaused = true;
    }
    public void resumeSimulation() {
        if (isRunning) {
            isPaused = false;
        }
    }

    public void stopSimulation() {
        isRunning = false;
        isPaused = true;
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

    public void setTurnDelayMs(long delayMs) {
        this.turnDelayMs = delayMs;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public WorldMap getMap() {
        return map;
    }
}