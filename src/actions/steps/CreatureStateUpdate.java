package actions.steps;

import actions.Action;
import entities.creatures.Creature;
import worldmap.Coordinates;
import worldmap.WorldMap;

import java.util.Map;

public class CreatureStateUpdate implements Action {
    @Override
    public void perform(WorldMap map) {
        Map<Creature, Coordinates> creatures = MapUtils.getAllCreatures(map);
        for (Creature creature : creatures.keySet()) {
            if (!creature.isDead()) {
                creature.updateState();
            }
        }
    }
}