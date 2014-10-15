package tenth.system;

import java.util.ArrayList;

import game.androidgame2.Orientation;
import game.androidgame2.Vector2;
import game.androidgame2.Vector3;

public class SquadFormation {
	
	public ArrayList<SquadPosition> positions;
	
	public SquadPosition leader;
	
	public SquadFormation() {
		leader = new SquadPosition();
		positions = new ArrayList<SquadPosition>();
	}
	
	public void generatePositionsFrom(Vector3 output, Vector3 position, Orientation direction) {
		double pointX = (-direction.y / direction.x);
		
		output.x = pointX;
		output.y = 1;
		output.z = 1;
		output.setNormalized2d();
		
		output.x += position.x;
		output.y += position.y;
		output.z += position.z;
	}
}
