package actions;

import entities.creatures.Creature;
import entities.creatures.Herbivore;
import entities.creatures.Predator;
import worldmap.Coordinates;
import worldmap.WorldMap;
import pathfinder.BreadthFirstSearch;

import java.util.*;

public class CreatureMovement implements Action {
    private final Random random = new Random();
    private final BreadthFirstSearch pathFinder = new BreadthFirstSearch();

    @Override
    public void perform(WorldMap map) {
        Map<Creature, Coordinates> creatures = map.getAllCreatures(map);
        int movedCount = 0;

        List<Creature> creaturesList = new ArrayList<>(creatures.keySet());
        Collections.shuffle(creaturesList);

        for (Creature creature : creaturesList) {
            if (creature.isDead()) continue;

            Coordinates currentPos = creatures.get(creature);
            if (moveCreature(map, creature, currentPos)) {
                movedCount++;
            }
        }

        System.out.println("Передвинуто существ: " + movedCount);
    }

    private boolean moveCreature(WorldMap map, Creature creature, Coordinates currentPos) {
        if (creature.getHunger() > creature.getMaxHunger() / 2) {
            return tryMoveToFood(map, creature, currentPos);
        }
        return moveRandomly(map, currentPos);
    }

    private boolean tryMoveToFood(WorldMap map, Creature creature, Coordinates currentPos) {
        Optional<Coordinates> target = Optional.empty();

        if (creature instanceof Herbivore) {
            target = pathFinder.findNearestGrass(map, currentPos);
        } else if (creature instanceof Predator) {
            target = pathFinder.findNearestHerbivore(map, currentPos);
        }

        if (target.isPresent()) {
            boolean canWalkOnGrass = creature instanceof Herbivore;
            Optional<Coordinates> nextStep = pathFinder.getNextStepToTarget(map, currentPos, target.get(), canWalkOnGrass);

            if (nextStep.isPresent() && map.isEmptyCell(nextStep.get())) {
                return map.moveEntity(currentPos, nextStep.get());
            }
        }

        return moveRandomly(map, currentPos);
    }

    private boolean moveRandomly(WorldMap map, Coordinates currentPos) {
        List<Coordinates> emptyAdjacent = map.getEmptyAdjacentCoordinates(currentPos);
        if (!emptyAdjacent.isEmpty()) {
            Coordinates target = emptyAdjacent.get(random.nextInt(emptyAdjacent.size()));
            return map.moveEntity(currentPos, target);
        }
        return false;
    }
}