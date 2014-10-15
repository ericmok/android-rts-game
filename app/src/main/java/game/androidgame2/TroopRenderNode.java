package game.androidgame2;

/**
 * Here is how it works. A subset of Troop data gets copied to a render node - 
 * info that is useful for rendering and nothing else. This limits
 * data that needs to be copied, decreases memory requirements, etc.
 * Drawing code can be arbitrarily complex, including text and sprites and all that...
 * Helper utility functions increase usability of code.
 * 
 * A possible scheme of increasing flexibility is to have nodes return a draw list.
 * The draw list is a list of render items that are sprites.
 * Question: Is there anything more than sprites needed? Maybe text? Maybe generated point fx?
 * Changing material? etc etc....
 * 
 * What about scalability to 3D meshes?
 * 
 * One other thing, these are all nodes with common interface.
 * 
 * DEPRECATED
 */
public class TroopRenderNode {
	public Vector3 position;
	public Vector3 velocity;
	
	public float radius;
	
	public float angle;
	
	public Troop.Type type = Troop.Type.BIG_TROOP;
	//public Troop.Team team = Troop.Team.TEAM_0;
	
	public Troop.State state = Troop.State.IDLE;
	public float stateProgress = 0.0f;
	
	public TroopRenderNode() {
		position = new Vector3();
		velocity = new Vector3();
		
		radius = 0;
		angle = 0;
	}
}
