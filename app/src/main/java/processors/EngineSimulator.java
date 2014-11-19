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

    public static void changeModelWithCommand(Engine engine, Command command) {

        // TODO: Sort command history by timeStamp
        // Replay commands taking into account their timeStamps

        if (command.command == Command.MOVE) {

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

            double mag = vectorToDestFromCogX * vectorToDestFromCogX + vectorToDestFromCogY * vectorToDestFromCogY;
            mag = Math.sqrt(mag);

//            tempRot.set(vectorToDestFromCogX, vectorToDestFromCogY);
//            tempRot.getPerpendicular(temp);


            for (int j = 0; j < command.selection.size(); j++) {
                Entity entity = command.selection.get(j);

                WorldComponent wc = (WorldComponent) entity.cData.get(WorldComponent.class);
                DestinationComponent dc = (DestinationComponent) entity.cData.get(DestinationComponent.class);

                double pointInFormationX = wc.pos.x - cog.x;
                double pointInFormationY = wc.pos.y - cog.y;

                double minTransformedFormationX = 0;
                double minTransformedFormationY = 0;

                int minIndex = 0;
                double minDistance = 10000000;

                for (int f = 0; f < formationPoints.size(); f++) {
                    Vector2 candidate = formationPoints.get(f);

                    //double degrees = Orientation.getDegreesBaseX(candidate.x, candidate.y);
                    //double transformedFormationX = temp.x * candidate.x;
                    //double transformedFormationX = Math.cos(Math.toRadians(degrees)) * candidate.x - Math.sin(Math.toRadians(degrees)) * candidate.y;
                    //double transformedFormationY = temp.y * candidate.y;
                    //double transformedFormationY = Math.sin(Math.toRadians(degrees)) * candidate.x + Math.cos(Math.toRadians(degrees)) * candidate.y;

                    temp.set(candidate.x, candidate.y);
                    Orientation.setVectorToDegree(temp, degrees);
//                    double sqDist = Math.pow((transformedFormationX - pointInFormationX),2) +
//                                    Math.pow((transformedFormationY - pointInFormationY),2);

                    double sqDist = Math.pow((command.vec.x + temp.x - wc.pos.x),2) +
                                    Math.pow((command.vec.y + temp.y - wc.pos.y),2);

                    if (sqDist < minDistance && !taken.contains(f)) {
                        minDistance = sqDist;
                        minIndex = f; // TODO: Not re-use already taken formations
                        taken.add(f);
                        //minTransformedFormationX = transformedFormationX;
                        //minTransformedFormationY = transformedFormationY;
                        minTransformedFormationX = temp.x;
                        minTransformedFormationY = temp.y;
                    }
                }

                // After getting the closest point to the rotated formation,
                // offset formation point by the ammount

//                dc.dest.set(pointInFormationX + vectorToDestFromCogX + cog.x + minTransformedFormationX,
//                        pointInFormationY + vectorToDestFromCogY + cog.y + minTransformedFormationY);


                //double degrees = Orientation.getDegreesBaseX(vectorToDestFromCogX, vectorToDestFromCogY);
//                double transformedFormationX = Math.cos(Math.toRadians(degrees)) * formationPoints.get(j).x - Math.sin(Math.toRadians(degrees)) * formationPoints.get(j).y;
//                double transformedFormationY = Math.sin(Math.toRadians(degrees)) * formationPoints.get(j).x + Math.cos(Math.toRadians(degrees)) * formationPoints.get(j).y;

                temp.set(formationPoints.get(minIndex).x, formationPoints.get(minIndex).y);
                Orientation.setVectorToDegree(temp, degrees);

//                dc.dest.set(vectorToDestFromCogX + cog.x + transformedFormationX,
//                            vectorToDestFromCogY + cog.y + transformedFormationY);
                dc.dest.set(vectorToDestFromCogX + cog.x + temp.x,
                            vectorToDestFromCogY + cog.y + temp.y);

//                dc.dest.set(vectorToDestFromCogX + cog.x + transformedFormationX ,
//                        vectorToDestFromCogY + cog.y + transformedFormationY);

                // This one uses the x of the formation points 2 times.
                // We just scale the perpendicular vector
                // On the other hand the y value is the vector to dest
//                dc.dest.set(vectorToDestFromCogX + cog.x + temp.x * formationPoints.get(minIndex).x,
//                        vectorToDestFromCogY + cog.y + temp.y * formationPoints.get(minIndex).x);


//                double offsetX = vectorToDestFromCogX + formationPoints.get(minIndex).x;
//                double offsetY = vectorToDestFromCogY + formationPoints.get(minIndex).y;

                //dc.dest.set(command.vec.x - wc.pos.x, command.vec.y - wc.pos.y);
//                dc.dest.set(offsetX, offsetY);
//
//                dc.dest.setNormalized();
//
//                dc.dest.scale(mag, mag);
//                dc.dest.translate(wc.pos.x, wc.pos.y);

                dc.hasDestination = true;
            }

            taken.clear();
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
