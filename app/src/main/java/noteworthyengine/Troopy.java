package noteworthyengine;

import model.Troop;

/**
 * Created by eric on 3/7/15.
 */
public class Troopy extends Unit {
    public static final String NAME = "Troopy";

    public Troopy() {
        this.name = NAME;

        MovementNode movementNode = new MovementNode(this);
    }
}
