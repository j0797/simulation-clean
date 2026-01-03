package actions;

import actions.steps.*;
import map.WorldMap;

import java.util.*;

public class TurnManager implements Action {
    private final List<Action> turnActions;

    public TurnManager() {
        this.turnActions = Arrays.asList(
                new CreatureStateUpdate(),
                new CreatureMovement(),
                new CreatureFeeding(),
                new DeathProcessing(),
                new GrassRegrowth(),
                new PopulationControl(),
                new StatisticsCollection()
        );
    }

    @Override
    public void perform(WorldMap map) {
        for (Action action : turnActions) {
            action.perform(map);
        }
    }
}