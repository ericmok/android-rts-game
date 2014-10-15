package tenth.system;

import java.util.ArrayList;

import android.util.Log;

import game.androidgame2.Game;
import game.androidgame2.Orientation;
import game.androidgame2.RewritableArray;
import game.androidgame2.Vector3;

public class FormationSystem {
	private Game game;
	
	//private RewritableArray<SquadPosition> squadPositions;
	private Vector3 accumulator = new Vector3();
	private Vector3 accumulator2 = new Vector3();
	private Vector3 temp = new Vector3();
	private Vector3 temp2 = new Vector3();
	
	public FormationSystem(Game game) {
		this.game = game;
//		squadPositions = new RewritableArray<SquadPosition>(SquadPosition.class, Game.MAX_UNITS * 2);	
//		squadPositions.resetWriteIndex();
	}
	
	// TODO: ???
//	public interface FormationSystemCallback {
//		public FormationNode filter(FormationNode nodeToFilter);
//	}
//	
//	public FormationNode findNextSoldier(ArrayList<SystemNode> nodes) {
//		FormationNode winning = null;
//
//		int counter = 0;
//		while (winning == null && counter < nodes.size()) {
//			FormationNode formationNode = ((FormationNodeBindable)nodes.get(counter)).getFormationNode();
//			if (formationNode.type[0] == FormationNode.Type.SOLDIER) {
//				winning = ((FormationNodeBindable)nodes.get(counter)).getFormationNode();
//			}
//			counter++;
//		}
//
//		return winning;
//	}
	
	public FormationNode findClosest(ArrayList<SystemNode> nodes, Vector3 position) {

		if (nodes.size() == 0) {
			return null;
		}
		
		FormationNode winning = null;
		
		int counter = 0;
//		while (winning == null && counter < nodes.size()) {
//			FormationNode formationNode = ((FormationNodeBindable)nodes.get(counter)).getFormationNode();
//			if (formationNode.type[0] == FormationNode.Type.SOLDIER) {
//				winning = ((FormationNodeBindable)nodes.get(counter)).getFormationNode();
//			}
//			counter++;
//		}
//
//		if (winning == null) return null;
		winning = ((FormationNodeBindable)nodes.get(0)).getFormationNode();
		
		// Initialize to infinity to find smallest distance
		double winningDist = 99999;
		
		for (int i = 0; i < nodes.size(); i++) {
			FormationNode troop = ((FormationNodeBindable)nodes.get(i)).getFormationNode();
			// TODO: Filter this outside the system?
			if (!troop.isAlive[0]) {
				continue;
			}
			double dist = Vector3.getDistanceBetween(position, troop.position);
			if (dist < winningDist) {
				winning = ((FormationNodeBindable)nodes.get(i)).getFormationNode();
				winningDist = dist;
			}
		}
		
		return winning;
	}
	
