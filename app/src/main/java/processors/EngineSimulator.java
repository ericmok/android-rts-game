package processors;

import java.util.ArrayList;

import model.DestinationComponent;
import model.Entity;
import model.Engine;
import networking.Command;

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
        BattleResolution.process(engine, dt);
    }
}
