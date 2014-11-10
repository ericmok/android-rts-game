package processors;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import model.Entity;
import model.Player;
import model.Projectile;
import model.WorldComponent;
import structure.DrawList2DItem;
import structure.GamePool;
import structure.GameSettings;
import structure.RewriteOnlyArray;
import structure.TemporaryDrawList2DItem;

/**
 * Created by eric on 11/9/14.
 */
public class ProjectileDrawerProcess {

    public static final void process(RewriteOnlyArray<DrawList2DItem> spriteAllocater,
                                     List<TemporaryDrawList2DItem> tempSprites,
                                     GamePool gamePool,
                                     Player player,
                                     double dt) {

        ArrayList<Entity> projectiles = player.denorms.getListForLabel(Entity.UNIT_BASIC_PROJECTILE);

        for (int i = 0; i < projectiles.size(); i++) {
            Entity projectile = projectiles.get(i);
            WorldComponent worldComponent = (WorldComponent)projectile.cData.get(WorldComponent.class);

            DrawList2DItem item = spriteAllocater.takeNextWritable();
            item.position.x = worldComponent.pos.x;
            item.position.y = worldComponent.pos.y;
            item.animationName = DrawList2DItem.ANIMATION_RETICLE_TAP;
            item.width = 1;
            item.height = 1;
            item.animationProgress = 0;
            item.color = Color.WHITE;
            item.angle = (float)Math.random() * 360;
        }

    }
}
