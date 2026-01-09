package simulation;

import worldmap.WorldMap;
import renderer.ConsoleRenderer;
import actions.*;

public class
Main {
    public static void main(String[] args) {
        WorldMap map = new WorldMap(10, 10);
        ConsoleRenderer renderer = new ConsoleRenderer();
        Simulation simulation = new Simulation(map, renderer);


        simulation.addInitAction(new InitWorldAction());
        simulation.addTurnAction(new CreatureMoveAction());
        simulation.addTurnAction(new GrassGrowthAction());

        simulation.setTurnDelayMs(1000);
        simulation.startSimulation();

        new Thread(() -> {
            try {
                Thread.sleep(20000); // 20 секунд
                simulation.pauseSimulation();
                System.out.println("Симуляция завершена после 20 ходов");
                simulation.stopSimulation();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}