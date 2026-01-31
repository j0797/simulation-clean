package simulation;

import entities.creatures.Herbivore;
import entities.creatures.Predator;
import entities.objects.Grass;
import entities.objects.Rock;
import entities.objects.Tree;
import worldmap.WorldMap;

public class SimulationStatistics {
    private final WorldMap map;
    private int turnCounter;
    private long startTime;
    private long endTime;
    private int initialTrees;
    private int initialRocks;
    private int initialGrass;
    private int initialHerbivores;
    private int initialPredators;

    public SimulationStatistics(WorldMap map) {
        this.map = map;
        this.turnCounter = 0;
    }

    public void setInitialStats(int trees, int rocks, int grass, int herbivores, int predators) {
        this.initialTrees = trees;
        this.initialRocks = rocks;
        this.initialGrass = grass;
        this.initialHerbivores = herbivores;
        this.initialPredators = predators;
    }

    public void printInitialStats() {
        System.out.println("Начальная статистика:");
        System.out.println("   Размер мира: " + map.getWidth() + "x" + map.getHeight());
        System.out.println("   Деревья: " + initialTrees);
        System.out.println("   Камни: " + initialRocks);
        System.out.println("   Трава: " + initialGrass);
        System.out.println("   Травоядные: " + initialHerbivores);
        System.out.println("   Хищники: " + initialPredators);
        System.out.println();
    }

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void stop() {
        endTime = System.currentTimeMillis();
    }

    public void incrementTurnCounter() {
        turnCounter++;
    }

    public int getTurnCounter() {
        return turnCounter;
    }

    public void printQuickStats() {
        int herbivores = map.getEntitiesOfType(Herbivore.class).size();
        int predators = map.getEntitiesOfType(Predator.class).size();
        int grass = map.getEntitiesOfType(Grass.class).size();

        System.out.printf("Травоядные: %d | Хищники: %d | Трава: %d%n", herbivores, predators, grass);
    }

    public void printFinalStatistics(long turnDelayMs) {

        System.out.println("\nИТОГОВАЯ СТАТИСТИКА");
        System.out.printf("Всего ходов: %d%n", turnCounter);
        System.out.printf("Размер мира: %d×%d%n", map.getWidth(), map.getHeight());
        long duration = endTime - startTime;
        System.out.printf("Время симуляции: ~%d секунд%n", duration / 1000);
        System.out.printf("Среднее время на ход: %.1f мс%n",
                turnCounter > 0 ? (double) duration / turnCounter : 0);

        int finalHerbivores = map.getEntitiesOfType(Herbivore.class).size();
        int finalPredators = map.getEntitiesOfType(Predator.class).size();
        int finalGrass = map.getEntitiesOfType(Grass.class).size();
        int finalTrees = map.getEntitiesOfType(Tree.class).size();
        int finalRocks = map.getEntitiesOfType(Rock.class).size();
        int totalCells = map.getWidth() * map.getHeight();
        int occupiedCells = map.getAllEntities().size();
        double occupancyPercent = (occupiedCells * 100.0) / totalCells;

        System.out.println("\nФИНАЛЬНОЕ СОСТОЯНИЕ:");
        System.out.printf("  Деревья: %d (было %d)%n", finalTrees, initialTrees);
        System.out.printf("  Камни: %d (было %d)%n", finalRocks, initialRocks);
        System.out.printf("  Трава: %d (было %d)%n", finalGrass, initialGrass);
        System.out.printf("  Травоядных: %d (было %d)%n", finalHerbivores, initialHerbivores);
        System.out.printf("  Хищников: %d (было %d)%n", finalPredators, initialPredators);
        System.out.printf("  Заполненность карты: %.1f%%%n", occupancyPercent);

        if (initialHerbivores > 0) {
            double herbivoreChange = ((double) (finalHerbivores - initialHerbivores) / initialHerbivores) * 100;
            System.out.printf("  Изменение травоядных: %+.1f%%%n", herbivoreChange);
        }
        if (initialPredators > 0) {
            double predatorChange = ((double) (finalPredators - initialPredators) / initialPredators) * 100;
            System.out.printf("  Изменение хищников: %+.1f%%%n", predatorChange);
        }

        System.out.println("\nИТОГИ:");
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

    public boolean shouldStop() {
        return map.getEntitiesOfType(Herbivore.class).isEmpty() ||
                map.getEntitiesOfType(Predator.class).isEmpty();
    }

    public int getHerbivoreCount() {
        return map.getEntitiesOfType(Herbivore.class).size();
    }

    public int getPredatorCount() {
        return map.getEntitiesOfType(Predator.class).size();
    }

    public int getGrassCount() {
        return map.getEntitiesOfType(Grass.class).size();
    }

    public double getOccupancyPercentage() {
        int totalCells = map.getWidth() * map.getHeight();
        int occupiedCells = map.getAllEntities().size();
        return (occupiedCells * 100.0) / totalCells;
    }

}
