package simulation;


public class Main {
    public static void main(String[] args) {

        int width = 10;
        int height = 10;
        long turnDelayMs = 1000;

        Simulation simulation = new Simulation(width, height, turnDelayMs);
        simulation.startSimulation();
    }
}