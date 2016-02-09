package noteworthyengine.battle;

/**
 * Created by eric on 2/4/16.
 */
public interface BattleTriggerHandler {

    //public void onArmorHit();

    public static enum Event {
        SPAWN,
        FIND_NEW_TARGET,
        TARGET_ACQUIRED,
        TARGET_LOST,
        ATTACK_READY,
        ATTACKED,
        ATTACK_CAST,
        ATTACK_SWING,
        ATTACK_CAST_FAIL,
        DIE
    }

    public void onSpawn(BattleSystem battleSystem);
    public void onFindNewTarget(BattleSystem battleSystem);
    public void onTargetAcquired(BattleSystem battleSystem);
    public void onTargetLost(BattleSystem battleSystem);
    public void onAttackReady(BattleSystem battleSystem, BattleNode target);
    public void onAttacked(BattleSystem battleSystem, BattleNode attacker, double damage);
    public void onAttackCast(BattleSystem battleSystem, BattleNode target);
    public void onAttackSwing(BattleSystem battleSystem, BattleNode target);
    public void onAttackCastFail(BattleSystem battleSystem);
    public void onDie(BattleSystem battleSystem);
}
