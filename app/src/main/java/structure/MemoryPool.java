package structure;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MemoryPool<E> {
	
		private int capacity = 10000;
		
		//private LinkedList<Unit> freeList;
		
		private Class<E> cls;
		private E[] freeArray; // Total Memory
		private ArrayList<Object> pointers; // Free Memory

		//private int freeUnits;
		
		public MemoryPool(Class<E> cls, int capacity) {
			
			this.cls = cls;
			this.capacity = capacity;
			freeArray = (E[])Array.newInstance(cls, capacity);
			pointers = new ArrayList<Object>();
			pointers.ensureCapacity(capacity);
			
			for (int i = 0; i < capacity; i++) {
				try {
					freeArray[i] = cls.newInstance();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pointers.add(freeArray[i]);
			}
			
		}
		

		public int getCapacity() { return capacity; }

		
		/**
		 * Puts unit into freeList. 
		 * Unit ownership should transfer to memory pool 
		 * @param unit
		 */
		public synchronized void recycleMemory(E unit) {
			
			//freeList.add(unit); // TODO: THIS CAUSES AN ALLOCATION
			//freeUnits += 1;
			pointers.add(unit);
		}
		
		/**
		 * Get a unit from free list. 
		 * If pool is empty, allocate new unit
		 * @return
		 */
		public synchronized E fetchMemory() {
//			if (freeUnits < 1) {
//				return new Unit();
//			}
//			Unit ret = freeList.pop();
//			freeUnits -= 1;
//			return ret;
			if (pointers.size() < 1) {
				E obj;
				try {
					obj = cls.newInstance();
					return obj;
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			E unit = (E) pointers.remove(pointers.size() - 1);
			return unit;
		}

		/**
		 * Recycles an array list of the item of the memory pool type
		 * @param list
		 */
		public void recycleArrayList(ArrayList<E> list) {
			for (int i = list.size() - 1; i > -1; i--) {
				this.recycleMemory( list.get(i) );
				list.remove(i);
			}
		}
}
