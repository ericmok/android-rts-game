package tenth.system;

import java.util.ArrayList;
import java.util.Hashtable;

import android.graphics.Color;

import game.androidgame2.DrawList2DItem;
import game.androidgame2.Game;
import game.androidgame2.Orientation;
import game.androidgame2.RewritableArray;
import game.androidgame2.TimedProgress;
import game.androidgame2.Vector3;
import tenth.system.SystemNode.NodeType;

public class ShipDrawSystem {
	private Game game;
	
	public ShipDrawSystem(Game game) {
		this.game = game;
	}
	
	/**
	 * Takes ShipDrawNodes
	 * @param output
	 * @param nodes ShipDrawNodes
	 * @param elapsedTime
	 */
	public void update(RewritableArray<DrawList2DItem> output, ArrayList<SystemNode> nodes, long elapsedTime) {
		for (int i = 0; i < nodes.size(); i++) {
			if (!nodes.get(i).getNodes().contains(NodeType.SHIP_DRAW)) {
				return; 
			}
			
			ShipDrawNode sdn = ((ShipDrawNodeBindable)nodes.get(i)).getShipDrawNode();
			
			DrawList2DItem sprite;
			
			sprite = output.takeNextWritable();
			sprite.position.copy(sdn.position);
			sprite.width = sdn.radius[0];
			sprite.height = sdn.radius[0];
			sprite.animationName = DrawList2DItem.ANIMATION_CAPITAL_SHIPS_IDLING;
			sprite.angle = (float)sdn.orientation.getDegrees();
			sprite.animationProgress = 1;
			sprite.color = Color.WHITE;
		}
	}
	
	
	public static class ShipDrawNode {
		public final Vector3 position;
		public final Vector3 velocity;
		public final Orientation orientation;
		
		public final float[] radius;
		
		public final Boolean[] isAlive;
		
		public final int[] team;
		
		public final Hashtable<String, TimedProgress> stateAnimations;
		
		public ShipDrawNode(Vector3 position, Vector3 velocity, Orientation orientation, float[] radius, Boolean[] isAlive, int[] team, Hashtable<String, TimedProgress> stateAnimations) {
			this.position = position;
			this.velocity = velocity;
			this.orientation = orientation;
			
			this.radius = radius;
			this.isAlive = isAlive;
			
			this.team = team;
			
			this.stateAnimations = stateAnimations;
		}
	}
	
	public interface ShipDrawNodeBindable {
		public ShipDrawNode getShipDrawNode();
	}
}
