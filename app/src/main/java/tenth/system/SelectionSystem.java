package tenth.system;

import java.util.ArrayList;
import java.util.Hashtable;

import game.androidgame2.Game;
import game.androidgame2.GameLoop;
import game.androidgame2.Vector3;
import tenth.system.SystemNode.NodeType;

public class SelectionSystem {
	
	public static final String NODE = "SELECTION";
	
	private Game game;
	
	public enum SelectionState {
		SELECT_UNIT,
		SELECT_COMMAND
	};
	
	public SelectionState selectionState = SelectionState.SELECT_UNIT;
	
	public SelectionSystem(Game game) {
		this.game = game;
	}
	
	/**
	 * Given nodes, a mouse coord, return selected <br/>
	 * <strong>Preconditions:</strong> A cleared/pre processed selected list
	 * @param nodes
	 * @param selected
	 * @param elapsedTime
	 */
	public void update(ArrayList<SystemNode> nodes, ArrayList<SystemNode> selected, float touchX, float touchY, Hashtable<Integer, Boolean> gestures, long elapsedTime) {
		if (gestures.containsKey(GameLoop.GESTURE_ON_SINGLE_TAP_UP) && 
				this.selectionState == SelectionState.SELECT_UNIT) {

			selected.clear();
			for (int i = 0; i < nodes.size(); i++) {
				if (!nodes.get(i).getNodeTypes().contains(NodeType.SELECTION)) {
					 continue;
				}
				
				SelectionNode selectionNode = ((SelectionNodeBindable)nodes.get(i)).getSelectionNode();
	
				if (Math.pow(selectionNode.position.x - touchX, 2) + 
					Math.pow(selectionNode.position.y - touchY, 2) 
					< 2 * Constants.UNIT_RADIUS) {
					selectionNode.isSelected[0] = true;
					selected.add(nodes.get(i));
				}
				else {
					selectionNode.isSelected[0] = false;
				}
			}
		}
	}
	
	
	public static class SelectionNode {
		public Vector3 position;
		public boolean[] isSelected;
		public boolean enabled = true;
		
		public SelectionNode(Vector3 position, boolean[] isSelected) {
			this.position = position;
			this.isSelected = isSelected;
		}
	}
	
	public interface SelectionNodeBindable {
		public SelectionNode getSelectionNode();
	}
}
