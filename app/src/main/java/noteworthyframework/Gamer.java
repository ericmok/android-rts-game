package noteworthyframework;

import android.graphics.Color;

import java.util.ArrayList;

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

    public String name = "player_" + Math.floor(Math.random() * 10000);
    public int team = 0;

    public Gamer() {
    }

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

    public Gamer copy(Gamer other) {
        this.name = other.name;
        this.team = other.team;

        return this;
    }

    @Override
    // TODO: Test
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    // TODO: Test
    public boolean equals(Object o) {
        if (o instanceof  Gamer) {
            return this.name == ((Gamer) o).name;
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}