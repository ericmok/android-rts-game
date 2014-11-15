package processors;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import model.Behaviors;
import model.DestinationComponent;
import model.Entity;
import model.Player;
import model.WorldComponent;
import structure.DrawList2DItem;
import structure.GamePool;
import structure.RewriteOnlyArray;
import structure.TemporaryDrawList2DItem;
import utils.Orientation;

/**
 * Created by eric on 11/9/14.
 */
public class ProjectileDrawerProcess {

    public static final void process(RewriteOnlyArray<DrawList2DItem> spriteAllocater,
                                     List<TemporaryDrawList2DItem> tempSprites,
                                     GamePool gamePool,
                                     Player player,
                                     double dt) {

        ArrayList<Entity> projectiles = player.denorms.getListForLabel(Behaviors.UNIT_BASIC_PROJECTILE);

        for (int i = 0; i < projectiles.size(); i++) {
            Entity projectile = projectiles.get(i);
            WorldComponent worldComponent = (WorldComponent)projectile.cData.get(WorldComponent.class);
            DestinationComponent dc = (DestinationComponent)projectile.cData.get(DestinationComponent.class);

            DrawList2DItem item = spriteAllocater.takeNextWritable();
            item.position.x = worldComponent.pos.x;
            item.position.y = worldComponent.pos.y;
            item.animationName = DrawList2DItem.ANIMATION_PROJECTILE_BASIC;
            item.width = 1;
            item.height = 1;
            item.animationProgress = 0;
            item.color = player.color();
            item.angle = (float)Orientation.getDegreesBaseX(dc.dest.x - worldComponent.pos.x,
                                                            dc.dest.y - worldComponent.pos.y);
        }

    }
}
