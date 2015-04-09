package noteworthyengine;

import java.util.ArrayList;

import noteworthyframework.Coords;
import noteworthyframework.GamerPtr;
import noteworthyframework.Node;
import noteworthyframework.Unit;
import utils.DoublePtr;
import utils.IntegerPtr;
import utils.Orientation;
import utils.Vector2;
import utils.VoidFunc;

/**
 * Created by eric on 3/13/15.
 */
public class FormationNode extends Node {

    public static final String NAME = "formationNode";

    public GamerPtr gamer;

    public Coords coords;
    public Vector2 formationForce;

    public Ptr leader;
    public ArrayList<Vector2> openPositions = new ArrayList<Vector2>(8);

    public FormationNode(Unit unit) {
        super(NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, FormationNode.class, this);
    }

    public static class Ptr {
        public FormationNode v = null;
    }

    public static class FormationSpot {
        FormationLeader formationLeader;

        Vector2 position = new Vector2();
        boolean isOccupied = false;

        public static class Ptr {
            FormationSpot v = null;
        }
    }

    public static class FormationLeader extends Node {
        public static final String NAME = "formationLeader";

        public static Vector2 temp = new Vector2();

        public GamerPtr gamer;
        public Coords coords;
        public IntegerPtr gridX;
        public IntegerPtr gridY;
        public Vector2 formationForce;
        public DoublePtr maxSpeed;

        //public FormationSheep[] sheeps = new FormationSheep[8];

        public ArrayList<Integer> freeIndices = new ArrayList<Integer>() {{
            this.add(-1);
            this.add(1);
            this.add(-2);
            this.add(2);
        }};
        public ArrayList<FormationSheep> sheeps = new ArrayList<>();

        public IntegerPtr maxNumberSheep = new IntegerPtr() {{ v = 1; }};

        public DoublePtr sheepAcquisitionDistance = new DoublePtr() {{ v = 8; }};

        public boolean canTakeSheep() {
            return freeIndices.size() > 0;
        }

        public void calculateSheepPosition(Vector2 output, int index) {
            output.zero();
            coords.rot.getPerpendicular(temp);
            output.x = coords.pos.x + temp.x * 1.4 * index;
            output.y = coords.pos.y + temp.y * 1.4 * index;
            //output.x = coords.pos.x;
            //output.y = coords.pos.y;
//            output.x = 0;
//            output.y = 0;
        }

        public int takeIndex() {
            return freeIndices.remove(0);
        }

        public void removeSheep(FormationSheep sheep) {
            freeIndices.add(sheep.formationIndex.v);
            sheep.leaderToFollow.v = null;
            sheeps.remove(sheep);
        }

        public FormationLeader(Unit unit) {
            super(NAME, unit);
            Node.instantiatePublicFieldsForUnit(unit, FormationLeader.class, this);
        }

        public static class Ptr {
            public FormationLeader v = null;
        }
    }

    public static class FormationSheep extends Node {
        public static final String NAME = "formationSheep";

        public GamerPtr gamer;
        public Coords coords;
        public IntegerPtr gridX;
        public IntegerPtr gridY;
        public Vector2 formationDestination;
        public Vector2 formationForce;
        public DoublePtr maxSpeed;

        public DoublePtr leaderAcquisitionDistance = new DoublePtr() {{ v = 4; }};

        public IntegerPtr formationIndex;
        public FormationLeader.Ptr leaderToFollow;

        public boolean hasLeader() {
            return leaderToFollow.v != null;
        }

        public FormationSheep(Unit unit) {
            super(NAME, unit);
            Node.instantiatePublicFieldsForUnit(unit, FormationSheep.class, this);
        }

        public static class Ptr {
            public FormationSheep v = null;
        }
    }
}
