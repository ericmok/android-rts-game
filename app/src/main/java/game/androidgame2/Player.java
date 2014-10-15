package game.androidgame2;

import java.util.ArrayList;

import tenth.system.SquadPosition;
import tenth.system.SystemNode;

public class Player {
	
	public static final int MAX_UNITS = 256;
	public static final int MAX_FIELDS = 64;
	
	public UnitArrayList units;
		
	public ArrayList<TriggerField> fields;
	
	public RewritableArray<SquadPosition> squadPositions;

	public String name;
	
	public Player(String name) {
		this.name = name;
		
		units = new UnitArrayList(MAX_UNITS);
		fields = new ArrayList<TriggerField>(MAX_FIELDS);
		
		squadPositions = new RewritableArray<SquadPosition>(SquadPosition.class, MAX_UNITS * 10);	
	}
}
