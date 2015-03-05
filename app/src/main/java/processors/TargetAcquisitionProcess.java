package processors;

import java.util.ArrayList;

import behaviors.Behaviors;
import behaviors.MeleeAttackComponent;
import model.Engine;
import model.Entity;
import model.Player;
import behaviors.RadiusComponent;
import behaviors.WorldComponent;
import utils.Vector2;

/**
 * Created by eric on 11/12/14.
 */
public class TargetAcquisitionProcess {

    public static class CollisionPair {
        public Entity e1 = null;
        public Entity e2 = null;
    }

    public ArrayList<CollisionPair> collisionPairs = new ArrayList<CollisionPair>(300);

    public static Vector2 temp = new Vector2();

    public static void process(Engine engine, double dt) {

        for (int i = 0; i < engine.players.size(); i++) {
            Player player1 = engine.players.get(i);

            ArrayList<Entity> troops = player1.denorms.getListForLabel(Behaviors.BEHAVIOR_AQUIRES_TARGET_IN_RANGE);

            for (int s = 0; s < troops.size(); s++) {
                Entity e1 = troops.get(s);
                MeleeAttackComponent cc1 = (MeleeAttackComponent) e1.cData.get(MeleeAttackComponent.class);
                cc1.targetsInRange.clear();
            }
        }


        for (int i = 0; i < engine.players.size(); i++) {
            Player player1 = engine.players.get(i);

            ArrayList<Entity> troops1 = player1.denorms.getListForLabel(Behaviors.BEHAVIOR_AQUIRES_TARGET_IN_RANGE);

            for (int j = 0; j < engine.players.size(); j++) {

                if (i == j) continue;

                Player player2 = engine.players.get(j);

                ArrayList<Entity> troops2 = player2.denorms.getListForLabel(Behaviors.BEHAVIOR_AQUIRES_TARGET_IN_RANGE);

                for (int s = 0; s < troops1.size(); s++) {

                    Entity entity1 = troops1.get(s);

                    MeleeAttackComponent cc1 = (MeleeAttackComponent)entity1.cData.get(MeleeAttackComponent.class);

                    for (int t = 0; t < troops2.size(); t++) {

                        Entity entity2 = troops2.get(t);

                        WorldComponent wc1 = (WorldComponent)entity1.cData.get(WorldComponent.class);
                        WorldComponent wc2 = (WorldComponent)entity2.cData.get(WorldComponent.class);

                        RadiusComponent rc1 = (RadiusComponent)entity1.cData.get(RadiusComponent.class);
                        RadiusComponent rc2 = (RadiusComponent)entity2.cData.get(RadiusComponent.class);

                        MeleeAttackComponent cc2 = (MeleeAttackComponent)entity2.cData.get(MeleeAttackComponent.class);

                        double range = wc1.pos.distanceTo(wc2.pos);

                        Vector2.subtract(temp, wc2.pos, wc1.pos);
                        temp.setNormalized();

                        if (range < cc1.targetAcquisitionRange) {
                            if (wc1.rot.dotProduct(temp) > 0.4) {
                                cc1.targetsInRange.add(entity2);
                            }
                        }

                        temp.set(-temp.x, -temp.y);

                        if (range < cc2.targetAcquisitionRange) {
                            if (wc2.rot.dotProduct(temp) > 0.4) {
                                cc2.targetsInRange.add(entity1);
                            }

                        }
                    }
                }
            }
        }
    }
}
