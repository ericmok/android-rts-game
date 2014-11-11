package processors;

import java.util.ArrayList;

import model.Behaviors;
import model.DestinationComponent;
import model.Entity;
import model.Engine;
import model.GameEntities;
import model.WorldComponent;
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

        if (command.command == Command.FIRE) {
            for (int j = 0; j < command.selection.size(); j++) {
                Entity projectile = GameEntities.projectilesMemoryPool.fetchMemory();
                Entity caster = command.selection.get(j);

                WorldComponent casterWorld = (WorldComponent) caster.cData.get(WorldComponent.class);
                WorldComponent wc = (WorldComponent) projectile.cData.get(WorldComponent.class);

                wc.pos.copy(casterWorld.pos);

                DestinationComponent dc = (DestinationComponent) projectile.cData.get(DestinationComponent.class);
                dc.dest.x = 3 * (casterWorld.rot.x) + casterWorld.pos.x;
                dc.dest.y = 3 * (casterWorld.rot.y) + casterWorld.pos.y;
                dc.hasDestination = true;

                engine.currentPlayer.queueAdded(projectile);
            }
        }
    }

    public static void interpolate(Engine engine, double ct, double dt) {
        ArrayList<Entity> destinedEntities = engine.currentPlayer.denorms.getListForLabel(Behaviors.BEHAVIOR_MOVES_TOWARD_DESTINATION);
        MoveTowardDestinationFunction.apply(destinedEntities, dt);
        BattleResolution.process(engine, dt);
    }
}
