package simulation;

import actions.*;
import worldmap.WorldMap;
import renderer.ConsoleRenderer;

import java.util.*;

public class Simulation {
    private final ConsoleRenderer renderer;
    private Scanner scanner;
    private boolean isRunning;
    private boolean isPaused;
    private final long turnDelayMs;
    private final List<Action> initActions;
    private final List<Action> turnActions;
    private final WorldMap map;
    private final SimulationStatistics statistics;

    public Simulation(int width, int height) {
        this(width, height, 1500);
    }

    public Simulation(int width, int height, long turnDelayMs) {
        this.map = new WorldMap(height, width);
        this.renderer = new ConsoleRenderer();
        this.turnDelayMs = turnDelayMs;
        this.isRunning = false;
        this.isPaused = false;
        this.initActions = createInitActions();
        this.turnActions = createTurnActions();
        this.statistics = new SimulationStatistics(map);

        initializeWorld();
    }

    private void initializeWorld() {

        for (Action action : initActions) {
            action.perform(map);
        }

        System.out.println("Начальное состояние мира:");
        renderer.render(map);
        System.out.println("Инициализация завершена.");
    }

    public void startSimulation() {
        if (isRunning) {
            System.out.println("Симуляция уже запущена!");
            return;
        }

        isRunning = true;
        scanner = new Scanner(System.in);
        statistics.start();

        System.out.println("\nСИМУЛЯЦИЯ ЗАПУЩЕНА");
        System.out.println();
        System.out.printf("Задержка между ходами: %d мс%n", turnDelayMs);
        System.out.println();

        Thread commandThread = new Thread(this::handleUserCommands);
        commandThread.setDaemon(true);
        commandThread.start();

        while (isRunning) {
            if (!isPaused) {
                nextTurn();
                printCommandsMenu();
            }

            try {
                Thread.sleep(turnDelayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Симуляция прервана пользователем");
                stopSimulation();
                return;
            }

            if (statistics.shouldStop()) {
                stopSimulation();
            }
        }
    }

    private void printCommandsMenu() {
        System.out.println("\nКоманды: p - пауза, r - возобновить, q - выход");
        System.out.print("Введите команду: ");
    }

    private void handleUserCommands() {
        while (isRunning) {
            try {
                String command = scanner.nextLine().toLowerCase().trim();
                if (command.isEmpty()) continue;
                switch (command) {
                    case "p":
                        pauseSimulation();
                        System.out.println("Симуляция на паузе. Введите 'r' для продолжения или 'q' для выхода.");
                        break;
                    case "r":
                        resumeSimulation();
                        System.out.println("Симуляция продолжена");
                        break;
                    case "q":
                        System.out.println("Завершение симуляции...");
                        stopSimulation();
                        break;
                    default:
                        System.out.println("Некорректная команда! Используйте: p, r, q");
                }
            } catch (Exception e) {
            }
        }
    }

    public void pauseSimulation() {
        if (!isRunning) {
            System.out.println("Симуляция не запущена!");
            return;
        }
        isPaused = true;
    }

    public void resumeSimulation() {
        if (!isRunning) {
            System.out.println("Симуляция не запущена!");
            return;
        }
        isPaused = false;
    }

    public void stopSimulation() {
        if (!isRunning) {
            System.out.println("Симуляция не запущена!");
            return;
        }
        isRunning = false;
        statistics.stop();
        statistics.printFinalStatistics(turnDelayMs);
    }

    private List<Action> createInitActions() {
        return List.of(new InitAction());
    }

    private List<Action> createTurnActions() {
        return List.of(
                new CreatureLifecycleAndFeeding(),
                new CreatureMovement(),
                new EntityRespawn()
        );
    }

    public void nextTurn() {
        statistics.incrementTurnCounter();
        System.out.println();
        System.out.println("ХОД " + statistics.getTurnCounter());
        System.out.println();

        for (Action action : turnActions) {
            action.perform(map);
        }
        renderer.render(map);
        statistics.printQuickStats();
    }

    public int getTurnCounter() {
        return statistics.getTurnCounter();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public WorldMap getMap() {
        return map;
    }

    public void restartSimulation() {
        stopSimulation();

        isRunning = false;
        isPaused = false;

        initializeWorld();
        startSimulation();
    }
}