package tenth.system;

import java.util.ArrayList;
import java.util.Hashtable;

import game.androidgame2.Game;
import utils.Orientation;
import game.androidgame2.Vector3;

public class BattleSystem {
	private Game game;
	
	public BattleSystem(Game game) {
		this.game = game;
	}
	
	public void update(ArrayList<SystemNode> userUnits, ArrayList<SystemNode> otherUnits, long elapsedTime) {
		
		for (int i = 0; i < userUnits.size(); i++) {
			BattleNode troop = (BattleNode) ((BattleNodeBindable)userUnits.get(i)).getBattleNode();
			
			if (troop.states.contains(States.DEAD)) {
				continue;
			}
			
			for (int j = 0; j < otherUnits.size(); j++) {
				
				BattleNode enemyTroop = (BattleNode) ((BattleNodeBindable)otherUnits.get(j)).getBattleNode();
				
				if (enemyTroop.states.contains(States.DEAD)) {
					continue;
				}

		
				if (Vector3.getDistanceBetween(troop.position, enemyTroop.position) < 0.12) {
					
					Vector3 accumulator = game.gamePool.vector3s.fetchMemory();
					Vector3 accumulator2 = game.gamePool.vector3s.fetchMemory();
					Vector3 accumulator3 = game.gamePool.vector3s.fetchMemory();
					
					Vector3.subtract(accumulator, enemyTroop.position, troop.position);
					Vector3.getNormalized2d(accumulator, accumulator);
					accumulator2.x = troop.orientation.x;
					accumulator2.y = troop.orientation.y;
					accumulator2.z = 1;
					Vector3.getNormalized2d(accumulator2, accumulator2);
					
					// Make amount [-1, 1] to [0, 1]
					double amount = Vector3.dotProduct2d(accumulator, accumulator2);
					amount = amount * 0.5f;
					amount = amount + 0.5;
					
					Vector3.subtract(accumulator, troop.position, enemyTroop.position);
					Vector3.getNormalized2d(accumulator, accumulator);
					accumulator2.x = enemyTroop.orientation.x;
					accumulator2.y = enemyTroop.orientation.y;
					accumulator2.z = 1;
					Vector3.getNormalized2d(accumulator2, accumulator2);					
					
					double enemyAmount = Vector3.dotProduct2d(accumulator, accumulator2);
					enemyAmount = enemyAmount * 0.5f;
					enemyAmount = enemyAmount + 0.5;
					
					game.gamePool.vector3s.recycleMemory(accumulator3);
					game.gamePool.vector3s.recycleMemory(accumulator2);
					game.gamePool.vector3s.recycleMemory(accumulator);
					
					
					if (enemyAmount > amount) {
						if (troop.states.contains(States.ALIVE)) {
							troop.states.remove(States.ALIVE);
							troop.states.put(States.DEAD, true);
							troop.orientation.x = 0;
							troop.orientation.y = 1;
						}
						
						if (troop.isAlive[0]) {
								troop.isAlive[0] = false;
								troop.orientation.x = 0;
								troop.orientation.y = 1;
						}	
					}
					else {
						// random test fails
						if (enemyTroop.states.contains(States.ALIVE)) {
							enemyTroop.states.remove(States.ALIVE);
							enemyTroop.states.put(States.DEAD, true);
							enemyTroop.orientation.x = 0;
							enemyTroop.orientation.y = 1;
						}
						
						if (enemyTroop.isAlive[0]) {
							enemyTroop.isAlive[0] = false;
							enemyTroop.orientation.x = 0;
							enemyTroop.orientation.y = 1;
						}	
					}
					
				} 
			}
		}
	}
	
	public static class BattleNode {
		public Vector3 position;
		public Vector3 velocity;
		public Orientation orientation;
		
		
		public Boolean[] isAlive;
		
		public Hashtable<String, Boolean> states;
		
		
		public BattleNode(Vector3 position, Vector3 velocity, Orientation orientation, Boolean[] isAlive, Hashtable<String, Boolean> states) {
			this.position = position;
			this.velocity = velocity;
			this.orientation = orientation;
			this.isAlive = isAlive;
			this.states = states;
		}
	}
	
	public interface BattleNodeBindable {
		public BattleNode getBattleNode();
	}
}
