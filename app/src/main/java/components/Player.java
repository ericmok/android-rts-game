package components;

import android.graphics.Color;

import java.util.ArrayList;

/**
 * Created by eric on 11/5/14.
 */
public class Player {
    public static ArrayList<Integer> TeamColors = new ArrayList<Integer>() {{
       this.add(Color.argb(240, 0, 201, 255));
       this.add(Color.argb(240, 198, 71, 132));

       // From flatcolors
       this.add(Color.argb(240, 241, 196, 15)); // Sunflower
       this.add(Color.argb(240, 44, 62, 80)); // Midnight blue
       this.add(Color.argb(240, 46, 204, 113)); // Emerald
       this.add(Color.argb(240, 155, 89, 182)); // Amethyst
    }};

    public String name = "player_" + Math.floor(Math.random() * 10000);
    public int team = 0;
    public DenormalizedDataSet<Entity, Integer> denorms = new DenormalizedDataSet<Entity, Integer>(32, 300);

    public Player(String name) {
        this.name = name;
    }

    public boolean equals(Player otherPlayer) {
        return this.name == otherPlayer.name;
    }

    public int color() {
        return colorForTeam(team);
    }

    public static int colorForTeam(int team) {
        return TeamColors.get(team % TeamColors.size());
    }
}
