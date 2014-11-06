package processors;

import java.util.ArrayList;

import components.Entity;
import components.Engine;
import networking.CommandHistory;

/**
 * Created by eric on 11/6/14.
 */
public class EngineSimulator {
    public static void simulate(Engine engine, CommandHistory commandHistory, double dt) {
        ArrayList<Entity> destinedEntities = engine.currentPlayer.denorms.getListForLabel(Entity.NODE_MOVE_TOWARD_DESTINATION);
        MoveTowardDestinationFunction.apply(destinedEntities, dt);
    }
}
