package game.androidgame2;

import java.util.ArrayList;

public class Squad {
	
	public class SquadPosition {
		public Vector3 pos = new Vector3();
		public Unit occupyingUnit = null;
	}
	
	public static final int MAX_NUMBER_UNITS = 16;
	
	public Unit leader;
	public Unit[] units;
	public ArrayList<SquadPosition> positions;
	
	
	public Squad() {
		leader = new Unit();
		units = new Unit[MAX_NUMBER_UNITS];
		positions = new ArrayList<SquadPosition>();
	}
	
	public void setStandardFormation() {
		for (double x = leader.kinematicPosition.position.x - leader.width * 3; 
					x < x + leader.width * 3; 
					x += leader.width * 0.5) {
			for (double y = leader.kinematicPosition.position.y - leader.height * 3; 
					y < y + leader.height * 3; 
					y += leader.height * 0.5) {
				SquadPosition sp = new SquadPosition();
				sp.pos.x = x;
				sp.pos.y = y;
				sp.pos.z = 0;
				positions.add(sp);
			}
		}
	}
	
	public void update(long elapsedTime) {
		// TODO: squad leader position update
		// Thing is, we want troops to get a new target upon squad leader update
	}

}
