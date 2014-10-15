package tenth.system;

import java.util.ArrayList;

import android.util.Log;

import game.androidgame2.Game;
import game.androidgame2.RewritableArray;
import game.androidgame2.TriggerField;
import game.androidgame2.Troop;
import game.androidgame2.Vector3;
import tenth.system.FieldUnitNode.FieldUnitNodeBindable;

/**
 * Given a field, move troops
 */
public class FieldMovementSystem {
	
	private Game game;
	private Vector3 accumulator;
	
	public static final float FIELD_TO_SCREEN_CONSTANT = 0.0001f;
	
	public FieldMovementSystem(Game game) {
		this.game = game;
		
		accumulator = new Vector3();
	}
	
	public double diracDeltaFalloff(double x) {
		double inverseSpikiness = 0.2;
		double scaling = 0.02;
		return (scaling / inverseSpikiness) * Math.exp( - x * x / inverseSpikiness );
	}
	
	public void update(ArrayList<SystemNode> userUnits, ArrayList<TriggerField> triggerFields, long elapsedTime) {
		
		for (int i = 0; i < userUnits.size(); i++) {
			
			FieldUnitNode fieldUnitNode = ((FieldUnitNodeBindable)userUnits.get(i)).getFieldUnitNode();
			
			fieldUnitNode.fieldForce.zero();
			
			if (fieldUnitNode.ignore) {
				continue;
			}
			if (!fieldUnitNode.isAlive[0]) {
				continue;
			}
			if (fieldUnitNode.states.contains(States.DEAD)) {
				continue;
			}
			
			accumulator.zero();

			// Add up all the field forces - any dist critera?
			for (int t = 0; t < triggerFields.size(); t++) {

					Vector3 temp = game.gamePool.vector3s.fetchMemory();
					temp.zero();
					
					Vector3.subtract(temp, triggerFields.get(t).dest, triggerFields.get(t).source);
					
					Vector3.getNormalized2d(temp, temp);

					//double dist = 0.000000001 + Math.pow(Vector3.getDistanceBetween(troop.position, triggerFields.get(t).source),2);
					double dist = Vector3.getSquaredDistanceBetween2d(fieldUnitNode.position, triggerFields.get(t).source);
					dist += 0.000001;
					
					Vector3.scaled(temp, temp, FIELD_TO_SCREEN_CONSTANT / dist);
					
					Vector3.add(accumulator, accumulator, temp);
						
					game.gamePool.vector3s.recycleMemory(temp);
			}
			
			//Vector3.scaled(fieldUnitNode.velocity, fieldUnitNode.velocity, Troop.VELOCITY_DRAG);
			
			//Vector3.getNormalized(troop.velocity, troop.velocity);
			//Vector3.getNormalized(accumulator, accumulator); 
			// Either this or some constant to relate screen force to movement speed for the troop 
			
			if (Double.isNaN(fieldUnitNode.velocity.getMagnitude2d())) {
				Log.i("NAN", "FIELD");
				Log.i("NAN FIELD", fieldUnitNode.velocity.x + "," + fieldUnitNode.velocity.y + "," + fieldUnitNode.velocity.z);
			}
			
			fieldUnitNode.fieldForce.copy(accumulator);
			fieldUnitNode.fieldForce.scale(fieldUnitNode.fieldForceSensitivity[0], fieldUnitNode.fieldForceSensitivity[0], 1);
			
			// Orientation Calculation based on normalized field force
//			Vector3.getNormalized(accumulator, accumulator);
//			fieldUnitNode.orientation.addVector(accumulator.x, accumulator.y, Constants.TURNING_RATE);

			// Add fieldForce to velocity
			//Vector3.add(fieldUnitNode.velocity, fieldUnitNode.velocity, fieldUnitNode.fieldForce);
			//fieldUnitNode.velocity.clampMagnitude(Troop.MAX_MOVE_SPEED);
			
			// Update
			//Vector3.add(fieldUnitNode.position, fieldUnitNode.position, fieldUnitNode.velocity);

//			accumulator.copy(fieldUnitNode.velocity);
//			Vector3.getNormalized(accumulator, accumulator);
//			
//			fieldUnitNode.orientation.addVector(accumulator.x, accumulator.y, Troop.TURNING_RATE);
			
		}
		
	}
}
