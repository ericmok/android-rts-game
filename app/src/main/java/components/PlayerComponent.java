package components;

/**
 * Created by eric on 11/5/14.
 */
public class PlayerComponent extends Component {

    private static final int TAG_BASE = 1021253;

    public static final int TAG_NEUTRAL_OWNED = TAG_BASE + 0;
    public static final int TAG_PLAYER_OWNED = TAG_BASE + 1;
    public static final int TAG_ALLIED_OWNED = TAG_BASE + 2;
    public static final int TAG_ENEMY_OWNED = TAG_BASE + 3;

    public String name = "default";
    public int team = TAG_PLAYER_OWNED;

    public int tagForTeam(int i) {
        switch (i) {
            case 0:
                return TAG_NEUTRAL_OWNED;
            case 1:
                return TAG_PLAYER_OWNED;
            case 2:
                return TAG_ALLIED_OWNED;
            case 3:
                return TAG_ENEMY_OWNED;
            case 4:
                return TAG_ENEMY_OWNED;
        }
        return TAG_PLAYER_OWNED;
    }
}
