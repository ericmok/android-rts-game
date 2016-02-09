package noteworthyengine.battle;

import noteworthyframework.Unit;

/**
 * Created by eric on 2/7/16.
 */
public class ChangeOwnershipOnDeathBattleEffect extends BattleEffect {

    public BattleNode battleNode;
    public SpawnForEnemyable spawnForEnemyable;

    public ChangeOwnershipOnDeathBattleEffect(BattleNode battleNode,
                SpawnForEnemyable spawnForEnemyable) {

        this.battleNode = battleNode;
        this.spawnForEnemyable = spawnForEnemyable;
    }

    @Override
    public void onDie(BattleSystem battleSystem) {
        super.onDie(battleSystem);
        spawnForEnemyable.spawnForEnemy(battleSystem, battleNode.lastAttacker.v);
    }

    @Override
    public void update(BattleSystem battleSystem, double dt) {
    }

    @Override
    public void sendEvent(BattleSystem battleSystem, BattleNode battleNode, Event event) {
    }

    public static class SpawnNewUnitOnDeathFunction {
        public void run(BattleSystem battleSystem, BattleNode battleNode) {
        }
    }

    @Override
    public void reset() {

    }

    public interface SpawnForEnemyable {
        public void spawnForEnemy(BattleSystem battleSystem, BattleNode attacker);
    }
}
