package actions.steps;

import actions.Action;
import entities.creatures.Herbivore;
import entities.creatures.Predator;
import entities.objects.Grass;
import worldmap.WorldMap;


public class StatisticsCollection implements Action {
    @Override
    public void perform(WorldMap map) {
        printTurnStatistics(map);
    }

    private void printTurnStatistics(WorldMap map) {
        int herbivoresCount = map.getEntitiesOfType(Herbivore.class).size();
        int predatorsCount = map.getEntitiesOfType(Predator.class).size();
        int grassCount = map.getEntitiesOfType(Grass.class).size();
        int totalCells = map.getWidth() * map.getHeight();
        int occupiedCells = map.getAllEntities().size();
        int occupancyPercent = (occupiedCells * 100) / totalCells;

        System.out.println("Статистика:");
        System.out.println("Травоядные: " + herbivoresCount + " | Хищники: " + predatorsCount + " | Трава: " + grassCount);
        System.out.println("Заполненность: " + occupancyPercent + "%");
    }
}

