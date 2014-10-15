package tenth.system;

import game.androidgame2.Troop;
import game.androidgame2.Vector3;
import tenth.system.FormationSystem.FormationNode;


/**
 * To make the units assemble in an orderly fashion...
 *
 */
public class SquadPosition {
	
	/** The leader of this position */
	public FormationNode leader = null;
		
	/** Location of the position */
	public Vector3 position = new Vector3();
	
	/** The follower of this position. ie. Choose closest troop to move into the position */
	public FormationNode follower = null;
	
	public SquadPosition() {		
	}
}
