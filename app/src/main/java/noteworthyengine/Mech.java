package noteworthyengine;

/**
 * Created by eric on 3/6/15.
 */
public class Mech extends Unit {

    public static final String NAME = "Mech";

    public Mech() {
        this.name = NAME;
        MovementNode movementNode = new MovementNode(this);
    }

    public static Unit createMech() {
        return new Mech();
    }
}
