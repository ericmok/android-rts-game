package processors;

import java.util.ArrayList;

import model.Behaviors;
import model.DestinationComponent;
import model.Entity;
import model.Engine;
import model.ProjectileCasterComponent;
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
            }
        }

        if (command.command == Command.FIRE) {
            for (int j = 0; j < command.selection.size(); j++) {

                Entity caster = command.selection.get(j);

                if (caster.labels().contains(Behaviors.BEHAVIOR_CASTS_PROJECTILE)) {

                    ProjectileCasterComponent pcc = (ProjectileCasterComponent) caster.cData.get(ProjectileCasterComponent.class);

                    if (pcc.phase == ProjectileCasterComponent.Phase.READY) {
                        pcc.phase = ProjectileCasterComponent.Phase.SHOOTING;
                    }
                }
            }
        }
    }

    public static void interpolate(Engine engine, double ct, double dt) {
        ArrayList<Entity> destinedEntities = engine.currentPlayer.denorms.getListForLabel(Behaviors.BEHAVIOR_MOVES_TOWARD_DESTINATION);

        CastingProjectileCooldownProcess.process(engine, dt);

        AdjustVelocityProcess.process(engine, dt);
        MoveTowardDestinationFunction.apply(destinedEntities, dt);
        //BattleResolution.process(engine, dt);
        TargetAcquisitionProcess.process(engine, dt);
        AttackTargetInRangeProcess.process(engine, dt);

        ProjectileExplodeProcess.process(engine, dt);

        DieOnNoHpProcess.process(engine, dt);
    }
}
