package processors;

import java.util.ArrayList;

import model.Behaviors;
import model.Engine;
import model.LivingComponent;
import model.Player;
import model.Entity;

/**
 * Created by eric on 11/12/14.
 */
public class DieOnNoHpProcess {

    public static void process(Engine engine, double dt) {

        for (int p = 0; p < engine.players.size(); p++) {
            Player player = engine.players.get(p);

            ArrayList<Entity> entitiesToDie = player.denorms.getListForLabel(Behaviors.BEHAVIOR_TAKES_DAMAGE_ON_COLLISION);

            for (int i = 0; i < entitiesToDie.size(); i++) {
                Entity toDie = entitiesToDie.get(i);

                LivingComponent lc = (LivingComponent)toDie.cData.get(LivingComponent.class);

                if (lc.hitPoints <= 0) {
                    player.queueRemoved(toDie);
                }
            }
        }
    }
}