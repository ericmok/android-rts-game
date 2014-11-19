package processors;

import android.util.Log;

import java.util.ArrayList;

import model.Behaviors;
import model.DestinationComponent;
import model.Entity;
import model.Engine;
import model.ProjectileCasterComponent;
import model.WorldComponent;
import networking.Command;
import utils.Orientation;
import utils.Vector2;

/**
 * Created by eric on 11/6/14.
 */
public class EngineSimulator {

    public static Vector2 cog = new Vector2();
    public static Vector2 temp = new Vector2();
    public static Orientation tempRot = new Orientation();

    public static final double FORMATION_OFFSET = 1.2;

    public static ArrayList<Vector2> formationPoints = new ArrayList<Vector2>() {{
        this.add(new Vector2(0, 0));
        this.add(new Vector2(0, FORMATION_OFFSET));
        this.add(new Vector2(0, -FORMATION_OFFSET));
        this.add(new Vector2(0, 2*FORMATION_OFFSET));
        this.add(new Vector2(0, -2*FORMATION_OFFSET));
        this.add(new Vector2(0, 3*FORMATION_OFFSET));
        this.add(new Vector2(0, -3*FORMATION_OFFSET));
        this.add(new Vector2(FORMATION_OFFSET, 0));
        this.add(new Vector2(FORMATION_OFFSET, FORMATION_OFFSET));
        this.add(new Vector2(FORMATION_OFFSET, -FORMATION_OFFSET));
        this.add(new Vector2(FORMATION_OFFSET, 2*FORMATION_OFFSET));
        this.add(new Vector2(FORMATION_OFFSET, -2*FORMATION_OFFSET));
        this.add(new Vector2(FORMATION_OFFSET, 3*FORMATION_OFFSET));
        this.add(new Vector2(FORMATION_OFFSET, -3*FORMATION_OFFSET));
        this.add(new Vector2(2*FORMATION_OFFSET, 0));
        this.add(new Vector2(2*FORMATION_OFFSET, FORMATION_OFFSET));
        this.add(new Vector2(2*FORMATION_OFFSET, -FORMATION_OFFSET));
        this.add(new Vector2(2*FORMATION_OFFSET, 2*FORMATION_OFFSET));
        this.add(new Vector2(2*FORMATION_OFFSET, -2*FORMATION_OFFSET));
        this.add(new Vector2(2*FORMATION_OFFSET, 3*FORMATION_OFFSET));
        this.add(new Vector2(2*FORMATION_OFFSET, -3*FORMATION_OFFSET));
        this.add(new Vector2(2*FORMATION_OFFSET, 4*FORMATION_OFFSET));
        this.add(new Vector2(2*FORMATION_OFFSET, -4*FORMATION_OFFSET));
        this.add(new Vector2(2*FORMATION_OFFSET, 5*FORMATION_OFFSET));
        this.add(new Vector2(2*FORMATION_OFFSET, -5*FORMATION_OFFSET));
    }};

    public static ArrayList<Integer> taken = new ArrayList<Integer>(32);

    // TODO: Replay commands in order by their time stamps

    public static void changeModelWithCommand(Engine engine, Command command) {
        if (command.command == Command.MOVE) {
            moveCommand(engine, command);
        }

        if (command.command == Command.FIRE) {
            fireCommand(engine, command);
        }
    }

    public static void moveCommand(Engine engine, Command command) {

        cog.zero();

        for (int j = 0; j < command.selection.size(); j++) {
            WorldComponent wc = (WorldComponent)command.selection.get(j).cData.get(WorldComponent.class);
            cog.translate(wc.pos.x, wc.pos.y);
        }
        double divide = 1.0/command.selection.size();
        cog.scale(divide, divide);

        double vectorToDestFromCogX = command.vec.x - cog.x;
        double vectorToDestFromCogY = command.vec.y - cog.y;

        double degrees = Orientation.getDegreesBaseX(vectorToDestFromCogX, vectorToDestFromCogY);

        for (int j = 0; j < command.selection.size(); j++) {
            Entity entity = command.selection.get(j);

            WorldComponent wc = (WorldComponent) entity.cData.get(WorldComponent.class);
            DestinationComponent dc = (DestinationComponent) entity.cData.get(DestinationComponent.class);

            double minTransformedFormationX = 0;
            double minTransformedFormationY = 0;

            int minIndex = 0;
            double minDistance = 10000000;

            for (int f = 0; f < formationPoints.size(); f++) {
                Vector2 candidate = formationPoints.get(f);

                temp.set(candidate.x, candidate.y);
                Orientation.setVectorToDegree(temp, degrees);

                double sqDist = Math.pow((command.vec.x + temp.x - wc.pos.x),2) +
                        Math.pow((command.vec.y + temp.y - wc.pos.y),2);

                if (sqDist < minDistance && !taken.contains(f)) {
                    minDistance = sqDist;
                    minIndex = f;
                    minTransformedFormationX = temp.x;
                    minTransformedFormationY = temp.y;
                    taken.add(f);
                }
            }

            dc.dest.set(vectorToDestFromCogX + cog.x + minTransformedFormationX,
                    vectorToDestFromCogY + cog.y + minTransformedFormationY);

            dc.hasDestination = true;
        }

        taken.clear();
    }

    public static void fireCommand(Engine engine, Command command) {
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
