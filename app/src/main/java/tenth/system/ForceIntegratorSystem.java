package tenth.system;

import java.util.ArrayList;
import java.util.Hashtable;

import android.util.Log;

import game.androidgame2.Game;
import game.androidgame2.Orientation;
import game.androidgame2.Vector3;

public class ForceIntegratorSystem {
	private Game game;
	
	private Vector3 accumulator = new Vector3();
	
	public ForceIntegratorSystem(Game game) {
		this.game = game;
	}
	
	public void update(ArrayList<SystemNode> forceIntegratorNodes, long elapsedTime) {
//		
//		forceIntegratorNodes.resetIterator();
//		while (forceIntegratorNodes.canIterateNext()) {
//			accumulator.zero();
//			
//			ForceIntegratorNode fin = forceIntegratorNodes.getNextIteratorItem();
//			
//			Vector3.add(accumulator, accumulator, fin.formationForce);
//			Vector3.add(accumulator, accumulator, fin.separationForce);
//			accumulator.clampMagnitude(Troop.MAX_FORCE);
//			
//			Vector3.add(fin.velocity, fin.velocity, accumulator);
//			fin.velocity.clampMagnitude(Troop.MAX_MOVE_SPEED);
//			
//			Vector3.scaled(accumulator, fin.velocity, elapsedTime);
//			Vector3.add(fin.position, fin.position, fin.velocity);
//		}
		
		for (int i = 0; i < forceIntegratorNodes.size(); i++) {
			
			ForceIntegratorNode fin = ((ForceIntegratorNodeBindable)forceIntegratorNodes.get(i)).getForceIntegratorNode();
			
			// Not necessary since all the forces to add are zeroed out
			if (!fin.isAlive[0]) {
				fin.velocity.zero();
				continue;
			}
			
			accumulator.zero();
			
			// TODO: Clamp correctly
			accumulator.copy(fin.fieldForce);
			//accumulator.clampMagnitude(Constants.MAX_FORCE);
			
			Vector3.add(accumulator, accumulator, fin.formationForce);
			Vector3.add(accumulator, accumulator, fin.separationForce);
			accumulator.clampMagnitude2d(Constants.MAX_FORCE);

			fin.velocity.copy(accumulator);

			if (fin.velocity.getMagnitude2d() != 0) {
				
				accumulator.setNormalized2d();
				fin.orientation.setNormalized();
				
				// TODO:
				//double dotProduct = accumulator.x * fin.orientation.x + accumulator.y * fin.orientation.y;
				
				// If its negative - the larger the product the smaller the scaling
				//if (dotProduct < 0) {
					//dotProduct = 1 - dotProduct;
				//}
				
				// Min scaling
				//dotProduct = Math.max(dotProduct, 0.0005f);
				
				//dotProduct = dotProduct / 2 + 0.5; // Move range from [-1 to 1] to [0 to 1]
				//Vector3.scaled(fin.velocity, fin.velocity, dotProduct);				
				
				fin.velocity.clampMagnitude2d(Constants.MAX_MOVE_SPEED);
			}
			
			if (Double.isNaN(fin.velocity.getMagnitude2d())) {
				Log.i("ForceIntegratorSystem", "ForceIntegratorSystem TROUBLE");
			}
			
			//Vector3.scaled(accumulator, fin.velocity, elapsedTime);
			//Vector3.add(fin.position, fin.position, accumulator);
			Vector3.scaled(fin.velocity, fin.velocity, elapsedTime);
			Vector3.add(fin.position, fin.position, fin.velocity);
		}
		
		
	}
	
	public interface ForceIntegratorNodeBindable {
		public ForceIntegratorNode getForceIntegratorNode();
	}

	public static class ForceIntegratorNode {
		public Vector3 position;
		public Vector3 velocity;
		
		public Orientation orientation;
		
		public Vector3 separationForce;
		public Vector3 formationForce;
		public Vector3 fieldForce;
		
		public boolean ignore;
		
		public Boolean[] isAlive;
		
		// TODO: Should this be systemnode?
		public ForceIntegratorNodeBindable sourceObject;
		
		public Hashtable<String, Boolean> states;
		
		public ForceIntegratorNode(Vector3 position, Vector3 velocity, Orientation orientation, Vector3 separationForce, Vector3 formationForce, Vector3 fieldForce, ForceIntegratorNodeBindable sourceObject, Boolean[] isAlive, Hashtable<String, Boolean> states) {
			this.position = position;
			this.velocity = velocity;
			this.orientation = orientation;
			
			this.separationForce = separationForce;
			this.formationForce = formationForce;
			this.fieldForce = fieldForce;
			
			this.sourceObject = sourceObject;
			
			this.isAlive = isAlive; 
			
			this.ignore = false;
			
			this.states = states;
		}
	}
}
