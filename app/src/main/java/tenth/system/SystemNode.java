package tenth.system;

import java.util.HashSet;

/**
 * TODO
 * Concerns: We still loop over all game objects per system...
 */
public interface SystemNode {
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
	
	public HashSet<NodeType> getNodes();

	/**
	 * For Memory Pools
	 * This will lower explicitness...
	 * @return
	 */
	public HashSet<String> getTags();
	
	
}
