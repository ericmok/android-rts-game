package tenth.system;

import java.util.ArrayList;

import game.androidgame2.Game;
import game.androidgame2.Orientation;
import game.androidgame2.Vector3;
import tenth.system.ForceIntegratorSystem.ForceIntegratorNode;
import tenth.system.ForceIntegratorSystem.ForceIntegratorNodeBindable;
import tenth.system.SystemNode.NodeType;

/**
 * To resolve orientations
 * 
 */
public class OrientationSystem {
	private Game game;
	private Vector3 accumulator = new Vector3();
	
	public OrientationSystem(Game game) {
		this.game = game;	
	}

	/**
	 * Takes force integrator nodes
	 * @param units
	 * @param elapsedTime
	 */
	public void update(ArrayList<SystemNode> units, long elapsedTime) {
		for (int i = 0; i < units.size(); i++) {
			
			if (!units.get(i).getNodes().contains(NodeType.ORIENTATION)) {
				return;
			}
			
			ForceIntegratorNode fin = ((ForceIntegratorNodeBindable)units.get(i)).getForceIntegratorNode();
			
			
			if (fin.formationForce.getMagnitude2d() < Constants.UNIT_RADIUS * 0.8) {
				accumulator.copy(fin.fieldForce);
			}
			else {
				Vector3.add(accumulator, fin.fieldForce, fin.formationForce);
			}
			
			Vector3.getNormalized2d(accumulator, accumulator);
			fin.orientation.addVector(accumulator.x, accumulator.y, 2);
			
			// orientation node and velocity
			//OrientationNode fin = ((OrientationNodeBindable)units.get(i)).getOrientationNode();
//			if (fin.velocity.getMagnitude2d() > 0.001f) {
//				accumulator.copy(fin.velocity);
//				accumulator.setNormalized2d();
//				fin.orientation.addVector(accumulator.x, accumulator.y, fin.turningRate[0] * elapsedTime);
//			}
								
			
			// Discretization test
//			double dx = fin.orientation.getDegrees() % 2;
//			fin.orientation.setDegrees(fin.orientation.getDegrees() - dx);
//			fin.orientation.setNormalized();
		}
	}

	
	public static class OrientationNode {
		public Vector3 velocity;
		public Orientation orientation;
		public float[] turningRate;
		
		public OrientationNode(Vector3 velocity, Orientation orientation, float[] turningRate) {
			this.velocity = velocity;
			this.orientation = orientation;
			this.turningRate = turningRate;
		}
	}
	
	public interface OrientationNodeBindable {
		public OrientationNode getOrientationNode();
	}
}
