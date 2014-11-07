package structure;

import java.util.ArrayList;

/**
 * GameThread updates its own data structures as often as possible.
 * Graphics will access output buffer as often as possible.
 * To make sure both can access their own buffers at the same time,
 * make sure that input buffers do not block game internal data. 
 */
public class Game2DSpriteBuffer {

	private ArrayList<Game2DSprite> inputBuffer;
	private ArrayList<Game2DSprite> outputBuffer;
	
	/**
	 * Lock object for buffer
	 */
	public Object bufferAccessLockMutex;
	public boolean bufferAccessLock = false; 
	
	/** 
	 * Lock object for output
	 */
	public Object outputLockMutex;
	public boolean outputLock = false;
	
	public Game2DSpriteBuffer() {
		inputBuffer = new ArrayList<Game2DSprite>();
		//graphicsBuffer = new ArrayList<Game2DSprite>();
	}
	
	public synchronized ArrayList<Game2DSprite> lockBuffer() {
		bufferAccessLock = true;
		return inputBuffer;
	}
	
	public synchronized void unlockBuffer() {
		bufferAccessLock = false;
	}
	
	/**
	 * TODO: The translation of game data to graphics data will be done on a different thread.
	 */
//	public synchronized void copyBufferToOutput() {
//		for (int i = 0; i < graphicsBuffer.size(); i++) {
//			graphicsBuffer.remove(i);
//		}
//		for (int i = 0; i < inputBuffer.size(); i++) {
//			graphicsBuffer.add(inputBuffer.get(i));
//		}
//	}
//	
//	public synchronized ArrayList<Game2DSprite> getList() {
//		if (!bufferAccessLock) {
//			copyBufferToOutput();
//		}
//		return graphicsBuffer;
//	}
	
}
