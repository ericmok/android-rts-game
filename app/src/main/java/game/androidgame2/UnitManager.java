package game.androidgame2;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * TODO: WAIT! if one list changes, it needs to copy to the other list...
 * Allocation problems
 * 
 * @author Eric
 *
 */
public class UnitManager {

	private ArrayList<Unit> unitList0;
	private ArrayList<Unit> unitList1;
	
	//private Game game;
	private MemoryPool<Unit> unitMemoryPool;

	private boolean externalAccessLock;
	private ArrayList<Unit> activeList;
	
	public UnitManager(MemoryPool<Unit> unitMemoryPool) {
		unitList0 = new ArrayList<Unit>();
		unitList1 = new ArrayList<Unit>();

//		this.game = game;
		this.unitMemoryPool = unitMemoryPool;
		
		activeList = unitList0;

		externalAccessLock = false;
	}
	
	
	private void copyList(ArrayList<Unit> list, ArrayList<Unit> listToCopy) {
		recycleList(list); // empty list
		// Use this to avoid garbage collecting iterators
		for (int i = 0; i < listToCopy.size(); i++) {
			Unit unit = unitMemoryPool.fetchMemory(); // Fetch memory
			unit.copy( listToCopy.get(i) ); // Copy into memory
			list.add(unit);			
		}
	}
	
	
	private void recycleList(ArrayList<Unit> list) {
		for (int i = list.size() - 1; i > -1; i--) {
			unitMemoryPool.recycleMemory(list.get(i));
			list.remove(i); // TODO: verify assumption
		}
	}
	
	/**
	 * TODO: The graphics will be starved!!!
	 * <strong>Assumptions:</strong><br/>
	 * <ul>
	 * <li>Graphic engine no longer needs the list being swapped!</li>
	 * <li>Performance hit on swap should be small</li>
	 * <li>Updates should stop after request has been made so ensure
	 * a swap is completed ASAP.</li>
	 * <li>Cannot run game logic faster than it draws</li>
	 * </ul>
	 * Return the list that is active (most recently updated)
	 * Swap with the list that is inactive only if list is already updated.
	 * Locks isUpdating variable. activeList won't swap until until method succeeds.
	 * @return ArrayList of units that is most recently updated
	 */
	public synchronized ArrayList<Unit> swapUnitList() {
	
		if (activeList == unitList0) {
			// Return unitList0 if we are ready
			// When returning unitList0, we must copy unitList1 to unitList0

			// TODO: We have to copy the activeList to the oldList!
			this.copyList(unitList1, unitList0);
			activeList = unitList1;
			return unitList0; 
		}
		else {
			this.copyList(unitList0, unitList1);
			activeList = unitList0;
			return unitList1;
		}	
	}
	
	/**
	 * Prevents updating the lists while external updates are made to lists.
	 * @return
	 */
	public synchronized ArrayList<Unit> lockCurrentList() {
		externalAccessLock = true;
		return activeList;	
	}
	
	/**
	 * Unlock list
	 */
	public synchronized void unlockList() {
		if (activeList == unitList0) {
			this.copyList(unitList1, unitList0);
		}
		else {
			this.copyList(unitList0, unitList1);
		}
		externalAccessLock = false;
	}

	/**
	 * Updates the active unitList. 
	 * When drawing, call <b>swapUnitList</b> to swap active buffers.
	 * 
	 * NO LONGER TRUE: An old buffer is returned if the new one is updating so this call will not block for updating delay
	 * allowing drawing to draw other things.
	 * @param elapsedTime
	 */
	public synchronized void update(long elapsedTime) {
		if (externalAccessLock) {
			return;
		}
		if (activeList == unitList0) {
			// Write to unitList0

			for (int i = 0; i < unitList0.size(); i++) {
				unitList0.get(i).update(elapsedTime);
			}

		}
		else {
			// Write to unitList1

			for (int i = 0; i < unitList1.size(); i++) {
				unitList1.get(i).update(elapsedTime);
			}

		}
	}
}
