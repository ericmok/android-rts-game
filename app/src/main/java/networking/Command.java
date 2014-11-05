package networking;

import game.androidgame2.Vector2;

/**
 * Created by eric on 11/5/14.
 */
public class Command {

    public static final String NONE = "none";
    public static final String MOVE = "move";

    public float timeStamp = 0;

    public String command = NONE;
    public int unitId = 0;
    public Vector2 vec = new Vector2();
}
