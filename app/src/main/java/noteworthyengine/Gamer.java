package noteworthyengine;

import android.graphics.Color;

import java.util.ArrayList;

import model.DenormalizedDataSet;

/**
 * Created by eric on 3/6/15.
 */
public class Gamer {

    public static ArrayList<Integer> TeamColors = new ArrayList<Integer>() {{
        this.add(Color.argb(240, 0, 201, 255));
        this.add(Color.argb(240, 198, 71, 132));

        // From flatcolors
        this.add(Color.argb(240, 46, 204, 113)); // Emerald
        this.add(Color.argb(240, 44, 62, 80)); // Midnight blue
        this.add(Color.argb(240, 230, 126, 34)); // Carrot
        this.add(Color.argb(240, 155, 89, 182)); // Amethyst
        this.add(Color.argb(240, 241, 196, 15)); // Sunflower
    }};

    public ArrayList<Unit> unitsToAdd = new ArrayList<Unit>(127);
    public ArrayList<Unit> unitsToRemove = new ArrayList<Unit>(127);
    public ArrayList<Unit> units = new ArrayList<Unit>(127);

    public String name = "player_" + Math.floor(Math.random() * 10000);
    public int team = 0;

    // Player-wise filters permit higher resolution filtering
    public DenormalizedDataSet<Unit, Integer> filters = new DenormalizedDataSet<Unit, Integer>(32, 300);


    public Gamer(String name) {
        this.name = name;
    }

    public boolean equals(Gamer otherPlayer) {
        return this.name == otherPlayer.name;
    }

    public int color() {
        return colorForTeam(team);
    }

    public static int colorForTeam(int team) {
        return TeamColors.get(team % TeamColors.size());
    }

    public void queueAdded(Unit entity) {
        //entity.event = Entity.Event.ADDED;
        unitsToAdd.add(entity);
    }

    public void queueRemoved(Unit entity) {
        //entity.event = Entity.Event.REMOVED;
        this.unitsToRemove.add(entity);
    }

    public void addQueued() {
        for (int j = 0; j < unitsToAdd.size(); j++) {
            Unit toAdd = unitsToAdd.get(j);

            // Add entity to denormalization mechanism
            this.filters.addDenormalizable(toAdd);
        }
        unitsToAdd.clear();
    }

    public void removeQueued() {
        for (int j = 0; j < unitsToRemove.size(); j++) {
            Unit toRemove = unitsToRemove.get(j);

            // Remove entity from denormalization mechanism
            this.filters.removeDenormalizable(toRemove);

            // TODO: Recycle
            //GameEntities.recycle(toRemove);
            //UnitPool.recycle(toRemove);
        }
        unitsToRemove.clear();
    }
}