	public void setupSquadPositionsBasedOnOrientation(ArrayList<SystemNode> leaders, long elapsedTime) {
		for(int i = 0; i < leaders.size(); i++) {
			FormationNode troop = ((FormationNodeBindable)leaders.get(i)).getFormationNode();
			
			troop.squadPositions.resetWriteIndex();
			
			troop.formationForce.zero();
			
			// set up squad positions
			if (troop.isAlive[0]) {
				
				SquadPosition sp = troop.squadPositions.takeNextWritable();
				troop.orientation.getPerpendicular(temp);
				Vector3.scaled(temp, temp, -1.6 * Constants.UNIT_RADIUS);
				//sp.position.x = sptemp.x + troop.position.x + troop.velocity.x * elapsedTime;
				//sp.position.y = sptemp.y + troop.position.y + troop.velocity.y * elapsedTime;
				sp.position.x = temp.x + troop.position.x;
				sp.position.y = temp.y + troop.position.y;
				sp.follower = null;
				sp.leader = troop;

				sp = troop.squadPositions.takeNextWritable();
				troop.orientation.getPerpendicular(temp);
				Vector3.scaled(temp, temp, 1.6 * Constants.UNIT_RADIUS);
				sp.position.x = temp.x + troop.position.x;
				sp.position.y = temp.y + troop.position.y;
				sp.follower = null;
				sp.leader = troop;
				
				sp = troop.squadPositions.takeNextWritable();
				troop.orientation.getPerpendicular(temp);
				Vector3.scaled(temp, temp, -3.2 * Constants.UNIT_RADIUS);
				sp.position.x = temp.x + troop.position.x;
				sp.position.y = temp.y + troop.position.y;
				sp.follower = null;
				sp.leader = troop;

				sp = troop.squadPositions.takeNextWritable();
				troop.orientation.getPerpendicular(temp);
				Vector3.scaled(temp, temp, 3.2 * Constants.UNIT_RADIUS);
				sp.position.x = temp.x + troop.position.x;
				sp.position.y = temp.y + troop.position.y;
				sp.follower = null;
				sp.leader = troop;
				
				sp = troop.squadPositions.takeNextWritable();
				troop.orientation.getPerpendicular(temp);
				Vector3.scaled(temp, temp, -4.8 * Constants.UNIT_RADIUS);
				sp.position.x = temp.x + troop.position.x;
				sp.position.y = temp.y + troop.position.y;
				sp.follower = null;
				sp.leader = troop;

				sp = troop.squadPositions.takeNextWritable();
				troop.orientation.getPerpendicular(temp);
				Vector3.scaled(temp, temp, 4.8 * Constants.UNIT_RADIUS);
				sp.position.x = temp.x + troop.position.x;
				sp.position.y = temp.y + troop.position.y;
				sp.follower = null;
				sp.leader = troop;
				
				
				// Next line
				sp = troop.squadPositions.takeNextWritable();
				//Vector3.getNormalized(accumulator, troop.velocity);
				troop.orientation.setNormalized();
				accumulator.x = troop.orientation.x * 1.6 * Constants.UNIT_RADIUS;
				accumulator.y = troop.orientation.y * 1.6 * Constants.UNIT_RADIUS;
				//Vector3.scaled(accumulator, accumulator, 1.6 * Constants.UNIT_RADIUS);
				sp.position.x = troop.position.x + accumulator.x;
				sp.position.y = troop.position.y + accumulator.y;
				sp.follower = null;
				sp.leader = troop;
				
				sp = troop.squadPositions.takeNextWritable();
				troop.orientation.getPerpendicular(temp);
				Vector3.scaled(temp, temp, -1.6 * Constants.UNIT_RADIUS);
				troop.orientation.setNormalized();
				sp.position.x = temp.x + troop.position.x + accumulator.x;
				sp.position.y = temp.y + troop.position.y + accumulator.y;
				sp.follower = null;
				sp.leader = troop;
				
				sp = troop.squadPositions.takeNextWritable();
				troop.orientation.getPerpendicular(temp);
				Vector3.scaled(temp, temp, 1.6 * Constants.UNIT_RADIUS);
				troop.orientation.setNormalized();
				sp.position.x = temp.x + troop.position.x + accumulator.x;
				sp.position.y = temp.y + troop.position.y + accumulator.y;
				sp.follower = null;
				sp.leader = troop;
				
				sp = troop.squadPositions.takeNextWritable();
				troop.orientation.getPerpendicular(temp);
				Vector3.scaled(temp, temp, -3.2 * Constants.UNIT_RADIUS);
				troop.orientation.setNormalized();
				sp.position.x = temp.x + troop.position.x + accumulator.x;
				sp.position.y = temp.y + troop.position.y + accumulator.y;
				sp.follower = null;
				sp.leader = troop;
				
				sp = troop.squadPositions.takeNextWritable();
				troop.orientation.getPerpendicular(temp);
				Vector3.scaled(temp, temp, 3.2 * Constants.UNIT_RADIUS);
				troop.orientation.setNormalized();
				sp.position.x = temp.x + troop.position.x + accumulator.x;
				sp.position.y = temp.y + troop.position.y + accumulator.y;
				sp.follower = null;
				sp.leader = troop;
				
				sp = troop.squadPositions.takeNextWritable();
				troop.orientation.getPerpendicular(temp);
				Vector3.scaled(temp, temp, -4.8 * Constants.UNIT_RADIUS);
				troop.orientation.setNormalized();
				sp.position.x = temp.x + troop.position.x + accumulator.x;
				sp.position.y = temp.y + troop.position.y + accumulator.y;
				sp.follower = null;
				sp.leader = troop;
				
				sp = troop.squadPositions.takeNextWritable();
				troop.orientation.getPerpendicular(temp);
				Vector3.scaled(temp, temp, 4.8 * Constants.UNIT_RADIUS);
				troop.orientation.setNormalized();
				sp.position.x = temp.x + troop.position.x + accumulator.x;
				sp.position.y = temp.y + troop.position.y + accumulator.y;
				sp.follower = null;
				sp.leader = troop;
				
				
				// Back line
				sp = troop.squadPositions.takeNextWritable();
				//Vector3.getNormalized(accumulator, troop.velocity);
				//Vector3.scaled(accumulator, accumulator, -1.6 * Constants.UNIT_RADIUS);
				troop.orientation.setNormalized();
				accumulator.x = troop.orientation.x * -1.6 * Constants.UNIT_RADIUS;
				accumulator.y = troop.orientation.y * -1.6 * Constants.UNIT_RADIUS;
				sp.position.x = troop.position.x + accumulator.x;
				sp.position.y = troop.position.y + accumulator.y;
				sp.follower = null;
				sp.leader = troop;
				

				sp = troop.squadPositions.takeNextWritable();
				troop.orientation.getPerpendicular(temp);
				Vector3.scaled(temp, temp, -1.6 * Constants.UNIT_RADIUS);
				troop.orientation.setNormalized();
				sp.position.x = temp.x + troop.position.x + accumulator.x;
				sp.position.y = temp.y + troop.position.y + accumulator.y;
				sp.follower = null;
				sp.leader = troop;
		
				sp = troop.squadPositions.takeNextWritable();
				troop.orientation.getPerpendicular(temp);
				Vector3.scaled(temp, temp, 1.6 * Constants.UNIT_RADIUS);
				troop.orientation.setNormalized();
				sp.position.x = temp.x + troop.position.x + accumulator.x;
				sp.position.y = temp.y + troop.position.y + accumulator.y;
				sp.follower = null;
				sp.leader = troop;

			}
		}

	}
	
