package game.androidgame2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import tenth.system.BattleSystem.BattleNode;
import tenth.system.BattleSystem.BattleNodeBindable;
import tenth.system.FieldUnitNode;
import tenth.system.FieldUnitNode.FieldUnitNodeBindable;
import tenth.system.ForceIntegratorSystem.ForceIntegratorNode;
import tenth.system.ForceIntegratorSystem.ForceIntegratorNodeBindable;
import tenth.system.FormationSystem.FormationNode;
import tenth.system.FormationSystem.FormationNodeBindable;
import tenth.system.LivingComponent;
import tenth.system.OrientationSystem.OrientationNode;
import tenth.system.OrientationSystem.OrientationNodeBindable;
import tenth.system.SelectionSystem.SelectionNode;
import tenth.system.SelectionSystem.SelectionNodeBindable;
import tenth.system.SeparationSystem;
import tenth.system.SeparationSystem.SeparationNode;
import tenth.system.SeparationSystem.SeparationNodeBindable;
import tenth.system.SquadPosition;
import tenth.system.States;
import tenth.system.SystemNode;
import tenth.system.TroopDrawSystem.UnitDrawNode;
import tenth.system.TroopDrawSystem.UnitDrawNodeBindable;
import utils.Orientation;

public class Troop extends SystemNode implements
								FieldUnitNodeBindable, SeparationNodeBindable, FormationNodeBindable,
								ForceIntegratorNodeBindable, 
								BattleNodeBindable, 
								UnitDrawNodeBindable,
								OrientationNodeBindable,
								SelectionNodeBindable {
	
	// No need, deprecated
	//public AnimationPack animationPack;

	public static final HashSet<NodeType> NODE_TYPES = new HashSet<NodeType>() {{
		this.add(SystemNode.NodeType.FORMATION);
		this.add(SystemNode.NodeType.SEPARATION);
		this.add(SystemNode.NodeType.FIELD_MOVEMENT);
		this.add(SystemNode.NodeType.FORCE_INTEGRATOR);
		this.add(SystemNode.NodeType.BATTLE);
		this.add(SystemNode.NodeType.TROOP_DRAW);
		this.add(SystemNode.NodeType.ORIENTATION);
		this.add(SystemNode.NodeType.SELECTION);
	}};
    public HashSet<NodeType> getNodeTypes() {  return NODE_TYPES;   }

	public static float TROOP_RADIUS = 0.065f;
	public static float TROOP_INNER_RADIUS = 0.06f;
	
	public static float VELOCITY_DRAG = 0.7f;
	public static float MAX_MOVE_SPEED = 0.007f;
	public static float MAX_FORCE = 0.0005f;
	public static float TURNING_RATE = 2.0f;
	
	public static float ART_ANIMATION_IDLING_DURATION = 1000;
	public static float ART_ANIMATION_DEATH_DURATION = 1160;
	
	public Vector3 position;
	public Vector3 velocity;
	
	// TODO
	public Vector3 formationForce;
	
	/** For separation system */
	public Vector3 separationForce;
	
	/** For user field movement system */
	public Vector3 fieldForce;
	
	// TODO
	public Orientation alignmentOrientation;
	
	public Orientation orientation;
	
	public float[] turningRate = new float[] { 0.01f };
	
	public OrientationNode orientationNode;
	
	/**
	 * Idea: If we randomly choose whether or not to set old position, we can get old position after x steps!
	 * Faking memory with randomness
	 */
	public Vector3 oldPosition;

	
	public float[] radius = new float[] {0.07f};
	
	public SquadPosition squadPositionToFollow = null;
	
	public float leadershipPropensity[] = new float[] { (float)Math.random() };
	
	/**
	 * The state reflects the user command the troop is following
	 *
	 */
	public enum State {
		IDLE,
		MOVING,
		ATTACKING,
		SPECIAL_ATTACKING,
		DEAD
	};
	
	public enum Intention {
		FOLLOWING_FORMATION,
		MOVING_TOWARDS_WAYPOINT
	};
	
	public enum Type {
		SMALL_TROOP,
		BIG_TROOP,
		CAPITAL_SHIP
	};
	
	public State state = State.IDLE;
	
	public Type type = Type.SMALL_TROOP;
	
	public float[] crowdDensity;
	
	private SeparationNode separationSystemNode;
	private FieldUnitNode fieldUnitNode;
	public float[] fieldForceSensitivity;
	
	private RewriteOnlyArray<SquadPosition> squadPositions = new RewriteOnlyArray<SquadPosition>(SquadPosition.class, 32);
	private FormationNode formationNode;
	//public FormationNode.Type[] formationType;
	
	private ForceIntegratorNode forceIntegratorNode;

	private BattleNode battleNode;
	
	public Boolean[] isAlive;
	public LivingComponent livingComponent = new LivingComponent();
	
	public boolean[] isSelected = new boolean[] { false };
	public SelectionNode selectionNode;
	
	/**
	 * TODO: Make this animation trigger? 
	 */
	public Hashtable<String, Boolean> states;
	public ArrayList<String> events;
	public Hashtable<String, TimedProgress> stateAnimations;

	
	public UnitDrawNode unitDrawNode;
	
	// TODO: Make this team
	public int player[] = new int[] {0};
	
	public Troop() {
		this.labels.add(Label.Troop);

		position = new Vector3();
		velocity = new Vector3();
		orientation = new Orientation();

		states = new Hashtable<String, Boolean>(32);
		states.put(States.ALIVE, true);
		
		events = new ArrayList<String>(32);
		
		stateAnimations = new Hashtable<String, TimedProgress>(32);
		
		fieldForce = new Vector3();
		fieldForceSensitivity = new float[] {1};
		
		formationForce = new Vector3();
		separationForce = new Vector3();
		
		//formationType = new FormationNode.Type[] { FormationNode.Type.SOLDIER };
		
		isAlive = new Boolean[] { true };
		
		separationSystemNode = new SeparationSystem.SeparationNode(position, velocity, orientation, separationForce, leadershipPropensity, isAlive, states, this);
		
		fieldUnitNode = new FieldUnitNode(position, velocity, orientation, fieldForce, fieldForceSensitivity, leadershipPropensity, this, isAlive, states, events);
		
		forceIntegratorNode = new ForceIntegratorNode(position, velocity, orientation, separationForce, formationForce, fieldForce, this, isAlive, states);
		
		battleNode = new BattleNode(position, velocity, orientation, isAlive, states);
		
		formationNode = new FormationNode(position, velocity, orientation, formationForce, isAlive, squadPositionToFollow, squadPositions);
		
		orientationNode = new OrientationNode(velocity, orientation, turningRate);
		
		unitDrawNode = new UnitDrawNode(position, velocity, orientation, radius, isAlive, isSelected, player, states, stateAnimations);
		
		selectionNode = new SelectionNode(position, isSelected);
	}

	public void headTowardsPosition(Vector3 position, long elapsedTime) {
		
	}
	
	public void update(long elapsedTime) {
		//headTowardsPosition(this.squadPosition.pos, elapsedTime);
	}
	
	public float getRadius() {
		return TROOP_RADIUS;
	}

	public FieldUnitNode getFieldUnitNode() {
		return fieldUnitNode;
	}
	
	public SeparationNode getSeparationSystemNode() {
		return separationSystemNode;
	}

	public ForceIntegratorNode getForceIntegratorNode() {
		return forceIntegratorNode;
	}

	@Override
	public FormationNode getFormationNode() {
		return formationNode;
	}
	
	public BattleNode getBattleNode() {
		return battleNode;
	}
	
	public UnitDrawNode getUnitDrawNode() {
		return unitDrawNode;
	}

	@Override
	public OrientationNode getOrientationNode() {
		return orientationNode;
	}

	@Override
	public SelectionNode getSelectionNode() {
		return selectionNode;
	}
}
