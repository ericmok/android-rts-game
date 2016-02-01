package noteworthyframework;

import android.graphics.Color;

import java.util.ArrayList;

/**
 * Created by eric on 3/6/15.
 */
public class Gamer {

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
