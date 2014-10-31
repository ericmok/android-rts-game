package tenth.system;

import java.util.ArrayList;

import game.androidgame2.Game;
import game.androidgame2.Troop;

public class StateProgressUpdateSystem {
	
	private Game game;
	
	public StateProgressUpdateSystem(Game game) {
		this.game = game;
	}
	
	public ArrayList<Troop> update(ArrayList<Troop> troops, long elapsedTime) {
		
		for (int i = 0; i < troops.size(); i++) {
			Troop troop = troops.get(i);
			
			// TODO: Testing
			//troop.animation.update(elapsedTime);
			
//			RewritableArray<TimedProgress> processes = troop.getProcesses();
//			processes.resetIterator();
//			while (processes.canIterateNext()) {
//				TimedProgress progress = processes.getNextIteratorItem();
//				progress.update(elapsedTime);
//			}
		}
		
		for (int i = 0; i < troops.size(); i++) {
			Troop troop = troops.get(i);
			
//			if (troop.state == Troop.State.DEAD && (troop.stateProgress >= 100 || troop.animation.progress >= 99.9)) {
//				troops.remove(i);
//				i = i - 1;
//			}
		}
		
		return troops;
	}
}
