package game.androidgame2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Utilities {
	
	public static int BYTES_PER_FLOAT = 4;
	
	public static FloatBuffer allocateFloatBuffer(int bytes) {
		return ByteBuffer.allocateDirect(bytes)
        .order(ByteOrder.nativeOrder()).asFloatBuffer();
	}
	
	public static FloatBuffer allocateFloatBuffer(float[] data) {
		FloatBuffer temp = ByteBuffer.allocateDirect(data.length * BYTES_PER_FLOAT)
		.order(ByteOrder.nativeOrder()).asFloatBuffer();
		temp.put(data).position(0);
		
		return temp;
	}
	
}
