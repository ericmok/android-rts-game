package processors;

import java.util.ArrayList;

import model.Behaviors;
import model.MeleeAttackComponent;
import model.Engine;
import model.Entity;
import model.LivingComponent;
import model.Player;
import model.VelocityComponent;
import model.WorldComponent;
import utils.Vector2;

/**
 * Created by eric on 11/12/14.
 */
public class AttackTargetInRangeProcess {

    private static Vector2 temp = new Vector2();

    public static void process(Engine engine, double dt) {

        for (int i = 0; i < engine.players.size(); i++) {
            Player player = engine.players.get(i);

            ArrayList<Entity> troops = player.denorms.getListForLabel(Behaviors.BEHAVIOR_AQUIRES_TARGET_IN_RANGE);

            for (int j = 0; j < troops.size(); j++) {
                Entity troop = troops.get(j);
                MeleeAttackComponent cc = (MeleeAttackComponent)troop.cData.get(MeleeAttackComponent.class);
                if (cc.event == MeleeAttackComponent.Event.COOLDOWN) {
                    cc.attackCooldown = cc.attackCooldown - dt;
                    if (cc.attackCooldown <= 0) {
                        cc.event = MeleeAttackComponent.Event.READY;
                        cc.attackSwingProgress = 0;
                        cc.attackCooldown = 0;
                    }
                }

                if (cc.targetsInRange.isEmpty()) {
                    if (cc.event == MeleeAttackComponent.Event.ATTACKING_TARGET) {
                        cc.event = MeleeAttackComponent.Event.COOLDOWN;
                    }
                    cc.targetLock = null;
                    continue;
                }

                Entity toAttack;

                // If possible, we want to attack the previous target.
                // If units get destroyed and added, the target acquisition might not be stable!
                if (cc.targetLock != null) {
                    toAttack = cc.targetLock;

                    WorldComponent toAttackWC = (WorldComponent)toAttack.cData.get(WorldComponent.class);
                    WorldComponent troopWC = (WorldComponent)troop.cData.get(WorldComponent.class);

                    if ( toAttackWC.pos.distanceTo(troopWC.pos) > cc.targetAcquisitionRange ) {
                        toAttack = cc.targetsInRange.get(0);
                    }
                }
                else {
                    cc.targetLock = toAttack = cc.targetsInRange.get(0);
                }

                if (!toAttack.labels().contains(Behaviors.BEHAVIOR_TAKES_DAMAGE_ON_COLLISION)) {
                    continue;
                }

                if (cc.event == MeleeAttackComponent.Event.READY) {

                    cc.event = MeleeAttackComponent.Event.ATTACKING_TARGET;

                }

                if (cc.event == MeleeAttackComponent.Event.ATTACKING_TARGET) {
                    cc.attackSwingProgress += dt;

                    if (cc.attackSwingProgress >= cc.attackSwingTime) {

                        LivingComponent lc = (LivingComponent) toAttack.cData.get(LivingComponent.class);
                        lc.takeDamage(cc.attackStrength);

                        cc.event = MeleeAttackComponent.Event.COOLDOWN;
                        cc.attackCooldown = 2;
                        cc.attackSwingProgress = 0;
                    }
                }

            }
        }
    }

}
