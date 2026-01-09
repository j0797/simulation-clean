package simulation;

import worldmap.WorldMap;
import renderer.ConsoleRenderer;
import actions.*;

public class Main {
    public static void main(String[] args) {
        WorldMap map = new WorldMap(15, 15);
        ConsoleRenderer renderer = new ConsoleRenderer();
        Simulation simulation = new Simulation(map, renderer);


        simulation.addInitAction(new InitWorldAction());
        simulation.addTurnAction(new CreatureMoveAction());
        simulation.addTurnAction(new GrassGrowthAction());


        simulation.startSimulation();


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        simulation.pauseSimulation();
        System.out.println("Симуляция на паузе. Нажмите Enter для завершения...");

        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        simulation.stopSimulation();
    }
}
