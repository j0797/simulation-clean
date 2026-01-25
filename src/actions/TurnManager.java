package actions;

import actions.steps.*;
import worldmap.WorldMap;

import java.util.*;

public class TurnManager implements Action {
    private final List<Action> turnActions;

    public TurnManager() {
        this.turnActions = Arrays.asList(
                new CreatureLifecycleAndFeeding(),
                new CreatureMovement(),
                new GrassRegrowth(),
                new PopulationControl()
        );
    }

    @Override
    public void perform(WorldMap map) {
        for (Action action : turnActions) {
            action.perform(map);
        }
    }
}