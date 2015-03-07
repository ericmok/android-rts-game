package noteworthyengine;

import android.util.Log;

import java.lang.reflect.*;

import utils.Vector2;

/**
 * Created by eric on 3/6/15.
 */
public class MovementNode extends Node {

//    public final Field[] fields = {
//        new Field("coords", Coords.class, null)
//    };

    public Coords coords;

    public Vector2 velocity;
    public Vector2 acceleration;

    public Vector2 fieldForce;
    public Vector2 formationForce;
    public Vector2 separationForce;
    public Vector2 enemyAttractionForce;

    public double crowdSpeed = 1;

    public MovementNode(Unit unit) {
        super(unit);

        Node.addUnitFieldsForNode(unit, MovementNode.class, this);
        //this.coords = (Coords) unit.field("coords");


//        for (int i = 0; i < this.fields.length; i++) {
//            this.fields[i].value = unit.field(fields[i].name);
//        }
    }

//    @Override
//    public Field[] getFields() {
//        return fields;
//    }
}
