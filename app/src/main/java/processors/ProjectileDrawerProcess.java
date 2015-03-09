package processors;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import behaviors.Behaviors;
import behaviors.DestinationComponent;
import model.Entity;
import behaviors.LivingComponent;
import model.Player;
import behaviors.WorldComponent;
import structure.Sprite2dDef;
import structure.GamePool;
import structure.RewriteOnlyArray;
import structure.TemporarySprite2dDef;
import utils.Orientation;

/**
 * Created by eric on 11/9/14.
 */
public class ProjectileDrawerProcess {

    public static final void process(RewriteOnlyArray<Sprite2dDef> spriteAllocater,
                                     List<TemporarySprite2dDef> tempSprites,
                                     GamePool gamePool,
                                     Player player,
                                     double dt) {

        ArrayList<Entity> projectiles = player.denorms.getListForLabel(Behaviors.UNIT_BASIC_PROJECTILE);

        for (int i = 0; i < projectiles.size(); i++) {
            Entity projectile = projectiles.get(i);
            WorldComponent worldComponent = (WorldComponent)projectile.cData.get(WorldComponent.class);
            DestinationComponent dc = (DestinationComponent)projectile.cData.get(DestinationComponent.class);
            LivingComponent lc = (LivingComponent)projectile.cData.get(LivingComponent.class);

            Sprite2dDef item = spriteAllocater.takeNextWritable();
            item.position.x = worldComponent.pos.x;
            item.position.y = worldComponent.pos.y;
            item.animationName = Sprite2dDef.ANIMATION_PROJECTILE_BASIC;
            item.width = 1;
            item.height = 1;
            item.animationProgress = 0;
            item.color = player.color();
            item.angle = (float)Orientation.getDegreesBaseX(dc.dest.x - worldComponent.pos.x,
                                                            dc.dest.y - worldComponent.pos.y);


            if (lc.hitPoints <= 0) {
                TemporarySprite2dDef tempSprite = gamePool.temporaryDrawItems.fetchMemory();
                tempSprite.animationName = Sprite2dDef.ANIMATION_PROJECTILE_EXPLOSION;
                tempSprite.progress.progress = 0;
                tempSprite.progress.duration = 600;
                tempSprite.animationProgress = 0;
                tempSprite.color = Color.WHITE;
                tempSprite.width = 0.7f;
                tempSprite.height = 0.7f;
                tempSprite.angle = 90;
                tempSprite.position.x = worldComponent.pos.x;
                tempSprite.position.y = worldComponent.pos.y;
                tempSprites.add(tempSprite);
            }
        }

    }
}
