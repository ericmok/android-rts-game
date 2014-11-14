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
import utils.Orientation;

/**
 * Created by eric on 11/3/14.
 */
public class TroopDrawerProcess {

    public static int progress = 0;

    public static final void process(RewriteOnlyArray<DrawList2DItem> spriteAllocater,
                                     List<TemporaryDrawList2DItem> tempSprites,
                                     GamePool gamePool,
                                     Player player,
                                     double dt) {

        TroopDrawerProcess.progress += 1;
        TroopDrawerProcess.progress %= 100;

        ArrayList<Entity> troopsToDraw = player.denorms.getListForLabel(Behaviors.BEHAVIOR_DRAWN_AS_TROOP);

        for (int i = 0; i < troopsToDraw.size(); i++) {
            Entity entity = troopsToDraw.get(i);
            WorldComponent wc = (WorldComponent)entity.cData.get(WorldComponent.class);
            LivingComponent lc = (LivingComponent)entity.cData.get(LivingComponent.class);

            if (lc.hitPoints <= 0) {
                TemporaryDrawList2DItem tempSprite = gamePool.temporaryDrawItems.fetchMemory();
                tempSprite.color = Color.argb(240, 255, 255, 255);
                tempSprite.angle = 90;
                tempSprite.animationName = DrawList2DItem.ANIMATION_TROOPS_DYING;
                tempSprite.progress.progress = 0;
                tempSprite.width = 1;
                tempSprite.height = 1;
                tempSprite.position.x = wc.pos.x;
                tempSprite.position.y = wc.pos.y;
                tempSprites.add(tempSprite);
            }
            else {
                DrawList2DItem drawItem = spriteAllocater.takeNextWritable();

                drawItem.animationName = DrawList2DItem.ANIMATION_TROOPS_IDLING;
                drawItem.animationProgress = 0;
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
                drawItem.animationProgress = TroopDrawerProcess.progress;
                drawItem.color = player.color();
                drawItem.position.x = wc.pos.x;
                drawItem.position.y = wc.pos.y;
                drawItem.angle = (float)wc.rot.getDegrees();
                drawItem.width = 1.4f;
                drawItem.height = 1.4f;
            }

            MeleeAttackComponent mac = (MeleeAttackComponent)entity.cData.get(MeleeAttackComponent.class);

            if (mac.event == MeleeAttackComponent.Event.COOLDOWN) {
                DrawList2DItem drawItem = spriteAllocater.takeNextWritable();
                drawItem.animationName = DrawList2DItem.ANIMATION_TROOPS_COOLDOWN;
                drawItem.animationProgress = 0;
                drawItem.color = player.color();
                drawItem.position.x = wc.pos.x - 0.97 * wc.rot.x;
                drawItem.position.y = wc.pos.y - 0.97 * wc.rot.y;
                drawItem.angle = 90;//(float)wc.rot.getDegrees();
                drawItem.width = 0.6f;
                drawItem.height = 0.9f;
            }

            if (mac.event == MeleeAttackComponent.Event.ATTACKING_TARGET) {

                // Show Attack Swing

                DrawList2DItem drawItem = spriteAllocater.takeNextWritable();
                drawItem.animationName = DrawList2DItem.ANIMATION_TROOPS_SWING;
                drawItem.animationProgress = (int)(100 * (mac.attackSwingProgress / mac.attackSwingTime));
                drawItem.color = player.color();//Color.argb((int)(128 * Math.pow(2, (mac.attackSwingProgress / mac.attackSwingTime))), 255, 255, 255);
                drawItem.position.x = wc.pos.x - 0.97 * wc.rot.x;
                drawItem.position.y = wc.pos.y - 0.97 * wc.rot.y;
                drawItem.angle = 90; //(float)wc.rot.getDegrees();
                drawItem.width = 0.6f;
                drawItem.height = 0.9f;

                // Show selected

                WorldComponent targetedWC = (WorldComponent)mac.targetsInRange.get(0).cData.get(WorldComponent.class);

                drawItem = spriteAllocater.takeNextWritable();
                drawItem.animationName = DrawList2DItem.ANIMATION_TROOPS_TARGETED;
                drawItem.animationProgress = 0;
                drawItem.color = player.color();
                drawItem.position.x = targetedWC.pos.x;
                drawItem.position.y = targetedWC.pos.y;
                drawItem.angle = (float)0; //wc.rot.getDegrees();
                drawItem.width = 1.7f;
                drawItem.height = 1.7f;

                // Show projectiles

                double swingProgress = (mac.attackSwingProgress / mac.attackSwingTime);

                drawItem = spriteAllocater.takeNextWritable();
                drawItem.animationName = DrawList2DItem.ANIMATION_TROOPS_PROJECTILE;
                drawItem.animationProgress = (int) (100 * swingProgress);
                drawItem.color = Color.argb(240, 240, 210, 120);
                //drawItem.position.x = targetedWC.pos.x;
                //drawItem.position.y = targetedWC.pos.y;
                double interpolatedX = (0.3 * swingProgress + 0.3) * (targetedWC.pos.x - wc.pos.x) + wc.pos.x;
                double interpolatedY = (0.3 * swingProgress + 0.3) * (targetedWC.pos.y - wc.pos.y) + wc.pos.y;
                drawItem.position.x = interpolatedX;
                drawItem.position.y = interpolatedY;
                drawItem.angle = (float) Orientation.getDegrees(1, 0, targetedWC.pos.x - wc.pos.x, targetedWC.pos.y - wc.pos.y);
                drawItem.width = 0.5f;
                drawItem.height = 0.5f;
            }

        }

    }
}
