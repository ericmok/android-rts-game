package processors;

import java.util.ArrayList;

import model.Engine;
import model.Entity;
import model.LivingComponent;
import model.Player;
import model.WorldComponent;

/**
 * Created by eric on 11/7/14.
 */
public class BattleResolution {

    public static void process(Engine engine, double dt) {

        for (int i = 0; i < engine.players.size(); i++) {
            Player player1 = engine.players.get(i);

            ArrayList<Entity> troops1 = player1.denorms.getListForLabel(Entity.UNIT_TROOP);

            for (int j = 0; j < engine.players.size(); j++) {

                if (i == j) continue;

                Player player2 = engine.players.get(j);

                ArrayList<Entity> troops2 = player2.denorms.getListForLabel(Entity.UNIT_TROOP);

                for (int s = 0; s < troops1.size(); s++) {
                    for (int t = 0; t < troops2.size(); t++) {
                        WorldComponent wc1 = (WorldComponent)troops1.get(s).cData.get(WorldComponent.class);
                        WorldComponent wc2 = (WorldComponent)troops2.get(t).cData.get(WorldComponent.class);

                        if (wc1.pos.distanceTo(wc2.pos) < 2) {
                            LivingComponent lc1 = (LivingComponent)troops1.get(s).cData.get(LivingComponent.class);
                            LivingComponent lc2 = (LivingComponent)troops2.get(t).cData.get(LivingComponent.class);
                            lc1.hitPoints = 0;
                            lc2.hitPoints = 0;

                            player1.queueRemoved(troops1.get(s));
                            player2.queueRemoved(troops2.get(t));
                        }
                    }
                }
            }
        }
    }

    public static void cohere() {

    }
}
