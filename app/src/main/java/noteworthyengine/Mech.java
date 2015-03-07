package noteworthyengine;

/**
 * Created by eric on 3/6/15.
 */
public class Mech {

    public static final String NAME = "Mech";

    public static Unit createMech() {
        Unit mech = new Unit();
        mech.name = NAME;
        MovementNode movementNode = new MovementNode(mech);
        return mech;
    }
}
