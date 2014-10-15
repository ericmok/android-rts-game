package game.androidgame2;

import java.util.ArrayList;
import java.util.LinkedList;

public class UnitMemoryPool {
	
	public static final int CAPACITY = 10000;
	
	//private LinkedList<Unit> freeList;
	
	private Unit[] freeArray;
	private ArrayList<Object> pointers;

	//private int freeUnits;
	
	public UnitMemoryPool() {
//		freeList = new LinkedList<Unit>();
//		for (int i = 0; i < CAPACITY; i++) {
//			Unit unit = new Unit();
//			freeList.add(unit);
//		}
//		freeUnits = CAPACITY;
		
		freeArray = new Unit[CAPACITY];
		pointers = new ArrayList<Object>();
		pointers.ensureCapacity(CAPACITY);
		
		for (int i = 0; i < CAPACITY; i++) {
			freeArray[i] = new Unit();
			pointers.add(freeArray[i]);
		}
		
	}
	
	/**
	 * Puts unit into freeList. 
	 * Unit ownership should transfer to memory pool 
	 * @param unit
	 */
	public void recycleUnit(Unit unit) {
		
		//freeList.add(unit); // TODO: THIS CAUSES AN ALLOCATION
		//freeUnits += 1;
		pointers.add(unit);
	}
	
	/**
	 * Get a unit from free list. 
	 * If pool is empty, allocate new unit
	 * @return
	 */
	public Unit fetchUnit() {
//		if (freeUnits < 1) {
//			return new Unit();
//		}
//		Unit ret = freeList.pop();
//		freeUnits -= 1;
//		return ret;
		if (pointers.size() < 1) {
			Unit unit = new Unit();
			return unit;
		}
		Unit unit = (Unit) pointers.remove(pointers.size() - 1);
		return unit;
	}
}
