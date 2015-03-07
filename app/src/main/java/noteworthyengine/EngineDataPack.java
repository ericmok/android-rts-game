package noteworthyengine;

import org.json.JSONException;

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


    public void addGamer(Gamer gamer) {
        gamers.add(gamer);
    }

    public void removeGamer(Gamer player) {
        gamers.remove(player);
    }

    public void eachGamerAddQueuedUnits() {
        for (int i = 0; i < gamers.size(); i++) {
            Gamer player = gamers.get(i);
            player.addQueued();
        }
    }

    public void eachGanerRemoveQueuedUnits() {
        for (int i = 0; i < gamers.size(); i++) {
            Gamer player = gamers.get(i);
            player.removeQueued();
        }
    }

    public void loadFromJson(String json) throws JSONException {
        EngineDataPackLoader.loadFromJson(this, json);
    }
}
