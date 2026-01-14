package simulation;

import worldmap.WorldMap;
import renderer.ConsoleRenderer;
import actions.*;

import java.util.Scanner;

public class SimulationLauncher {
    private Simulation simulation;
    private SimulationStatistics statistics;
    private ConsoleRenderer renderer;
    private Scanner scanner;
    private long turnDelayMs = 1000;
    private Thread simulationThread;
    private boolean launcherRunning = false;
    private boolean autoRender = false;
    private WorldMap map;

    public void launch() {
        scanner = new Scanner(System.in);

        System.out.println("=== СИМУЛЯЦИЯ 2D МИРА ===");
        System.out.println();
        System.out.print("Введите размер мира (например, 15 для 15x15): ");
        int size = scanner.nextInt();
        map = new WorldMap(size, size);
        renderer = new ConsoleRenderer();
        simulation = new Simulation(map);

        simulation.addInitAction(new InitWorldAction());
        simulation.addTurnAction(new CreatureMoveAction());
        simulation.addTurnAction(new GrassGrowthAction());

        System.out.print("Показывать карту каждый ход? (1 - да, 0 - нет): ");
        int showMap = scanner.nextInt();
        autoRender = (showMap == 1);

        System.out.print("Задержка между ходами (мс): ");
        turnDelayMs = scanner.nextLong();

        statistics = new SimulationStatistics(simulation);
        simulation.addListener(statistics);

        if (autoRender) {
            simulation.addListener(new Simulation.SimulationListener() {
                @Override
                public void onSimulationStarted() {
                    renderer.render(map);
                }

                @Override
                public void onTurnCompleted(int turnNumber) {
                    System.out.println("Ход #" + turnNumber);
                    renderer.render(map);
                }

                @Override
                public void onSimulationStopped(String reason) {
                    System.out.println("\n=================================");
                    System.out.println("СИМУЛЯЦИЯ ЗАВЕРШЕНА!");
                    System.out.println("Причина: " + reason);
                    System.out.println("Всего ходов: " + simulation.getTurnCount());
                    System.out.println("=================================\n");
                    renderer.render(map);
                    launcherRunning = false;
                }

                @Override
                public void onEntityCountChanged(int herbivores, int predators, int grass) {
                }
            });
        } else {
            simulation.addListener(new Simulation.SimulationListener() {
                @Override
                public void onSimulationStopped(String reason) {
                    System.out.println("\n=================================");
                    System.out.println("СИМУЛЯЦИЯ ЗАВЕРШЕНА!");
                    System.out.println("Причина: " + reason);
                    System.out.println("Всего ходов: " + simulation.getTurnCount());
                    System.out.println("=================================\n");
                    launcherRunning = false;
                }

                @Override
                public void onSimulationStarted() {
                }

                @Override
                public void onTurnCompleted(int turnNumber) {
                }

                @Override
                public void onEntityCountChanged(int herbivores, int predators, int grass) {
                }
            });
        }

        System.out.println("\n=== ЗАПУСК СИМУЛЯЦИИ ===");
        startSimulation();

        handleUserCommands();

        scanner.close();
        System.out.println("\nСимуляция завершена.");
    }

    private void startSimulation() {
        simulation.start();
        launcherRunning = true;
        simulationThread = new Thread(() -> {
            while (launcherRunning && simulation.isRunning()) {
                if (!simulation.isPaused()) {
                    simulation.nextTurn();
                    try {
                        Thread.sleep(turnDelayMs);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    if (!simulation.isRunning()) {
                        launcherRunning = false;
                        break;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        });
        simulationThread.start();
    }

    private void handleUserCommands() {
        while (launcherRunning) {
            System.out.println("\nКоманды: p - пауза, r - возобновить, s - статистика, q - выход");
            System.out.print("Введите команду: ");
            String command = scanner.next().toLowerCase();

            switch (command) {
                case "p":
                    simulation.pause();
                    System.out.println("Симуляция на паузе");
                    break;
                case "r":
                    simulation.resume();
                    System.out.println("Симуляция возобновлена");
                    break;
                case "s":
                    statistics.displayCurrentStats();
                    break;
                case "q":
                    System.out.println("Завершение симуляции...");
                    stopSimulation();
                    break;
                default:
                    System.out.println("Некорректная команда!");
            }
        }
    }

    private void stopSimulation() {
        launcherRunning = false;
        simulation.stop();
        if (simulationThread != null) {
            simulationThread.interrupt();
        }
    }
}