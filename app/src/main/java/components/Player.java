package components;

import android.graphics.Color;

import java.util.ArrayList;

/**
 * Created by eric on 11/5/14.
 */
public class Player {
    public static ArrayList<Integer> TeamColors = new ArrayList<Integer>() {{
       this.add(Color.WHITE);
       this.add(Color.RED);
       this.add(Color.YELLOW);
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
        return TeamColors.get(team);
    }
}
