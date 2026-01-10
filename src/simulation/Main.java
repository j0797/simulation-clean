package simulation;

import worldmap.WorldMap;
import renderer.ConsoleRenderer;
import actions.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== СИМУЛЯЦИЯ 2D МИРА ===");
        System.out.println();


        System.out.print("Введите размер мира (например, 15 для 15x15): ");
        int size = scanner.nextInt();

        WorldMap map = new WorldMap(size, size);
        ConsoleRenderer renderer = new ConsoleRenderer();
        Simulation simulation = new Simulation(map, renderer);

        simulation.addInitAction(new InitWorldAction());
        simulation.addTurnAction(new CreatureMoveAction());
        simulation.addTurnAction(new GrassGrowthAction());

        System.out.print("Показывать карту каждый ход? (1 - да, 0 - нет): ");
        int showMap = scanner.nextInt();
        simulation.setAutoRender(showMap == 1);

        System.out.print("Задержка между ходами (мс): ");
        long delay = scanner.nextLong();
        simulation.setTurnDelayMs(delay);

        System.out.println("\n=== ЗАПУСК СИМУЛЯЦИИ ===");
        simulation.startSimulation();


        while (simulation.isRunning()) {
            System.out.println("\nКоманды: p - пауза, r - возобновить, s - статистика, q - выход");
            System.out.print("Введите команду: ");
            String command = scanner.next().toLowerCase();

            switch (command) {
                case "p":
                    simulation.pauseSimulation();
                    System.out.println("Симуляция на паузе");
                    break;
                case "r":
                    simulation.resumeSimulation();
                    System.out.println("Симуляция возобновлена");
                    break;
                case "s":
                    showStatistics(simulation);
                    break;
                case "q":
                    System.out.println("Завершение симуляции...");
                    simulation.stopSimulation();
                    break;
                default:
                    System.out.println("Неизвестная команда");
            }

            if (command.equals("q")) break;
        }

        scanner.close();
        System.out.println("\nСимуляция завершена.");
    }

    private static void showStatistics(Simulation simulation) {
        System.out.println("\n=== СТАТИСТИКА ===");
        System.out.println("Текущий ход: " + simulation.getTurnCount());
        System.out.println("Статус: " +
                (simulation.isRunning() ?
                        (simulation.isPaused() ? "На паузе" : "Выполняется") :
                        "Остановлена"));
    }
}