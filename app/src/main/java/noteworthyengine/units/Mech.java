package noteworthyengine.units;

import android.graphics.Color;

import art.Animations;
import noteworthyengine.BattleNode;
import noteworthyengine.RenderSystem;
import noteworthyframework.Gamer;
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
        this(null);
    }

    public Mech(Gamer gamer) {
        super();
        this.name = NAME;

        this.reset();
        this.configure(gamer);

        this.renderNode.onDraw = new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem system) {
                //renderNode.color.v = Gamer.TeamColors.get(battleNode.gamer.v.team) & 0xFF9999FF;
                renderNode.color.v = Gamer.TeamColors.get(battleNode.gamer.v.team);

                if (battleNode.hp.v <= 0) {
                    //TemporarySprite2dDef tempSprite = system.drawCompat.tempSpritesMemoryPool.fetchMemory();
                    //TemporarySprite2dDef tempSprite = system.defineNewTempSprite(Animations.ANIMATION_TROOPS_DYING_DEF, 0);
                    TemporarySprite2dDef tempSprite = system.beginNewTempSprite();

                    tempSprite.copy(Animations.ANIMATION_TROOPS_DYING_DEF);
                    tempSprite.setPosition((float) battleNode.coords.pos.x, (float) battleNode.coords.pos.y, 1);

                    system.endNewTempSprite(tempSprite, 0);

                    //system.drawCompat.drawTemporarySprite(tempSprite);
                    //system.drawCompat.tempSpritesMemoryPool.recycleMemory(tempSprite);
                }

                if (battleNode.attackState.v == BattleNode.ATTACK_STATE_SWINGING) {

                    if (battleNode.target.v != null) {
                        double ratio = (battleNode.attackProgress.v / battleNode.attackSwingTime.v);

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
                                0);
                    }
                }
            }
        };
    }

    public void reset() {
        super.reset();

        this.renderNode.width.v = 1.3f;
        this.renderNode.height.v = 1.3f;
        this.battleNode.hp.v = 90;
        this.battleNode.maxSpeed.v = 1.1;
        //this.battleNode.attackRange.v = 5.5;
        this.battleNode.attackDamage.v = 3;
        this.battleNode.attackRange.v = 1.5;
        this.battleNode.targetAcquisitionRange.v = 17;
        this.battleNode.attackSwingTime.v = 2;
        this.battleNode.attackCooldown.v = 2;
        this.battleNode.attackState.v = BattleNode.ATTACK_STATE_READY;

        this.battleNode.target.v = null;

        this.renderNode.animationName.v = "Animations/Archers/Idling";
    }
}
