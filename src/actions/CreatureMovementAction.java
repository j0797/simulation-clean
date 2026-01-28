package actions;

import entities.creatures.Creature;
import worldmap.Coordinates;
import worldmap.WorldMap;

import java.util.Map;

public class CreatureMovementAction implements Action {
    @Override
    public void perform(WorldMap map) {
        Map<Creature, Coordinates> creatures = WorldMap.getAllCreatures(map);
        int moved = 0;

        for (var entry : creatures.entrySet()) {
            Creature creature = entry.getKey();
            Coordinates currentPos = entry.getValue();

            if (!creature.isDead()) {
                creature.makeMove(map);
                moved++;
            }
        }

        System.out.printf("Передвинуто существ: %d%n", moved);
    }
}