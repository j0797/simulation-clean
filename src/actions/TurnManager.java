package actions;

import actions.steps.*;
import worldmap.WorldMap;

import java.util.*;

public class TurnManager implements Action {
    private final List<Action> turnActions;

    public TurnManager() {
        this.turnActions = Arrays.asList(
                new CreatureLifecycle(),
                new CreatureMovement(),
                new CreatureFeeding(),
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