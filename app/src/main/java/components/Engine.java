package components;

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

    public Player currentPlayer = new Player("self");

    public double gameTime = 0;

    public Engine() {
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }
}
