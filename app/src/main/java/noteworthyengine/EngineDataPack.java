package noteworthyengine;

import java.util.ArrayList;

/**
 * Created by eric on 3/6/15.
 */
public class EngineDataPack {

    public ArrayList<Gamer> gamers = new ArrayList<Gamer>(8);
    public double gameTime = 0;

    public Gamer currentGamer;

    public EngineDataPack() {
    }


    public void addPlayer(Gamer gamer) {
        gamers.add(gamer);
    }

    public void removePlayer(Gamer player) {
        gamers.remove(player);
    }

    public void eachPlayerProcessAdded() {
        for (int i = 0; i < gamers.size(); i++) {
            Gamer player = gamers.get(i);
            player.addQueued();
        }
    }

    public void eachPlayerProcessRemoved() {
        for (int i = 0; i < gamers.size(); i++) {
            Gamer player = gamers.get(i);
            player.removeQueued();
        }
    }
}
