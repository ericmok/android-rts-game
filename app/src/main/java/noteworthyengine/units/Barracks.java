package noteworthyengine.units;

import art.Constants;
import noteworthyengine.QuadTreeSystem;
import noteworthyengine.battle.BattleEffect;
import noteworthyengine.battle.BattleNode;
import noteworthyengine.battle.BattleSystem;
import noteworthyengine.CityWinLoseConditionNode;
import noteworthyengine.FactoryNode;
import noteworthyengine.FactorySystem;
import noteworthyengine.GridNode;
import noteworthyengine.RenderNode;
import noteworthyengine.RenderSystem;
import noteworthyengine.battle.BattleTriggerHandler;
import noteworthyengine.battle.ChangeOwnershipOnDeathBattleEffect;
import noteworthyengine.battle.EmptyBattleEffect;
import noteworthyengine.players.PlayerUnit;
import noteworthyframework.Unit;
import structure.TextDrawItem;
import utils.VoidFunc;
import utils.VoidFunc2;

/**
 * Created by eric on 4/30/15.
 */
public class Barracks extends Unit implements ChangeOwnershipOnDeathBattleEffect.SpawnForEnemyable{

    public static final String NAME = "barracks";

    public GridNode gridNode;

    public FactoryNode factoryNode = new FactoryNode(this);

    public BattleNode battleNode = new BattleNode(this) {{
        this.battleEffects.add(new ChangeOwnershipOnDeathBattleEffect(this, Barracks.this));
    }};

    public RenderNode renderNode = new RenderNode(this);

    public CityWinLoseConditionNode cityWinLoseConditionNode = new CityWinLoseConditionNode(this);

    public Barracks() {
        this.name = NAME;

        this.addNode("QuadTreeNode created in constructor", new QuadTreeSystem.QuadTreeNode(this));
        gridNode = new GridNode(this, null, battleNode);

        renderNode.onDraw = new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem system) {
                TextDrawItem textDrawItem = system.fetchTextDrawItem();
                textDrawItem.position.set(renderNode.coords.pos.x, renderNode.coords.pos.y - 1, 0);
                textDrawItem.height = 0.9f;
                textDrawItem.cameraIndex = system.getCameraIndex(RenderNode.RENDER_LAYER_FOREGROUND);
                textDrawItem.color = renderNode.color.v;
                textDrawItem.textDirection.set(1, 0);
                textDrawItem.stringBuilder.setLength(0);
                textDrawItem.stringBuilder.append((int) (factoryNode.buildTime.v - factoryNode.buildProgress.v));

                // HP Bars:
                system.drawLine(system.getCameraIndex(renderNode.renderLayer.v),
                    (float)renderNode.coords.pos.x - 0.5f, (float)renderNode.coords.pos.y + 0.8f,
                    (float)renderNode.coords.pos.x + 0.5f * (float)(battleNode.hp.v / battleNode.startingHp.v), (float)renderNode.coords.pos.y + 0.8f,
                    4,
                    renderNode.color.v);

            }
        };

        factoryNode.spawnFunction = new VoidFunc2<FactorySystem, FactoryNode>() {
            @Override
            public void apply(FactorySystem factorySystem, FactoryNode factoryNode) {
                Platoon platoon = UnitPool.platoons.fetchMemory();
                platoon.configure(factoryNode.playerUnitPtr.v);
                platoon.battleNode.coords.pos.copy(battleNode.coords.pos);
                factorySystem.getBaseEngine().addUnit(platoon);
            }
        };
    }

    public void configure(PlayerUnit playerUnit) {
        battleNode.reset();
        battleNode.startingHp.v = 100;
        battleNode.hp.v = 100;
        battleNode.battleAttack.amount = 1;
        battleNode.playerUnitPtr.v = playerUnit;
        renderNode.set(0, 0, 0, 1.5f, 1.5f, 90f, Constants.colorForTeam(playerUnit.playerNode.playerData.team), "Animations/Buildings/City", 0, 0);

        factoryNode.configure(playerUnit);
        factoryNode.buildTime.v = 25;
        factoryNode.buildProgress.v = 0;
    }

    public void spawnForEnemy(BattleSystem battleSystem, BattleNode attacker) {
        Barracks barracks = UnitPool.barracks.fetchMemory();
        barracks.configure(attacker.playerUnitPtr.v);
        barracks.battleNode.coords.pos.copy(battleNode.coords.pos);
        battleSystem.getBaseEngine().addUnit(barracks);
    }
}
