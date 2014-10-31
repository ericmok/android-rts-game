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
import tenth.system.OrientationSystem.OrientationNode;
import tenth.system.OrientationSystem.OrientationNodeBindable;
import tenth.system.SeparationSystem.SeparationNode;
import tenth.system.SeparationSystem.SeparationNodeBindable;
import tenth.system.ShipDrawSystem.ShipDrawNode;
import tenth.system.ShipDrawSystem.ShipDrawNodeBindable;
import tenth.system.SquadPosition;
import tenth.system.SystemNode;
import utils.Orientation;

public class SmallShip extends SystemNode implements
									FieldUnitNodeBindable, FormationNodeBindable, SeparationNodeBindable, 
									ForceIntegratorNodeBindable, 
									BattleNodeBindable, 
									ShipDrawNodeBindable,
									OrientationNodeBindable {

	public static final HashSet<NodeType> NODE_TYPES = new HashSet<NodeType>() {{
		this.add(SystemNode.NodeType.FIELD_MOVEMENT);
		this.add(SystemNode.NodeType.FORMATION);
		this.add(SystemNode.NodeType.SEPARATION);
		this.add(SystemNode.NodeType.BATTLE);
		this.add(SystemNode.NodeType.FORCE_INTEGRATOR);
		this.add(SystemNode.NodeType.SHIP_DRAW);
		this.add(SystemNode.NodeType.ORIENTATION);
	}};

    public HashSet<NodeType> getNodeTypes() {  return NODE_TYPES;   }


    public final int[] team = new int[] { 0 };
	
	public final Vector3 position = new Vector3();
	public final Vector3 velocity = new Vector3();
	
	public final Orientation orientation = new Orientation();
	public final float[] turningRate = new float[] { 0.006f };
	
	public OrientationNode orientationNode;
	
	public final float[] radius = new float[] { 0.12f };
	public final Boolean[] isAlive = new Boolean[] {true};
	
	public final Hashtable<String, Boolean> states = new Hashtable<String, Boolean>(16);
	public final ArrayList<String> events = new ArrayList<String>(16);
	
	public final Vector3 fieldForce = new Vector3();
	public final Vector3 separationForce = new Vector3();
	public final Vector3 formationForce = new Vector3();
	
	public final float[] fieldForceSensitivity = new float[] { 0.9f };
	
	public final float[] leadershipPropensity = new float[] { 1 };
	
	private FieldUnitNode fieldUnitNode;
	private FormationNode formationNode;
	private SeparationNode separationNode;
	private BattleNode battleNode;
	
	private ForceIntegratorNode forceIntegratorNode;

	private ShipDrawNode shipDrawNode;
	
	private SquadPosition squadPositionToFollow = new SquadPosition();
	private RewritableArray<SquadPosition> squadPositions = new RewritableArray<SquadPosition>(SquadPosition.class, 64);
	
	public Hashtable<String, TimedProgress> stateAnimations;
	
	public SmallShip() {
        this.labels.add(Label.SmallShip);

		fieldUnitNode = new FieldUnitNode(position, velocity,orientation, fieldForce, fieldForceSensitivity, leadershipPropensity, this, isAlive, states, events);
		formationNode = new FormationNode(position, velocity, orientation, formationForce, isAlive, squadPositionToFollow, squadPositions);
		separationNode = new SeparationNode(position, velocity, orientation, separationForce, leadershipPropensity, isAlive, states, this);
		
		forceIntegratorNode = new ForceIntegratorNode(position, velocity, orientation, separationForce, formationForce, fieldForce, this, isAlive, states);
		
		battleNode = new BattleNode(position, velocity, orientation, isAlive, states);
		
		orientationNode = new OrientationNode(velocity, orientation, turningRate);
		
		shipDrawNode = new ShipDrawNode(position, velocity, orientation, radius, isAlive, team, stateAnimations);
	}

	@Override
	public BattleNode getBattleNode() {
		return battleNode;
	}

	@Override
	public SeparationNode getSeparationSystemNode() {
		return separationNode;
	}

	@Override
	public FormationNode getFormationNode() {
		return formationNode;
	}

	@Override
	public FieldUnitNode getFieldUnitNode() {
		return fieldUnitNode;
	}

	public ForceIntegratorNode getForceIntegratorNode() {
		return forceIntegratorNode;
	}

	@Override
	public ShipDrawNode getShipDrawNode() {
		return shipDrawNode;
	}

	@Override
	public OrientationNode getOrientationNode() {
		return orientationNode;
	}
}