	private int numberTimes = 0;
	
	public void setupSquadPositions(ArrayList<SystemNode> leaders, long elapsedTime) {

		numberTimes++;
		
		for(int i = 0; i < leaders.size(); i++) {
			FormationNode troop = ((FormationNodeBindable)leaders.get(i)).getFormationNode();
			
			troop.squadPositions.resetWriteIndex();
			
			troop.formationForce.zero();
			
			// set up squad positions
			if (troop.isAlive[0]) {

				if (Double.isNaN(troop.velocity.getMagnitude2d())) {
					Log.i("VEL 0", "VEL: " + troop.velocity.toString());
				}
				
				// If velocity is 0, then use orientation
				if (troop.velocity.getMagnitude2d() < 0.0001f) {
					troop.orientation.getPerpendicular(temp);
					
					temp2.x = troop.orientation.x;
					temp2.y = troop.orientation.y;
				}
				else {
					Orientation.getPerpendicular(temp, troop.velocity);	
					
					temp2.x = troop.velocity.x;
					temp2.y = troop.velocity.y;
				}
				temp2.setNormalized2d();
				
				// TODO: Make this an array list
				SquadPosition sp = troop.squadPositions.takeNextWritable();
				Vector3.scaled(accumulator, temp, -1.6 * Constants.UNIT_RADIUS);
				sp.position.x = accumulator.x + troop.position.x;
				sp.position.y = accumulator.y + troop.position.y;
				sp.follower = null;
				sp.leader = troop;

				sp = troop.squadPositions.takeNextWritable();
				Vector3.scaled(accumulator, temp, 1.6 * Constants.UNIT_RADIUS);
				sp.position.x = accumulator.x + troop.position.x;
				sp.position.y = accumulator.y + troop.position.y;
				sp.follower = null;
				sp.leader = troop;
				
				sp = troop.squadPositions.takeNextWritable();
				Vector3.scaled(accumulator, temp, -3.2 * Constants.UNIT_RADIUS);
				sp.position.x = accumulator.x + troop.position.x;
				sp.position.y = accumulator.y + troop.position.y;
				sp.follower = null;
				sp.leader = troop;

				sp = troop.squadPositions.takeNextWritable();
				troop.orientation.getPerpendicular(temp);
				Vector3.scaled(accumulator, temp, 3.2 * Constants.UNIT_RADIUS);
				sp.position.x = accumulator.x + troop.position.x;
				sp.position.y = accumulator.y + troop.position.y;
				sp.follower = null;
				sp.leader = troop;
				
				
				// Next Line
				sp = troop.squadPositions.takeNextWritable();
				accumulator.x = temp2.x * 1.6 * Constants.UNIT_RADIUS;
				accumulator.y = temp2.y * 1.6 * Constants.UNIT_RADIUS;	
				sp.position.x = troop.position.x + accumulator.x;
				sp.position.y = troop.position.y + accumulator.y;
				sp.follower = null;
				sp.leader = troop;
				
				sp = troop.squadPositions.takeNextWritable();
				Vector3.scaled(accumulator, temp, -1.6 * Constants.UNIT_RADIUS);
				Vector3.scaled(accumulator2, temp2, 1.6 * Constants.UNIT_RADIUS);
				sp.position.x = accumulator.x + troop.position.x + accumulator2.x;
				sp.position.y = accumulator.y + troop.position.y + accumulator2.y;
				sp.follower = null;
				sp.leader = troop;
				
				sp = troop.squadPositions.takeNextWritable();
				Vector3.scaled(accumulator, temp, 1.6 * Constants.UNIT_RADIUS);
				Vector3.scaled(accumulator2, temp2, 1.6 * Constants.UNIT_RADIUS);
				sp.position.x = accumulator.x + troop.position.x + accumulator2.x;
				sp.position.y = accumulator.y + troop.position.y + accumulator2.y;
				sp.follower = null;
				sp.leader = troop;
				
				sp = troop.squadPositions.takeNextWritable();
				Vector3.scaled(accumulator, temp, -3.2 * Constants.UNIT_RADIUS);
				Vector3.scaled(accumulator2, temp2, 1.6 * Constants.UNIT_RADIUS);
				sp.position.x = accumulator.x + troop.position.x + accumulator2.x;
				sp.position.y = accumulator.y + troop.position.y + accumulator2.y;
				sp.follower = null;
				sp.leader = troop;
				
				sp = troop.squadPositions.takeNextWritable();
				Vector3.scaled(accumulator, temp, 3.2 * Constants.UNIT_RADIUS);
				Vector3.scaled(accumulator2, temp2, 1.6 * Constants.UNIT_RADIUS);
				sp.position.x = accumulator.x + troop.position.x + accumulator2.x;
				sp.position.y = accumulator.y + troop.position.y + accumulator2.y;
				sp.follower = null;
				sp.leader = troop;
				
				
				// 2 Next Line
				sp = troop.squadPositions.takeNextWritable();
				accumulator.x = temp2.x * 3.2 * Constants.UNIT_RADIUS;
				accumulator.y = temp2.y * -1.6 * Constants.UNIT_RADIUS;	
				sp.position.x = troop.position.x + accumulator.x;
				sp.position.y = troop.position.y + accumulator.y;
				sp.follower = null;
				sp.leader = troop;
				
				sp = troop.squadPositions.takeNextWritable();
				Vector3.scaled(accumulator, temp, -1.6 * Constants.UNIT_RADIUS);
				Vector3.scaled(accumulator2, temp2, -1.6 * Constants.UNIT_RADIUS);
				sp.position.x = accumulator.x + troop.position.x + accumulator2.x;
				sp.position.y = accumulator.y + troop.position.y + accumulator2.y;
				sp.follower = null;
				sp.leader = troop;
				
				sp = troop.squadPositions.takeNextWritable();
				Vector3.scaled(accumulator, temp, 1.6 * Constants.UNIT_RADIUS);
				Vector3.scaled(accumulator2, temp2, -1.6 * Constants.UNIT_RADIUS);
				sp.position.x = accumulator.x + troop.position.x + accumulator2.x;
				sp.position.y = accumulator.y + troop.position.y + accumulator2.y;
				sp.follower = null;
				sp.leader = troop;
				
				sp = troop.squadPositions.takeNextWritable();
				Vector3.scaled(accumulator, temp, -3.2 * Constants.UNIT_RADIUS);
				Vector3.scaled(accumulator2, temp2, -1.6 * Constants.UNIT_RADIUS);
				sp.position.x = accumulator.x + troop.position.x + accumulator2.x;
				sp.position.y = accumulator.y + troop.position.y + accumulator2.y;
				sp.follower = null;
				sp.leader = troop;
				
				sp = troop.squadPositions.takeNextWritable();
				Vector3.scaled(accumulator, temp, 3.2 * Constants.UNIT_RADIUS);
				Vector3.scaled(accumulator2, temp2, -1.6 * Constants.UNIT_RADIUS);
				sp.position.x = accumulator.x + troop.position.x + accumulator2.x;
				sp.position.y = accumulator.y + troop.position.y + accumulator2.y;
				sp.follower = null;
				sp.leader = troop;
			}
		}

	}
	


