package noteworthyengine.battle;

/**
 * Created by eric on 2/4/16.
 */
public abstract class BattleEffect implements BattleTriggerHandler, BattleBuff, Resetable {

    @Override
    public void onSpawn(BattleSystem battleSystem) {

    }

    @Override
    public void onFindNewTarget(BattleSystem battleSystem) {

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

    }

    @Override
    public void onAttackSwing(BattleSystem battleSystem, BattleNode target) {

    }

    @Override
    public void onAttackCastFail(BattleSystem battleSystem) {

    }

    @Override
    public void onDie(BattleSystem battleSystem) {

    }

    public double buffAttackRange(double in) { return in; }

    public double buffAttackDamage(double in) { return in; }
    public double buffAttackSwingTime(double in) { return in; }
    public double buffAttackCooldown(double in) { return in; }

    public double buffArmorAmount(double in) { return in; }

    @Override
    public void reset() {

    }

    public int buffIsAttackable(int in) { return in; }
    public double buffHp(double in) { return in; }

    public double buffMaxSpeed(double in) { return in; }

    public abstract void update(BattleSystem battleSystem, double dt);

    public abstract void sendEvent(BattleSystem battleSystem, BattleNode battleNode, BattleTriggerHandler.Event event);
}