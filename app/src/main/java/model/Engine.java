package model;

import java.util.ArrayList;

/**
 * Created by eric on 10/30/14.
 */
public class Engine {

    /**
     * Entities organized by logic-y stuff
     */
    public ArrayList<Player> players = new ArrayList<Player>(16);

    //public DenormalizedDataSet<Entity, Integer> entityDenormalizer;

    public Player currentPlayer;

    public double gameTime = 0;

    public Engine() {
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public void eachPlayerProcessAdded() {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            player.addQueued();
        }
    }

    public void eachPlayerProcessRemoved() {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            player.removeQueued();
        }
    }
}
