package structure;

import java.util.ArrayList;

import utils.MemoryPool;

/**
 * Circular Buffer. Writing tries to cycle through
 * buffers if possible. This allows graphics to access most
 * recent written buffer with concurrent writes.
 * If graphics holds onto a buffer for too long, the
 * writing will write to the same buffer. Starvation is averted
 * because it is assumed that the graphics will give up its
 * currently held buffer before a subsequent access, during which
 * time writing will have access the given up buffer.
 * To make sure this holds true, the writing buffer will be swapped 
 * on a read request rather than waiting for the write request.
 * If drawing takes too long, the writing buffer may be rewritten several times!
 * To prevent starvation of the reading buffer against a super fast producer,
 * the swap request is synchronized with a write buffer access request. 
 */
public class ReadWriteSyncBuffer {
	
	public static int capacity = 10000;

	private ArrayList<Object> list0;
	private ArrayList<Object> list1;
	

	private boolean externalAccessLock = false;
	private ArrayList<Object> activeList;
	
	/**
	 * If the updateFlag is true, this means that writer has 
	 * created some data which can be consumed. It can be false
	 * if the reader has consumed data before new data is written,
	 * during which a stale list is returned for the reader.
	 */
	private boolean updateFlag;
	
	public ReadWriteSyncBuffer(int capacity) {
		list0 = new ArrayList<Object>();
		list1 = new ArrayList<Object>();

		this.capacity = capacity;
		list0.ensureCapacity(capacity);
		list1.ensureCapacity(capacity);

		activeList = list0;

		updateFlag = false;
	}
	
	
//	private void copyList(ArrayList<Unit> list, ArrayList<SimpleShaderSprite> listToCopy) {
//		recycleList(list); // empty list
//		// Use this to avoid garbage collecting iterators
//		for (int i = 0; i < listToCopy.size(); i++) {
//			Unit unit = game.unitMemoryPool.fetchUnit(); // Fetch memory
//			unit.copy( listToCopy.get(i) ); // Copy into memory
//			list.add(unit);			
//		}
//	}
//	
	
//	private void recycleList(ArrayList<SimpleShaderSprite> list) {
//		for (int i = list.size() - 1; i > -1; i--) {
			//game.spriteMemoryPool.re(list.get(i));
			//list.remove(i); // TODO: verify assumption
		//}
	//}
	
	/**
	 * Only reading swaps buffers.
	 * @return
	 */
	public synchronized ArrayList<Object> swapBuffers() {
		if (updateFlag) {
			if (activeList == list0) {
				// Return unitList0 if we are ready
				// When returning unitList0, we must copy unitList1 to unitList0
	
				// TODO: We have to copy the activeList to the oldList!
				updateFlag = false;
				activeList = list1;
				return list0; 
			}
			else {
				updateFlag = false;
				activeList = list0;
				return list1;
			}	
		} 
		else {
			return activeList; // Return the list that isn't updated
		}
	}
	
	public synchronized void recycleList(ArrayList<Object> list, MemoryPool pool) {
		for (int i = list.size() - 1; i > -1; i--) {
			pool.recycleMemory(list.get(i));
			list.remove(i);
		}
	}
	
	/**
	 * Prevents updating the denormalizedLists while external updates are made to denormalizedLists.
	 * The write list is populated with previous data that was last used by graphics.
	 * Clearing the list may be needed.<br/>
	 * This method is synchronized with swapping requests to prevent starvation for the reader.
	 * @return
	 */
	public synchronized ArrayList<Object> lockWritableList() {
		externalAccessLock = true;
		return activeList;	
	}
	
	/**
	 * Unlock list
	 */
	public synchronized void unlockWriteableList() {
		updateFlag = true;
		externalAccessLock = false;
	}

}
