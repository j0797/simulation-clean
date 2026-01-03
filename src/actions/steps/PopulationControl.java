package actions.steps;

import actions.Action;
import entities.creatures.Herbivore;
import entities.creatures.Predator;
import map.Coordinates;
import map.WorldMap;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class PopulationControl implements Action {
    private final Random random = new Random();
    private static final int MIN_HERBIVORES = 3;
    private static final int MIN_PREDATORS = 1;
    private static final double HERBIVORE_ADD_CHANCE = 0.05;
    private static final double PREDATOR_ADD_CHANCE = 0.02;

    @Override
    public void perform(WorldMap map) {
        int herbivoresAdded = tryAddHerbivores(map);
        int predatorsAdded = tryAddPredators(map);

        if (herbivoresAdded > 0 || predatorsAdded > 0) {
            System.out.println("Добавлено: " + herbivoresAdded + " травоядных, " + predatorsAdded + " хищников");
        }
    }

    private int tryAddHerbivores(WorldMap map) {
        List<Herbivore> herbivores = map.getEntitiesOfType(Herbivore.class);
        if (herbivores.size() >= MIN_HERBIVORES) return 0;
        if (random.nextDouble() >= HERBIVORE_ADD_CHANCE) return 0;

        Optional<Coordinates> coordinates = map.getRandomEmptyCoordinate(random);
        if (coordinates.isPresent() && map.placeEntity(coordinates.get(), new Herbivore())) {
            return 1;
        }
        return 0;
    }

    private int tryAddPredators(WorldMap map) {
        List<Predator> predators = map.getEntitiesOfType(Predator.class);
        if (predators.size() >= MIN_PREDATORS) return 0;
        if (random.nextDouble() >= PREDATOR_ADD_CHANCE) return 0;

        Optional<Coordinates> coordinates = map.getRandomEmptyCoordinate(random);
        if (coordinates.isPresent() && map.placeEntity(coordinates.get(), new Predator())) {
            return 1;
        }
        return 0;
    }
}