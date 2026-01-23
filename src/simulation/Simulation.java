package simulation;

import actions.*;
import worldmap.WorldMap;
import renderer.ConsoleRenderer;

import java.util.Scanner;

public class Simulation {
    private final WorldMap map;
    private final ConsoleRenderer renderer;
    private Scanner scanner;
    private final TurnManager turnManager;
    private boolean isRunning;
    private int turnCounter;
    private boolean isPaused;
    private final long turnDelayMs;

    public Simulation(int width, int height) {
        this(width, height, 1500);
    }

    public Simulation(int width, int height, long turnDelayMs) {
        this.map = new WorldMap(height, width);
        this.renderer = new ConsoleRenderer();
        this.turnManager = new TurnManager();
        this.turnDelayMs = turnDelayMs;
        this.isRunning = false;
        this.turnCounter = 0;
        this.isPaused = false;

        initializeWorld();
    }

    private void initializeWorld() {

        InitAction initActions = new InitAction();
        initActions.perform(map);

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

            if (shouldStopSimulation()) {
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
        printFinalStatistics();
    }

    public void nextTurn() {
        turnCounter++;
        System.out.println();
        System.out.println("ХОД " + turnCounter);
        System.out.println();

        turnManager.perform(map);

        renderer.render(map);

        printQuickStats();
    }

    private void printQuickStats() {
        int herbivores = map.getEntitiesOfType(entities.creatures.Herbivore.class).size();
        int predators = map.getEntitiesOfType(entities.creatures.Predator.class).size();
        int grass = map.getEntitiesOfType(entities.objects.Grass.class).size();

        System.out.printf("Травоядные: %d | Хищники: %d | Трава: %d%n", herbivores, predators, grass);
    }

    private boolean shouldStopSimulation() {
        return map.getEntitiesOfType(entities.creatures.Herbivore.class).isEmpty() || map.getEntitiesOfType(entities.creatures.Predator.class).isEmpty();
    }

    private void printFinalStatistics() {

        System.out.println("          ИТОГОВАЯ СТАТИСТИКА");


        System.out.printf("Всего ходов: %d%n", turnCounter);
        System.out.printf("Размер мира: %d×%d%n", map.getWidth(), map.getHeight());
        System.out.printf("Время симуляции: ~%d секунд%n", turnCounter * turnDelayMs / 1000);

        int finalHerbivores = map.getEntitiesOfType(entities.creatures.Herbivore.class).size();
        int finalPredators = map.getEntitiesOfType(entities.creatures.Predator.class).size();
        int finalGrass = map.getEntitiesOfType(entities.objects.Grass.class).size();
        int totalCells = map.getWidth() * map.getHeight();
        int occupiedCells = map.getAllEntities().size();
        double occupancyPercent = (occupiedCells * 100.0) / totalCells;

        System.out.println("ФИНАЛЬНОЕ СОСТОЯНИЕ:");
        System.out.printf("  Травоядных: %d%n", finalHerbivores);
        System.out.printf("  Хищников: %d%n", finalPredators);
        System.out.printf("  Травы: %d%n", finalGrass);
        System.out.printf("  Заполненность карты: %.1f%%%n", occupancyPercent);

        System.out.println("ИТОГИ:");
        if (finalHerbivores == 0 && finalPredators == 0) {
            System.out.println("Все существа вымерли!");
        } else if (finalHerbivores == 0) {
            System.out.println("Травоядные вымерли, хищникам нечего есть");
        } else if (finalPredators == 0) {
            System.out.println("Хищники вымерли, популяция травоядных не контролируется");
        } else {
            double ratio = (double) finalHerbivores / finalPredators;
            if (ratio > 10) {
                System.out.printf("Дисбаланс: слишком много травоядных (%.1f:1)%n", ratio);
            } else if (ratio < 2) {
                System.out.printf("Дисбаланс: слишком много хищников (%.1f:1)%n", ratio);
            } else {
                System.out.printf("Сбалансированная экосистема (%.1f:1)%n", ratio);
            }
        }
    }

    public int getTurnCounter() {
        return turnCounter;
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

        turnCounter = 0;
        isRunning = false;
        isPaused = false;

        initializeWorld();
        startSimulation();
    }
}