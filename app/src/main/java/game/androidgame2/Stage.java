package game.androidgame2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import tenth.system.SystemNode;

/**
 * Holds all the data for the game that is to be used by logic 
 */
public class Stage {

	public static final String TEAM_0 = "TEAM_0";
	public static final String TEAM_1 = "TEAM_1";
	public static final String TEAM_2 = "TEAM_2";

    public HashMap<SystemNode.Label, ArrayList<SystemNode>> labeledLists = new HashMap<SystemNode.Label, ArrayList<SystemNode>>(16);

//	public ArrayList<SystemNode> userUnits;
//	public ArrayList<SystemNode> nonUserUnits;
//	
//	public ArrayList<TriggerField> userTriggerFields;
	
	// ArrayList instead?
	public ArrayList<Player> players;
	
	public Stage() {
//		userUnits = new ArrayList<SystemNode>(512);
//		
//		userTriggerFields = new ArrayList<TriggerField>(256);
//		
//		nonUserUnits = new ArrayList<SystemNode>(512);

		players = new ArrayList<Player>();
		players.add(new Player(TEAM_0));
		players.add(new Player(TEAM_1));


        for (int i = 0; i <  SystemNode.Label.values().length; i++) {
            SystemNode.Label label = SystemNode.Label.values()[i];
            labeledLists.put(label, new ArrayList<SystemNode>(300));
        }
	}

    public void addNode(SystemNode node) {
        for (int i = 0; i <  SystemNode.Label.values().length; i++) {
            SystemNode.Label label = SystemNode.Label.values()[i];
            if (node.getLabels().contains(label)) {
                labeledLists.get(label).add(node);
            }
        }
    }

    public void removeNode(SystemNode node) {
        for (int i = 0; i <  SystemNode.Label.values().length; i++) {
            SystemNode.Label label = SystemNode.Label.values()[i];
            if (node.getLabels().contains(label)) {
                labeledLists.get(label).remove(node);
            }
        }
    }
}
