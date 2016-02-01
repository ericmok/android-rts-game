package art;

import android.graphics.Color;

import java.util.ArrayList;

/**
 * Created by eric on 2/1/16.
 */
public class Constants {
    public static ArrayList<Integer> TeamColors = new ArrayList<Integer>() {{
        this.add(Color.argb(250, 0, 191, 255));
        this.add(Color.argb(250, 208, 71, 132));
        this.add(Color.argb(250, 46, 204, 103)); // Emerald

        // From flatcolors
        this.add(Color.argb(250, 230, 126, 34)); // Carrot
        this.add(Color.argb(250, 155, 89, 182)); // Amethyst
        this.add(Color.argb(250, 241, 196, 15)); // Sunflower
        this.add(Color.argb(250, 189, 195, 199)); // Silver
    }};

    public static int colorForTeam(int team) {
        return TeamColors.get(team % TeamColors.size());
    }
}
