package tenth.system;

import java.util.ArrayList;
import java.util.Hashtable;

import game.androidgame2.Orientation;
import game.androidgame2.Vector3;

/**
 * Stores pointers to the actual data
 */
public class FieldUnitNode {
	
	/**
	 * Pointer to position
	 */
	final public Vector3 position;
	
	/**
	 * Pointer to velocity
	 */
	final public Vector3 velocity;
	
	/**
	 * Pointer to orientation
	 */
	final public Orientation orientation;
	
	/**
	 * Pointer to the Object that is represented by this node
	 */
	final public FieldUnitNodeBindable sourceObject;
	
	/**
	 * Set this to true if the node is to skip computation
	 */
	final public boolean ignore = false;

	final public float[] leadershipPropensity;
	
	final float[] fieldForceSensitivity;
	
	final public Vector3 fieldForce;
	
	/**
	 * TODO:
	 * Set between 0 and 1 to scale the field force.
	 */
	//final public float[] fieldForceSensitivity;
	
	final public Boolean[] isAlive;
	
	final public Hashtable<String, Boolean> states;

	final public ArrayList<String> events;
	
	/**
	 * Sets everything to null. 
	 */
	public FieldUnitNode(Vector3 position, 
							Vector3 velocity, 
							Orientation orientation, 
							Vector3 fieldForce, 
							float[] fieldForceSensitivity,
							float[] leadershipPropensity, 
							FieldUnitNodeBindable sourceObject, 
							Boolean[] isAlive,
							Hashtable<String, Boolean> states,
							ArrayList<String> events) {
		this.position = position;
		this.velocity = velocity;
		this.orientation = orientation;
		
		this.fieldForce = fieldForce;
		this.fieldForceSensitivity = fieldForceSensitivity;
		
		this.leadershipPropensity = leadershipPropensity;
		
		this.sourceObject = sourceObject;
		
		this.isAlive = isAlive;
		
		this.states = states;
		this.events = events;
	}
	
	public interface FieldUnitNodeBindable
	{		
		public FieldUnitNode getFieldUnitNode();
	}
}