	public ArrayList<SystemNode> update(ArrayList<SystemNode> leaders, ArrayList<SystemNode> sheep, long elapsedTime) {

		setupSquadPositions(leaders, elapsedTime);
		
		// Reset allocations
		for (int k = 0; k < sheep.size(); k++) {
			FormationNode troop = ((FormationNodeBindable)sheep.get(k)).getFormationNode();
			troop.squadPositionToFollow = null;
			troop.formationForce.zero();
		}
		
		for (int j = 0; j < leaders.size(); j++) {
			FormationNode troop = ((FormationNodeBindable)leaders.get(j)).getFormationNode();
			
			for (int i = 0; i < troop.squadPositions.size(); i++) {			
				FormationNode closestTroop = findClosest(sheep, troop.squadPositions.get(i).position);
	
				// TODO: Makeshift
				if (closestTroop == null || closestTroop.squadPositionToFollow != null) {
					continue;
				}
				
				Vector3.subtract(accumulator, troop.squadPositions.get(i).position, closestTroop.position);
				//Vector3.getNormalized(accumulator, accumulator);
				double dist = Vector3.getDistanceBetween(closestTroop.position, troop.squadPositions.get(i).position);
				
				
				// More force the farther away, the closer it is, less force
				double screenDivisions = 1;
				//Vector3.scaled(accumulator, accumulator, (100 * (elapsedTime/1000.0f)) * (dist / screenDivisions));
				//accumulator.clampMagnitude(Troop.MAX_MOVE_SPEED);
				
				Vector3.getNormalized2d(accumulator, accumulator);
				if (elapsedTime == 0) {
					elapsedTime = 1;
				}
				Vector3.scaled(accumulator, accumulator, dist / (0.9 * elapsedTime) );

				troop.squadPositions.get(i).follower = troop;
				closestTroop.squadPositionToFollow = troop.squadPositions.get(i);
				
				closestTroop.formationForce.copy(accumulator);
			}
		}

		
		return leaders;
	}
	
	
	public ArrayList<SystemNode> update2(ArrayList<SystemNode> troops, long elapsedTime) throws Exception {

		setupSquadPositions(troops, elapsedTime);
		
		for (int j = 0; j < troops.size(); j++) {
			FormationNode troop = ((FormationNodeBindable)troops.get(j)).getFormationNode();
			
			for (int i = 0; i < troop.squadPositions.size(); i++) {			
				FormationNode closestTroop = findClosest(troops, troop.squadPositions.get(i).position);
				
				// TODO: Makeshift
				if (closestTroop == null) {
					continue;
				}
				Vector3.subtract(accumulator, troop.squadPositions.get(i).position, closestTroop.position);
				//Vector3.getNormalized(accumulator, accumulator);
				double dist = Vector3.getDistanceBetween(closestTroop.position, troop.squadPositions.get(i).position);
				
				// More force the farther away, the closer it is, less force
				double screenDivisions = 1;
				Vector3.scaled(accumulator, accumulator, dist);
				//Vector3.scaled(accumulator, accumulator, (5 * (elapsedTime/1000.0f)) * (dist / screenDivisions));
				//accumulator.clampMagnitude(Troop.MAX_MOVE_SPEED);

				// Add attractive squad position force to velocity
				//Vector3.scaled(closestTroop.velocity, closestTroop.velocity, Troop.VELOCITY_DRAG);
				//Vector3.add(closestTroop.velocity, closestTroop.velocity, accumulator);
				// closestTroop.position.copy(troop.squadPositions.get(i).position);
				
				closestTroop.formationForce.copy(accumulator);
			}
		}

		
		return troops;
	}
	
