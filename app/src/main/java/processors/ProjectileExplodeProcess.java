package processors;

import java.util.ArrayList;

import model.Behaviors;
import model.Engine;
import model.LivingComponent;
import model.Player;
import model.Entity;
import model.RadiusComponent;
import model.WorldComponent;

/**
 * Created by eric on 11/14/14.
 */
public class ProjectileExplodeProcess {

    public static final void process(Engine engine, double dt) {

        for (int p1 = 0; p1 < engine.players.size(); p1++) {

            Player player1 = engine.players.get(p1);
            ArrayList<Entity> projectiles1 = player1.denorms.getListForLabel(Behaviors.UNIT_BASIC_PROJECTILE);

            for (int p2 = 0; p2 < engine.players.size(); p2++) {

                if (p1 == p2) continue;

                Player player2 = engine.players.get(p2);

                ArrayList<Entity> collidableEntities = player2.denorms.getListForLabel(Behaviors.BEHAVIOR_TAKES_DAMAGE_ON_COLLISION);

                for (int projectileCounter = 0; projectileCounter < projectiles1.size(); projectileCounter++) {
                    for (int u = 0; u < collidableEntities.size(); u++) {

                        Entity projectile = projectiles1.get(projectileCounter);
                        Entity collided = collidableEntities.get(u);

                        WorldComponent wc = (WorldComponent)collided.cData.get(WorldComponent.class);
                        WorldComponent projectileWc = (WorldComponent)projectile.cData.get(WorldComponent.class);

                        LivingComponent projectileLC = (LivingComponent)projectile.cData.get(LivingComponent.class);

                        RadiusComponent rc = (RadiusComponent)collided.cData.get(RadiusComponent.class);

                        if (wc.pos.distanceTo(projectileWc.pos) < rc.radius) {

                            LivingComponent lc = (LivingComponent)collided.cData.get(LivingComponent.class);
                            lc.takeDamage(1);

                            projectileLC.takeDamage(100);

                            //player1.queueRemoved(projectile);
                        }
                    }
                }

            }
        }

    }
}
