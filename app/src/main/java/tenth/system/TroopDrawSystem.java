package tenth.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import android.graphics.Color;

import game.androidgame2.DrawList2DItem;
import game.androidgame2.Game;
import game.androidgame2.Orientation;
import game.androidgame2.RewritableArray;
import game.androidgame2.TemporaryDrawList2DItem;
import game.androidgame2.TimedProgress;
import game.androidgame2.Troop;
import game.androidgame2.Vector3;
import tenth.system.SystemNode.NodeType;

public class TroopDrawSystem {
	private Game game;
	
	public HashMap<Troop.State, String> stateMap;
	
	public TroopDrawSystem(Game game) {
		this.game = game;
		
		stateMap = new HashMap<Troop.State, String>();
		stateMap.put(Troop.State.IDLE, DrawList2DItem.ANIMATION_TROOPS_IDLING);
		stateMap.put(Troop.State.DEAD, DrawList2DItem.ANIMATION_TROOPS_DYING);
	}
	
	/**
	 * Takes a list of troops and outputs stuff to render
	 * 
	 * @param output
	 * @param troops
	 * @param elapsedTime
	 */
	public void update(RewritableArray<DrawList2DItem> output, List<TemporaryDrawList2DItem> tempSprites, ArrayList<SystemNode> troops, long elapsedTime) {
		
		for (int i = 0; i < troops.size(); i++) {
			
			if (!troops.get(i).getNodes().contains(NodeType.TROOP_DRAW)) {
				continue;
			}
			
			UnitDrawNode udn = ((UnitDrawNodeBindable)troops.get(i)).getUnitDrawNode();
			
			DrawList2DItem sprite; 
			
			if (udn.states.containsKey(States.ALIVE)|| udn.isAlive[0]) {
				sprite = output.takeNextWritable();
				sprite.position.copy(udn.position);
				sprite.width = udn.radius[0];
				sprite.height = udn.radius[0];
				sprite.angle = (float) udn.orientation.getDegrees();
				sprite.color = Color.WHITE;
				
				
				if (udn.states.containsKey(States.TEAM_1) || udn.team[0] != 0) {
					sprite.animationName = DrawList2DItem.ANIMATION_ENEMY_TROOPS_IDLING;
					//sprite.color = Color.RED;
				}
				else {
					sprite.animationName = DrawList2DItem.ANIMATION_TROOPS_IDLING;
					
					TimedProgress tp = udn.stateAnimations.get(States.TEAM_0);
					if (tp != null) {
						sprite.animationProgress = (int)tp.progress;
						tp.update(elapsedTime);
					}
					else {
						TimedProgress toInsert = game.gamePool.timedProgresses.fetchMemory();
						toInsert.duration = Troop.ART_ANIMATION_IDLING_DURATION;
						toInsert.recurrent = true;
						toInsert.resetProgress();
						udn.stateAnimations.put(States.TEAM_0, toInsert);
						sprite.animationProgress = 1;
					}
					
					if (udn.isSelected[0]) {
						sprite = output.takeNextWritable();
						sprite.position.copy(udn.position);
						sprite.width = udn.radius[0] * 3f;
						sprite.height = udn.radius[0] * 3f;
						sprite.angle = (float)udn.orientation.getDegrees();
						sprite.color = Color.WHITE;
						sprite.animationName = DrawList2DItem.ANIMATION_TROOPS_SELECTED;
						
						
						if (udn.stateAnimations.containsKey("Tapped")) {
							sprite.animationProgress = (int) udn.stateAnimations.get("Tapped").progress;
							udn.stateAnimations.get("Tapped").update(elapsedTime);
						}
						else {
							TimedProgress toInsert = game.gamePool.timedProgresses.fetchMemory();
							toInsert.duration = 900;
							toInsert.recurrent = true;
							toInsert.resetProgress();
							udn.stateAnimations.put("Tapped", toInsert);
							sprite.animationProgress = 1;	
						}
						
					}
				}
					
			}
			
			if (udn.states.containsKey(States.DEAD) || !udn.isAlive[0]) {
//				sprite = output.takeNextWritable();
//				sprite.position.copy(udn.position);
//				sprite.width = udn.radius[0];
//				sprite.height = udn.radius[0];
//				//sprite.angle = (float) udn.orientation.getDegrees();
//				sprite.angle = 90;
//				sprite.animationName = DrawList2DItem.ANIMATION_TROOPS_DYING;
//				
//				TimedProgress tp = udn.stateAnimations.get(States.DEAD);
//				if (tp != null) {
//					sprite.animationProgress = (int)tp.progress;
//					tp.update(elapsedTime);
//				}
//				else {
//					TimedProgress toInsert = game.gamePool.timedProgresses.fetchMemory();
//					toInsert.duration = Troop.ART_ANIMATION_DEATH_DURATION;
//					toInsert.recurrent = false;
//					toInsert.resetProgress();
//					udn.stateAnimations.put(States.DEAD, toInsert);
//					sprite.animationProgress = 1;
//				}
				TemporaryDrawList2DItem tempSprite = game.gamePool.temporaryDrawItems.fetchMemory();
				tempSprite.position.copy(udn.position);
				tempSprite.width = udn.radius[0];
				tempSprite.height = udn.radius[0];
				tempSprite.angle = 90.0f;
				tempSprite.animationName = DrawList2DItem.ANIMATION_TROOPS_DYING;
				tempSprite.progress.duration = Troop.ART_ANIMATION_DEATH_DURATION;
				tempSprite.progress.recurrent = false;
				tempSprite.progress.resetProgress();
				tempSprites.add(tempSprite);
			}
			
		}
		
//	
//		for (int i = 0; i < troops.size(); i++) {
//			Troop troop = (Troop)troops.get(i);
//				
//				DrawList2DItem sprite = output.takeNextWritable();
//				
//				sprite.position.x = troop.position.x;
//				sprite.position.y = troop.position.y;
//				sprite.width = troop.radius;
//				sprite.height = troop.radius;
//				sprite.angle = (float) troop.orientation.getDegrees();
//				
//				if (troop.type == Troop.Type.BIG_TROOP) {
//					sprite.width = Troop.TROOP_RADIUS * 1.01f;
//					sprite.height = Troop.TROOP_RADIUS * 1.01f;
//				}
//				
//				if (troop.type == Troop.Type.CAPITAL_SHIP) {
//					sprite.animationName = DrawList2DItem.ANIMATION_CAPITAL_SHIPS_IDLING;
//					
//					if (troop.state == Troop.State.DEAD) {
//						sprite.animationName = DrawList2DItem.ANIMATION_CAPITAL_SHIPS_DYING;	
//						
//					}
//				}
//				else {
//					if (troop.state == Troop.State.DEAD) {
//						sprite.animationName = DrawList2DItem.ANIMATION_TROOPS_DYING;	
//					}
//				}
//				
//				if (troop.team == Team.TEAM_1) {
//					sprite.animationName = DrawList2DItem.ANIMATION_ENEMY_TROOPS_IDLING;
//					
//					if (troop.state == Troop.State.DEAD) {
//						sprite.animationName = DrawList2DItem.ANIMATION_TROOPS_DYING;	
//					}
//				}
//				
//				
//		}	
				//sprite.animationProgress = (int) troop.animation.progress;
				
//				troop.statuses.resetIterator();
				
//				while (troop.statuses.canIterateNext()) {
//					TimedProgress timedProgress = troop.statuses.getNextIteratorItem();
//					sprite = output.getNextWritable();
//					
//					sprite.position.x = troop.kinematicPosition.position.x;
//					sprite.position.y = troop.kinematicPosition.position.y;
//					sprite.width = troop.radius;
//					sprite.height = troop.radius;
//					sprite.angle = (float) troop.kinematicRotation.orientation.getDegrees();
//					sprite.animationName = stateMap.get(timedProgress.name);
//					sprite.animationProgress = (int) timedProgress.progress;
//
//				}
		
	}
	
	public static class UnitDrawNode {
		
		public Vector3 position;
		public Vector3 velocity;
		public Orientation orientation;
		public float[] radius;
		
		public Boolean[] isAlive;
		
		public boolean[] isSelected;
		
		// TODO: When there is an event, the system can decide to add an animation to the list of running animations
		public ArrayList<String> events;
		
		public Hashtable<String, Boolean> states;
		
		public Hashtable<String, TimedProgress> stateAnimations;
		
		public int[] team;
		
		public UnitDrawNode(Vector3 position, Vector3 velocity, Orientation orientation, float[] radius, Boolean[] isAlive, boolean[] isSelected, int[] team, Hashtable<String, Boolean> states, Hashtable<String, TimedProgress> stateAnimations) {
			this.position = position;
			this.velocity = velocity;
			this.orientation = orientation;
			this.radius = radius;
			this.isAlive = isAlive;
			
			this.isSelected = isSelected;
			
			this.team = team;
			this.states = states;
			this.stateAnimations = stateAnimations;
			
		}
	}
	
	public static interface UnitDrawNodeBindable {
		public UnitDrawNode getUnitDrawNode();
	}
}
