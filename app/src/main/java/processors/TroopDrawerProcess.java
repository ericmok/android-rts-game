package processors;

import java.util.ArrayList;

import components.PlayerComponent;
import components.WorldComponent;
import components.SelectionComponent;
import game.androidgame2.DrawList2DItem;
import game.androidgame2.RewriteOnlyArray;

import components.Entity;

/**
 * Created by eric on 11/3/14.
 */
public class TroopDrawerProcess {
    public static final void process(RewriteOnlyArray<DrawList2DItem> spriteAllocater, ArrayList<Entity> troopsToDraw, double dt) {
        for (int i = 0; i < troopsToDraw.size(); i++) {
            Entity entity = troopsToDraw.get(i);
            WorldComponent wc = (WorldComponent)entity.cData.get(WorldComponent.class);

            DrawList2DItem drawItem = spriteAllocater.takeNextWritable();

            if (entity.getLabels().contains(PlayerComponent.TAG_ENEMY_OWNED)) {
                drawItem.animationName = DrawList2DItem.ANIMATION_ENEMY_TROOPS_IDLING;
            }
            else {
                drawItem.animationName = DrawList2DItem.ANIMATION_TROOPS_IDLING;
            }

            drawItem.position.x = wc.pos.x;
            drawItem.position.y = wc.pos.y;
            drawItem.angle = (float)wc.rot.getDegrees();
            drawItem.width = 1.0f;
            drawItem.height = 1.0f;

            SelectionComponent sc = (SelectionComponent)entity.cData.get(SelectionComponent.class);

            if (sc.isSelected) {
                drawItem = spriteAllocater.takeNextWritable();
                drawItem.animationName = DrawList2DItem.ANIMATION_TROOPS_SELECTED;
                drawItem.position.x = wc.pos.x;
                drawItem.position.y = wc.pos.y;
                drawItem.angle = (float)wc.rot.getDegrees();
                drawItem.width = 1.3f;
                drawItem.height = 1.3f;
            }
        }

    }
}
