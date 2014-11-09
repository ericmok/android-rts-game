package structure;

import java.util.ArrayList;

public class DoubleBufferredArrayList<E> {
	
	// TODO: Make these array denormalizedLists into rewritable arrays
	private ArrayList<E> buffer1;
	private ArrayList<E> buffer2;
		
	/**
	 * List that is actively written to
	 */
	private ArrayList<E> activeList;
	
	private Class cls;

	private boolean writeLock = false;
	public Object isUpdatedMutex;
	private boolean isUpdated = false;
	
	public DoubleBufferredArrayList(Class cls, int capacity) {
		buffer1 = new ArrayList<E>();
		buffer2 = new ArrayList<E>();
		activeList = buffer1;
		
		buffer1.ensureCapacity(capacity);
		buffer2.ensureCapacity(capacity);
		
		isUpdatedMutex = new Object();
		
		this.cls = cls;
	}
	
	// TODO: Return null if locked by someone else
	public ArrayList<E> lockWritableBuffer() {
		writeLock = true;
		return activeList;	
	}
	
	public void unlockWritableBuffer() {
		writeLock = false;
	}
	
	public void finalizeUpdate() {
		synchronized(isUpdatedMutex) {
			isUpdated = true;	
		}
	}
	
	
	private ArrayList<E> otherList(ArrayList<E> list) {
		if (list == buffer1) {
			return buffer2;
		}
		else {
			return buffer1;
		}
	}
	
	
	/**
	 * Swap buffers if updated. Else return non active list.
	 * Only reading allows swapping
	 * @return
	 */
	public synchronized ArrayList<E> swapBuffer() {
		
		synchronized(isUpdatedMutex) {
			
			// If active list is updated since last swap
			// Swap activeList pointer
			// return the active list that was just updated
			if (isUpdated) {
				
				ArrayList<E> temp = activeList;
				activeList = otherList(activeList);
				isUpdated = false;
				return temp;
			}
			else {
				
				// If not updated, just return the non-active list
				return otherList(activeList);	
			}
		}
		
	}
	
	
}
