package tenth.system;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * TODO
 * Concerns: We still loop over all game objects per system...
 */
public abstract class SystemNode {
	public enum NodeType {
		FORMATION,
		SEPARATION,
		FIELD_MOVEMENT,
		FORCE_INTEGRATOR,
		BATTLE, 
		TROOP_DRAW,
		SHIP_DRAW,
		ORIENTATION,
		SELECTION
	};

    public enum Label {
        Troop,
        SmallShip
    };

    public abstract HashSet<NodeType> getNodeTypes();

    protected  HashSet<Label> labels = new HashSet<Label>(16);

    public HashSet<Label> getLabels() {
        return labels;
    }
}