	public void old() {
//		
//		game.gamePool.vector3s.recycleMemory(sptemp);
//		
//		for (int i = 0; i < troops.size(); i++) {
//			
//			Troop troop = (Troop)troops.get(i);
//			
//			if (troop.state == Troop.State.DEAD || troop.type == Troop.Type.BIG_TROOP) {
//				continue;
//			}
//
//			troop.formationForce.zero();
//			
//			for (int t = 0; t < squadPositions.size(); t++) {
//				squadPositions.get(t).winning = troop;
//			}
//			
//			for (int t = 0; t < squadPositions.size(); t++) {
//
//				Vector3 temp = game.gamePool.vector3s.fetchMemory();
//
//				double dist = Vector3.getSquaredDistanceBetween(troop.position, squadPositions.get(t).position);
//				dist += 0.00001;
//
//				
//				Vector3.subtract(temp, squadPositions.get(t).position, troop.position);
//				
//				Vector3.getNormalized(temp, temp);
//
//
//				Vector3.scaled(temp, temp, 0.00001 / dist);	
//
//				
//				Vector3.add(troop.formationForce, troop.formationForce, temp);
//					
//				game.gamePool.vector3s.recycleMemory(temp);
//					
//			}
//			
//			Vector3.scaled(troop.velocity, troop.velocity, Troop.VELOCITY_DRAG);
//	
//			Vector3.add(troop.velocity, troop.velocity, troop.formationForce);
//			
//			troop.velocity.clampMagnitude(Troop.MAX_MOVE_SPEED);
//			
//			// Update
//			Vector3.add(troop.position, troop.position, troop.velocity);
//			
//		}
	}
	
//	public RewritableArray<SquadPosition> getSquadPositions() {
//		return squadPositions;
//	}
	
	public static class FormationNode {
		public Vector3 position;
		public Vector3 velocity;
		public Orientation orientation;
		
		public Vector3 formationForce;

		public Boolean[] isAlive;
		
		public enum Type {
			PAWN,
			BISHOP,
			ROOK,
			KING
		};

		public SquadPosition squadPositionToFollow = null;
		public RewritableArray<SquadPosition> squadPositions = null;
		
		public FormationNode(Vector3 inPosition, Vector3 inVelocity, Orientation inOrientation, Vector3 inFormationForce, Boolean[] isAlive, SquadPosition squadPositionToFollow, RewritableArray<SquadPosition> squadPositions) {
			this.position = inPosition;
			this.velocity = inVelocity;
			this.orientation = inOrientation;
			this.formationForce = inFormationForce;
			this.isAlive = isAlive;
			this.squadPositions = squadPositions;
			this.squadPositionToFollow = squadPositionToFollow;
		}
	}
	
	public static interface FormationNodeBindable {
		public FormationNode getFormationNode();
	}
}
