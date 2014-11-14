package processors;

import java.util.ArrayList;

import model.Behaviors;
import model.Engine;
import model.Entity;
import model.MeleeAttackComponent;
import model.Player;
import model.VelocityComponent;

/**
 * Created by eric on 11/13/14.
 */
public class AdjustVelocityProcess {

    public static void process(Engine engine, double dt) {

        for (int i = 0; i < engine.players.size(); i++) {
            Player player = engine.players.get(i);

            ArrayList<Entity> entities = player.denorms.getListForLabel(Behaviors.BEHAVIOR_AQUIRES_TARGET_IN_RANGE);

            for (int e = 0; e < entities.size(); e++) {
                Entity entity = entities.get(e);

                MeleeAttackComponent mac = (MeleeAttackComponent)entity.cData.get(MeleeAttackComponent.class);
                VelocityComponent vc = (VelocityComponent)entity.cData.get(VelocityComponent.class);

                vc.moveSpeed = 1;

                if (mac.targetsInRange.size() > 0) {
                    vc.moveSpeed = 0.3;
                }

                if (mac.attackSwingProgress > 0) {
                    vc.moveSpeed = 0.2;
                }
            }
        }

    }
}
