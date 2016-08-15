package noteworthyengine.battle;

/**
 * Created by eric on 5/24/16.
 *
 * Find a target to attack. When attacking, deal AOE damage to enemy units within attack range.
 *
 */
public class SuicidalAOEAttackBattleEffect extends BattleEffect {
    //RewriteOnlyArray<BattleNode.Target> multipleTargetBuffer = new RewriteOnlyArray<>(BattleNode.Target.class, 8);

    @Override
    public void update(BattleSystem battleSystem, double dt) {

    }

    @Override
    public void sendEvent(BattleSystem battleSystem, BattleNode battleNode, Event event) {
        if (event == Event.FIND_NEW_TARGET) {
            battleSystem.findClosestBatleNodeWithinRange(battleNode.target, battleNode, battleNode.targetAcquisitionRange.v, BattleSystem.DEFAULT_TARGET_CRITERIA);
        }
        if (event == Event.ATTACK_CAST) {
            battleSystem.inflictSplashDamage(battleNode, battleNode.battleAttack.range, BattleSystem.DEFAULT_TARGET_CRITERIA);

            battleNode.hp.v = 0;
        }
    }
}
