package simulation;


import worldmap.WorldMap;

public class Main {
    public static void main(String[] args) {
        WorldMap map = new WorldMap(10, 10);
        Simulation simulation = new Simulation(map, 1000);
        simulation.startSimulation();
    }
}