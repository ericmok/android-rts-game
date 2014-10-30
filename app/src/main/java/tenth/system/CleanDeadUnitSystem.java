package tenth.system;

import java.util.ArrayList;

import game.androidgame2.Game;
import game.androidgame2.Troop;
import tenth.system.BattleSystem.BattleNode;
import tenth.system.BattleSystem.BattleNodeBindable;

public class CleanDeadUnitSystem {
	private Game game;
	
	public CleanDeadUnitSystem(Game game) {
		this.game = game;
	}
	
	/**
	 * Accepts battlenodes
	 * TODO: Recycle stateAnimations / TimedProgresses
	 */
	public void update(ArrayList<SystemNode> units, long elapsedTime) {
		for (int i = 0; i < units.size(); i++) {
			//BattleNode battleNode = ((BattleNodeBindable)units.get(i)).getBattleNode();
			
//			try {
////				Log.i("CleanDeadUnitSystem", "CleanDeadUnitSystem: State Dead?: " + 	((Troop)units.get(i)).states.contains(States.DEAD));
////				Log.i("CleanDeadUnitSystem", "CleanDeadUnitSystem: StateAnim: " + 	((Troop)units.get(i)).stateAnimations.size());
//				Log.i("CleanDeadUnitSystem", "CleanDeadUnitSystem: StateAnim Alive: " + 	((Troop)units.get(i)).stateAnimations.get(States.ALIVE).getProgress());	
//				Log.i("CleanDeadUnitSystem", "CleanDeadUnitSystem: StateAnim Dead: " + 	((Troop)units.get(i)).stateAnimations.get(States.DEAD).getProgress());	
//			}
//			catch (Exception e) {
//				e.printStackTrace();
//			}
			
//			if (battleNode.states.contains(States.DEAD) && 
//					((Troop)units.get(i)).stateAnimations.contains(States.DEAD) && 
//					((Troop)units.get(i)).stateAnimations.get(States.DEAD).getProgress() > 98) {
//				game.gamePool.troops.recycleMemory( (Troop) game.stage.userUnits.remove(i) );
//				i = i - 1;
//			}
			
//			if (battleNode.states.contains(States.DEAD)) {
//				Log.i("CleanDeadUnitSystem", "CleanDeadUnitSystem: StateAnim Dead: " + 	(int)((Troop)units.get(i)).stateAnimations.get(States.DEAD).getProgress());
//				game.gamePool.troops.recycleMemory( (Troop) game.stage.userUnits.remove(i) );
//				i = i - 1;
//			}
			
			
			// TODO: Fix dependency with Troop
//			
//			Troop troop = (Troop)units.get(i);
//			if (troop.stateAnimations.get(States.DEAD) != null && troop.stateAnimations.get(States.DEAD).getProgress() > 98) {
//				
//				units.remove(i);
//				i = i - 1;
//				game.gamePool.troops.recycleMemory( troop );					
//
//			}
			SystemNode node = units.get(i);
			BattleNode bn = ((BattleNodeBindable)units.get(i)).getBattleNode();
			if (bn.isAlive[0] == false) {
				
				if (node.getLabels().contains(SystemNode.Label.Troop)) {
					// TODO: Time sensitive memory pool..
					game.gamePool.recycle(units.get(i), Troop.class);
				}
				
				units.remove(i);
				i = i - 1;
			}
		}
	}
	
	
}
