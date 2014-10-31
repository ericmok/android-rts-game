package tenth.system;

import java.util.ArrayList;

import game.androidgame2.Game;
import game.androidgame2.Troop;
import tenth.system.FormationSystem.FormationNode;
import tenth.system.FormationSystem.FormationNodeBindable;

public class GenerateTroopsInSquadPositionsSystem {
	private Game game;

	
	public GenerateTroopsInSquadPositionsSystem(Game game) {
		this.game = game;
	}
	
	public void update(int player, ArrayList<SystemNode> nodes) {

		for (int s = 0; s < nodes.size(); s++) {
			FormationNode fn = ((FormationNodeBindable)nodes.get(s)).getFormationNode();
			
			for (int i = 0; i < fn.squadPositions.size(); i++) {
				SquadPosition sp = fn.squadPositions.get(i);
				Troop troop = game.gamePool.troops.fetchMemory();
				//troop.team[0] = Stage.TEAM_0;
				troop.position.x = sp.position.x;
				troop.position.y = sp.position.y;
				troop.radius[0] = Constants.UNIT_RADIUS * 0.5f;
				troop.velocity.zero();
				troop.type = Troop.Type.SMALL_TROOP;
				troop.fieldForceSensitivity[0] = 0.5f;
				troop.player[0] = player;
				//troop.formationType[0] = FormationNode.Type.SOLDIER;
				
				nodes.add(troop);
			}	
		}
	}
}
