package networking;

import java.util.ArrayList;

import utils.Vector2;
import model.Entity;

/**
 * Created by eric on 11/5/14.
 */
public class Command {

    public static final String NONE = "none";
    public static final String MOVE = "move";

    public double timeStamp = 0;

    public String command = NONE;
    public ArrayList<Entity> selection = new ArrayList<Entity>(100);
    public Vector2 vec = new Vector2();
}
