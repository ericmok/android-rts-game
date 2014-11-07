package processors;

import java.util.ArrayList;

import components.DestinationComponent;
import components.Entity;
import components.Engine;
import components.SelectionComponent;
import networking.Command;
import networking.CommandHistory;

/**
 * Created by eric on 11/6/14.
 */
public class EngineSimulator {

    public static void changeModelWithCommand(Engine engine, Command command) {

        // TODO: Sort command history by timeStamp
        // Replay commands taking into account their timeStamps

        if (command.command == Command.MOVE) {
            for (int j = 0; j < command.selection.size(); j++) {
                Entity entity = command.selection.get(j);

                DestinationComponent dc = (DestinationComponent) entity.cData.get(DestinationComponent.class);
                dc.dest.copy(command.vec);
                dc.hasDestination = true;

                SelectionProcessor.FN_DESELECT.apply(entity);
            }
        }
    }

    public static void interpolate(Engine engine, double ct, double dt) {
        ArrayList<Entity> destinedEntities = engine.currentPlayer.denorms.getListForLabel(Entity.NODE_MOVE_TOWARD_DESTINATION);
        MoveTowardDestinationFunction.apply(destinedEntities, dt);
    }
}
