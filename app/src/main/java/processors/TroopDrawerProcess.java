package processors;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import model.Behaviors;
import model.LivingComponent;
import model.MeleeAttackComponent;
import model.Player;
import model.WorldComponent;
import model.SelectionComponent;
import structure.DrawList2DItem;
import structure.GamePool;
import structure.RewriteOnlyArray;

import model.Entity;
import structure.TemporaryDrawList2DItem;

/**
 * Created by eric on 11/3/14.
 */
public class TroopDrawerProcess {
    public static final void process(RewriteOnlyArray<DrawList2DItem> spriteAllocater,
                                     List<TemporaryDrawList2DItem> tempSprites,
                                     GamePool gamePool,
                                     Player player,
                                     double dt) {
        ArrayList<Entity> troopsToDraw = player.denorms.getListForLabel(Behaviors.BEHAVIOR_DRAWN_AS_TROOP);

        for (int i = 0; i < troopsToDraw.size(); i++) {
            Entity entity = troopsToDraw.get(i);
            WorldComponent wc = (WorldComponent)entity.cData.get(WorldComponent.class);
            LivingComponent lc = (LivingComponent)entity.cData.get(LivingComponent.class);

            if (lc.hitPoints <= 0) {
                TemporaryDrawList2DItem tempSprite = gamePool.temporaryDrawItems.fetchMemory();
                tempSprite.color = Color.argb(240, 255, 255, 255);
                tempSprite.progress.progress = 0;
                tempSprite.angle = 90;
                tempSprite.animationName = DrawList2DItem.ANIMATION_TROOPS_DYING;
                tempSprite.width = 1;
                tempSprite.height = 1;
                tempSprite.position.x = wc.pos.x;
                tempSprite.position.y = wc.pos.y;
                tempSprites.add(tempSprite);
            }
            else {
                DrawList2DItem drawItem = spriteAllocater.takeNextWritable();

                drawItem.animationName = DrawList2DItem.ANIMATION_TROOPS_IDLING;
                drawItem.color = player.color();
                drawItem.position.x = wc.pos.x;
                drawItem.position.y = wc.pos.y;
                drawItem.angle = (float)wc.rot.getDegrees();
                drawItem.width = 1.0f;
                drawItem.height = 1.0f;
            }

            SelectionComponent sc = (SelectionComponent)entity.cData.get(SelectionComponent.class);

            if (sc.isSelected) {
                DrawList2DItem drawItem = spriteAllocater.takeNextWritable();
                drawItem.animationName = DrawList2DItem.ANIMATION_TROOPS_SELECTED;
                drawItem.color = player.color();
                drawItem.position.x = wc.pos.x;
                drawItem.position.y = wc.pos.y;
                drawItem.angle = (float)wc.rot.getDegrees();
                drawItem.width = 1.3f;
                drawItem.height = 1.3f;
            }

            MeleeAttackComponent mac = (MeleeAttackComponent)entity.cData.get(MeleeAttackComponent.class);
            if (mac.event == MeleeAttackComponent.Event.COOLDOWN) {
                DrawList2DItem drawItem = spriteAllocater.takeNextWritable();
                drawItem.animationName = DrawList2DItem.ANIMATION_RETICLE_TAP;
                drawItem.color = Color.BLUE;
                drawItem.position.x = wc.pos.x;
                drawItem.position.y = wc.pos.y;
                drawItem.angle = (float)wc.rot.getDegrees();
                drawItem.width = 1.4f;
                drawItem.height = 1.4f;
            }
            if (mac.event == MeleeAttackComponent.Event.ATTACKING_TARGET) {
                DrawList2DItem drawItem = spriteAllocater.takeNextWritable();
                drawItem.animationName = DrawList2DItem.ANIMATION_BUTTONS_ATTACK;
                drawItem.color = player.color();
                drawItem.position.x = wc.pos.x;
                drawItem.position.y = wc.pos.y;
                drawItem.angle = (float)0;
                drawItem.width = 1.4f;
                drawItem.height = 1f;
            }
        }

    }
}
