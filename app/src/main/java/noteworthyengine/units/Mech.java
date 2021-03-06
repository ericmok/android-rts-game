package noteworthyengine.units;

import art.Animations;
import art.Constants;
import noteworthyengine.battle.BattleNode;
import noteworthyengine.RenderNode;
import noteworthyengine.RenderSystem;
import noteworthyengine.players.PlayerUnit;
import structure.Sprite2dDef;
import structure.TemporarySprite2dDef;
import utils.Orientation;
import utils.VoidFunc;

/**
 * Created by eric on 3/23/15.
 */
public class Mech extends Platoon {

    public static final String NAME = "Mech";

    public Mech() {
        super();
        this.name = NAME;

        this.renderNode.onDraw = new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem system) {
                //renderNode.color.v = Gamer.TeamColors.get(battleNode.gamer.v.team) & 0xFF9999FF;
                renderNode.color.v = Constants.colorForTeam(battleNode.playerUnitPtr.v.playerNode.playerData.team);

                if (!battleNode.isAlive()) {
                    //TemporarySprite2dDef tempSprite = system.drawCompat.tempSpritesMemoryPool.fetchMemory();
                    //TemporarySprite2dDef tempSprite = system.defineNewTempSprite(Animations.ANIMATION_TROOPS_DYING_DEF, 0);
                    TemporarySprite2dDef tempSprite = system.beginNewTempSprite();

                    tempSprite.copy(Animations.ANIMATION_TROOPS_DYING_DEF);
                    tempSprite.setPosition((float) battleNode.coords.pos.x, (float) battleNode.coords.pos.y, 1);

                    system.endNewTempSprite(tempSprite, 0);

                    //system.drawCompat.drawTemporarySprite(tempSprite);
                    //system.drawCompat.tempSpritesMemoryPool.recycleMemory(tempSprite);
                }

                if (battleNode.battleState.v == BattleNode.BATTLE_STATE_SWINGING) {

                    if (battleNode.target.v != null) {
                        double ratio = (battleNode.battleProgress.v / battleNode.battleAttack.swingTime);

                        //Sprite2dDef sprite2dDef = system.drawCompat.spriteAllocator.takeNextWritable();
                        //sprite2dDef.set("Animations/Troops/Sword", 0,
                        Sprite2dDef sprite2dDef = system.defineNewSprite(
                                "Animations/Troops/Sword", 0,
                                (float)(ratio * (battleNode.target.v.coords.pos.x - battleNode.coords.pos.x) + battleNode.coords.pos.x),
                                (float)(ratio * (battleNode.target.v.coords.pos.y - battleNode.coords.pos.y) + battleNode.coords.pos.y),
                                1f,
                                0.55f, 0.55f,
                                (float) Orientation.getDegreesBaseX(battleNode.target.v.coords.pos.x - battleNode.coords.pos.x,
                                        battleNode.target.v.coords.pos.y - battleNode.coords.pos.y),
                                renderNode.color.v,
                                RenderNode.RENDER_LAYER_FOREGROUND);
                    }
                }
            }
        };
    }

    public void configure(final PlayerUnit playerUnit) {
        this.movementNode.reset();
        this.battleNode.reset();

        this.renderNode.width.v = 1.2f;
        this.renderNode.height.v = 1.2f;

        this.battleNode.playerUnitPtr.v = playerUnit;
        this.battleNode.hp.v = 110;
        this.battleNode.maxSpeed.v = 0.9;
        //this.battleNode.attackRange.v = 5.5;
        this.battleNode.battleAttack.amount = 5;
        this.battleNode.battleAttack.range = 2;
        this.battleNode.targetAcquisitionRange.v = this.battleNode.battleAttack.range + 3;
        this.battleNode.battleAttack.swingTime = 1;
        this.battleNode.battleAttack.cooldownTime = 2;

        this.battleNode.target.v = null;

        this.renderNode.animationName.v = "Animations/Archers/Idling";
    }
}
