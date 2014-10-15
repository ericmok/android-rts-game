package tenth.system;

import java.util.ArrayList;
import java.util.Hashtable;

import game.androidgame2.Game;
import game.androidgame2.Orientation;
import game.androidgame2.Vector3;

public class SeparationSystem {
	private Game game;

	private Vector3 accumulator;
	
	public SeparationSystem(Game game) {
		this.game = game;
		this.accumulator = new Vector3();
	}

	/**
	 * Makes the src run away from neighbor
	 * @param separationSystemNodeBindables
	 * @param elapsedTime
	 */
	public void update(ArrayList<SystemNode> separationSystemNodes, long elapsedTime) {
		
		for (int i = 0; i < separationSystemNodes.size(); i++) {
			SeparationNode src = ((SeparationNodeBindable)separationSystemNodes.get(i)).getSeparationSystemNode();
			src.separationForce.zero();
			this.accumulator.zero();
			
			if (!src.isAlive[0]) {
				continue;
			}
			
			// For each neighbor, calculate aggregated separation force
			for (int j = 0; j < separationSystemNodes.size(); j++) {
				SeparationNode neighbor = ((SeparationNodeBindable)separationSystemNodes.get(j)).getSeparationSystemNode();
				
				double dist = 0.00000001 + Vector3.getSquaredDistanceBetween2d(src.position, neighbor.position);

				if (dist > Constants.UNIT_RADIUS * 1.9f) {
					continue;
				}
				
				// B
				dist = 1000 * 1000 * 100 * dist * dist; // in one second
				
				// Stronger spike
				//dist = 0.000002 * dist * dist;
							
				// separation force increase with distance...?
				Vector3.subtract(accumulator, src.position, neighbor.position);
				
				// Scaling the force with respect to elapsed Time
				// A
				//Vector3.scaled(accumulator, accumulator, 0.001 * elapsedTime);
				//Vector3.scaled(accumulator, accumulator, 0.00003 / dist);
				
				
				Vector3.scaled(accumulator, accumulator, 1.0 / dist);
				
				Vector3.add(src.separationForce, src.separationForce, accumulator);
			}
		}
	}
	
	/**
	 * Stores pointers to the actual data
	 */
	public static class SeparationNode {
		
		/** Pointer to position */
		public Vector3 position;
		
		/** Pointer to velocity */
		public Vector3 velocity;
		
		public Vector3 separationForce;
		
		/** Pointer to orientation */
		public Orientation orientation;
		
		/** Pointer to the Object that is represented by this node */
		public SeparationNodeBindable sourceObject;
		
		public float[] leadershipPropensity;
		
		public Boolean[] isAlive;
		
		public boolean ignore;
		
		public Hashtable<String, Boolean> states;
		
		/** Sets everything to null. */
		public SeparationNode(Vector3 position, Vector3 velocity, Orientation orientation, Vector3 separationForce, float[] leadershipPropensity, Boolean[] isAlive, Hashtable<String, Boolean> states, SeparationNodeBindable sourceObject) {
			this.position = position;
			this.velocity = velocity;
			this.orientation = orientation;
			
			this.separationForce = separationForce;
			
			this.leadershipPropensity = leadershipPropensity;
			this.isAlive = isAlive;
			this.states = states;

			this.sourceObject = sourceObject;
			this.ignore = false;
		}
	}
	
	
	public interface SeparationNodeBindable {
		/** Component with binders to associated object */
		public SeparationNode getSeparationSystemNode();
	}

}
