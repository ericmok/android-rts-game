package processors;

import java.util.ArrayList;

import model.Behaviors;
import model.DestinationComponent;
import model.Engine;
import model.GameEntities;
import model.LivingComponent;
import model.Player;
import model.Entity;
import model.ProjectileCasterComponent;
import model.VelocityComponent;
import model.WorldComponent;

/**
 * Created by eric on 11/14/14.
 */
public class CastingProjectileCooldownProcess {

    public static void process(Engine engine, double dt) {

        for (int p = 0; p < engine.players.size(); p++) {

            Player player = engine.players.get(p);
            ArrayList<Entity> entities = player.denorms.getListForLabel(Behaviors.BEHAVIOR_CASTS_PROJECTILE);

            for (int e = 0; e < entities.size(); e++) {

                Entity unit = entities.get(e);
                ProjectileCasterComponent pcc = (ProjectileCasterComponent)unit.cData.get(ProjectileCasterComponent.class);

                if (pcc.update(dt)) {
                    Entity projectile = GameEntities.projectilesMemoryPool.fetchMemory();

                    WorldComponent casterWorld = (WorldComponent) unit.cData.get(WorldComponent.class);
                    WorldComponent wc = (WorldComponent) projectile.cData.get(WorldComponent.class);

                    wc.pos.copy(casterWorld.pos);

                    LivingComponent lc = (LivingComponent) projectile.cData.get(LivingComponent.class);
                    lc.hitPoints = 1;

                    VelocityComponent vc = (VelocityComponent)projectile.cData.get(VelocityComponent.class);
                    vc.moveSpeed = 2;

                    DestinationComponent dc = (DestinationComponent) projectile.cData.get(DestinationComponent.class);
                    dc.dest.x = 15 * (casterWorld.rot.x) + casterWorld.pos.x;
                    dc.dest.y = 15 * (casterWorld.rot.y) + casterWorld.pos.y;
                    dc.hasDestination = true;

                    engine.currentPlayer.queueAdded(projectile);
                }

            }
        }
    }
}
