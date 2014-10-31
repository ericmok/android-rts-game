package tenth.system;

import java.util.ArrayList;

import game.androidgame2.RewriteOnlyArray;
import game.androidgame2.Game;
import utils.Orientation;
import game.androidgame2.Vector3;

public class Formation2System {
	private Game game;
	
	private Vector3 accumulator = new Vector3();
	private Vector3 temp = new Vector3();
	
	public Formation2System(Game game) {
		this.game = game;
	}
	
	public void update(ArrayList<SystemNode> nodes, long elapsedTime) {
		
	}
	
	public static class FormationNode {
		public Vector3 position;
		public Vector3 velocity;
		public Orientation orientation;
		
		public Vector3 formationForce;

		public Boolean[] isAlive;
		
		public RewriteOnlyArray<SquadPosition> squadPositions = null;;
		
		public FormationNode(Vector3 inPosition, Vector3 inVelocity, Orientation inOrientation, Vector3 inFormationForce, Boolean[] isAlive, RewriteOnlyArray<SquadPosition> squadPositions) {
			this.position = inPosition;
			this.velocity = inVelocity;
			this.orientation = inOrientation;
			this.formationForce = inFormationForce;
			this.isAlive = isAlive;
			this.squadPositions = squadPositions;
		}
	}
	
	public static interface FormationNodeBindable {
		public FormationNode getFormationNode();
	}
}
