package game.androidgame2;

import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;

/**
 * Crazy triple buffering
 * @author Eric
 *
 */
public class UnitManager2 {
	private ArrayList<Unit> processingList;
	private ArrayList<Unit> bufferList;
	private ArrayList<Unit> outputList;
	
	private Object bufferLock;
	
	//private Game game;
	private MemoryPool<Unit> unitMemoryPool;
	
	private boolean externalAccessLock;
	
	public UnitManager2(MemoryPool<Unit> unitMemoryPool) {
		processingList = new ArrayList<Unit>();
		outputList = new ArrayList<Unit>();
		bufferList = new ArrayList<Unit>();
		
		bufferLock = new Object();
		
		//this.game = game;
		this.unitMemoryPool = unitMemoryPool;
		
		externalAccessLock = false;
	}
	
	
	private void copyList(ArrayList<Unit> dest, ArrayList<Unit> listToCopy) {
		recycleList(dest); // empty list
		Iterator itr = listToCopy.iterator();
		while (itr.hasNext()) {
			Unit unit = unitMemoryPool.fetchMemory(); // Fetch memory
			unit.copy(  ( (Unit)itr.next() )  ); // Copy into memory
			dest.add(unit);			
		}
	}
	
	
	private void recycleList(ArrayList<Unit> list) {
		for (int i = list.size() - 1; i > -1; i--) {
			unitMemoryPool.recycleMemory(list.get(i));
			list.remove(i); // TODO: verify assumption
		}
	}
	
	/**
	 * Get a copy of the cache
	 * @return
	 */
	public ArrayList<Unit> swapUnitList() {
		synchronized (bufferLock) {
			this.copyList(outputList, bufferList);
		}
		return outputList;
	}
	
	/**
	 * Prevents updating the lists while external updates are made to lists.
	 * @return
	 */
	public synchronized ArrayList<Unit> lockCurrentList() {
		externalAccessLock = true;
		return processingList;	
	}
	
	/**
	 * Unlock list
	 */
	public synchronized void unlockList() {
		externalAccessLock = false;
	}

	/**
	 * Updates list. Copies to buffer.
	 * @param elapsedTime
	 */
	public void update(long elapsedTime) {
		if (externalAccessLock) {
			return;
		}
		Iterator itr = processingList.iterator();
		while (itr.hasNext()) {
			((Unit)itr.next()).update(elapsedTime);
		}
		synchronized (bufferLock) {
			this.copyList(bufferList, processingList);
		}
	}
}
