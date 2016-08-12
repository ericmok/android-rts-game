package noteworthyengine.battle;

/**
 * Created by eric on 2/7/16.
 */
public class BasicAttackEffect extends BattleEffect {

    public BattleNode battleNode;

    public BasicAttackEffect(BattleNode battleNode) {
        this.battleNode = battleNode;
    }

    @Override
    public void onSpawn(BattleSystem battleSystem) {

    }

    @Override
    public void onFindNewTarget(BattleSystem battleSystem) {
        battleSystem.findClosestBatleNodeWithinRange(battleNode.target,
                battleNode,
                battleNode.targetAcquisitionRange.v,
                battleNode.targetCriteria);
    }

    @Override
    public void onTargetAcquired(BattleSystem battleSystem) {

    }

    @Override
    public void onTargetLost(BattleSystem battleSystem) {

    }

    @Override
    public void onAttackReady(BattleSystem battleSystem, BattleNode target) {

    }

    @Override
    public void onAttacked(BattleSystem battleSystem, BattleNode attacker, double damage) {

    }

    @Override
    public void onAttackCast(BattleSystem battleSystem, BattleNode target) {
        battleSystem.calculateAndInflictDamage(battleNode, target);
    }

    @Override
    public void onAttackSwing(BattleSystem battleSystem, BattleNode target) {

    }

    @Override
    public void onAttackCastFail(BattleSystem battleSystem) {
        battleNode.battleState.v = BattleNode.BATTLE_STATE_IDLE;
        battleNode.battleProgress.v = 0;
    }

    @Override
    public void onDie(BattleSystem battleSystem) {

    }

    @Override
    public void update(BattleSystem battleSystem, double dt) {

    }

    @Override
    public void sendEvent(BattleSystem battleSystem, BattleNode battleNode, Event event) {

    }

    @Override
    public void reset() {

    }
}
