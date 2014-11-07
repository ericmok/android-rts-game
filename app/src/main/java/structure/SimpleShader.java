package structure;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.HashMap;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

import game.androidgame2.R;


public class SimpleShader {

	/** How many bytes per float. */
	public static final int BYTES_PER_FLOAT = 4;

	/** Size of the position data in elements. */
	public static final int NUMBER_POSITION_ELEMENTS = 3;

	/** Size of the color data in elements. */
	public static final int NUMBER_COLOR_ELEMENTS = 4;
	
	/** Size of the texture data in elements. */
	public static final int NUMBER_TEXTURE_ELEMENTS = 2;
	
	private int programHandle = -1;
	
	public static final String SHADER_UNIFORM_MVPMATRIX = "u_MVPMatrix";
	public static final String SHADER_UNIFORM_TEXTURE = "u_Texture";
	public static final String SHADER_ATTRIBUTE_POSITION = "a_Position";
	public static final String SHADER_ATTRIBUTE_COLOR = "a_Color";
	public static final String SHADER_ATTRIBUTE_TEXTURE = "a_TexCoord";
	
	public static final int VERTEX_SHADER_RESOURCE = R.raw.texturevertexshader;
	public static final int FRAGMENT_SHADER_RESOURCE = R.raw.texturefragmentshader;
	
	/**
	 * Stores the locations of the shader variables
	 */
	private HashMap<String, Integer> locations;	
	
	private Resources resources;
	
	private String vertexShaderSource;
	private String fragmentShaderSource;
	private int vertexShaderHandle;
	private int fragmentShaderHandle;
	
	private boolean isInitialized = false;
	
	
	public SimpleShader(Resources resources) {	
		this.resources = resources;
		
		locations = new HashMap<String, Integer>();
	}
	
	public void initializeResources() throws Exception {

		InputStream inputStream = resources.openRawResource(VERTEX_SHADER_RESOURCE);
		
		try {
			byte[] b = new byte[inputStream.available()];
			inputStream.read(b);
			vertexShaderSource = new String(b);
		}
		catch (Exception e) {
			Log.d("INIT", "Cannot read vshader resource!");
			throw new Exception("Cannot read vshader resource!");
		}
		
		try {
			inputStream = resources.openRawResource(FRAGMENT_SHADER_RESOURCE);
			byte[] b = new byte[inputStream.available()];
			inputStream.read(b);
			fragmentShaderSource = new String(b);
		}
		catch (Exception e) {
			Log.d("INIT", "Cannot read fshader resource!");
			throw new Exception("Cannot read vshader resource!");
		}	
									

		// Load in the vertex shader.
		vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

		if (vertexShaderHandle != 0) 
		{
			// Pass in the shader source.
			GLES20.glShaderSource(vertexShaderHandle, vertexShaderSource);

			// Compile the shader.
			GLES20.glCompileShader(vertexShaderHandle);

			// Get the compilation status.
			final int[] compileStatus = new int[1];
			GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

			// If the compilation failed, delete the shader.
			if (compileStatus[0] == 0) 
			{				
				GLES20.glDeleteShader(vertexShaderHandle);
				vertexShaderHandle = 0;
			}
		}

		if (vertexShaderHandle == 0)
		{
			throw new Exception("Error creating vertex shader.");
		}

		// Load in the fragment shader shader.
		fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

		if (fragmentShaderHandle != 0) 
		{
			// Pass in the shader source.
			GLES20.glShaderSource(fragmentShaderHandle, fragmentShaderSource);

			// Compile the shader.
			GLES20.glCompileShader(fragmentShaderHandle);

			// Get the compilation status.
			final int[] compileStatus = new int[1];
			GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

			// If the compilation failed, delete the shader.
			if (compileStatus[0] == 0) 
			{				
				GLES20.glDeleteShader(fragmentShaderHandle);
				fragmentShaderHandle = 0;
			}
		}

		if (fragmentShaderHandle == 0)
		{
			throw new RuntimeException("Error creating fragment shader.");
		}

		// Create a program object and store the handle to it.
		this.programHandle = GLES20.glCreateProgram();

		if (programHandle != 0) 
		{
			// Bind the vertex shader to the program.
			GLES20.glAttachShader(programHandle, vertexShaderHandle);			

			// Bind the fragment shader to the program.
			GLES20.glAttachShader(programHandle, fragmentShaderHandle);

			// Bind attributes
			GLES20.glBindAttribLocation(programHandle, 0, SHADER_ATTRIBUTE_POSITION);
			GLES20.glBindAttribLocation(programHandle, 1, SHADER_ATTRIBUTE_COLOR);
			GLES20.glBindAttribLocation(programHandle, 2, SHADER_ATTRIBUTE_TEXTURE);

			// Link the two shaders together into a program.
			GLES20.glLinkProgram(programHandle);

			// Get the link status.
			final int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

			// If the link failed, delete the program.
			if (linkStatus[0] == 0) 
			{				
				GLES20.glDeleteProgram(programHandle);
				programHandle = 0;
			}
		}

		if (programHandle == 0)
		{
			throw new RuntimeException("Error creating program.");
		}
        
        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(programHandle);   
 
        // Set program handles. These will later be used to pass in values to the program.
        locations.put(SHADER_UNIFORM_MVPMATRIX, GLES20.glGetUniformLocation(programHandle, SHADER_UNIFORM_MVPMATRIX) );
        locations.put(SHADER_UNIFORM_TEXTURE, GLES20.glGetUniformLocation(programHandle, SHADER_UNIFORM_TEXTURE) );
        locations.put(SHADER_ATTRIBUTE_POSITION, GLES20.glGetAttribLocation(programHandle, SHADER_ATTRIBUTE_POSITION) );
        locations.put(SHADER_ATTRIBUTE_COLOR, GLES20.glGetAttribLocation(programHandle, SHADER_ATTRIBUTE_COLOR) );
        locations.put(SHADER_ATTRIBUTE_TEXTURE, GLES20.glGetAttribLocation(programHandle, SHADER_ATTRIBUTE_TEXTURE) );
        
        isInitialized = true;
	}

	public void useProgram() {
		GLES20.glUseProgram(programHandle);
	}
	
	
	/**
	 * 
	 * @param mvpMatrix
	 * @param positionBuffer Buffer of triangle points, Gets bound to vertex attribute pointer related to position
	 * @param colorBuffer 
	 * @param textureHandle GlBindTexture on this handle
	 * @param textureBuffer Gets bound to shader uniform
	 * @param mode
	 * @param count
	 */
	public void draw(float[] mvpMatrix, VertexBuffer positionBuffer, VertexBuffer colorBuffer, int textureHandle, VertexBuffer textureBuffer, int mode, int count) {
		// Pass in mvpMatrix
        GLES20.glUniformMatrix4fv(locations.get(SHADER_UNIFORM_MVPMATRIX), 1, false, mvpMatrix, 0);
		        
		// Pass in the position information
        positionBuffer.bindBuffer();
        GLES20.glVertexAttribPointer(locations.get(SHADER_ATTRIBUTE_POSITION), 
        							NUMBER_POSITION_ELEMENTS, 
        							GLES20.GL_FLOAT, false,
        							0, 0);        
        GLES20.glEnableVertexAttribArray(locations.get(SHADER_ATTRIBUTE_POSITION));        
        
        // Pass in the color information
        colorBuffer.bindBuffer();
        GLES20.glVertexAttribPointer(locations.get(SHADER_ATTRIBUTE_COLOR), 
        							NUMBER_COLOR_ELEMENTS, 
        							GLES20.GL_FLOAT, false,
        							0, 0);        
        GLES20.glEnableVertexAttribArray(locations.get(SHADER_ATTRIBUTE_COLOR));
        

        // Textures
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);
        
		// Tell texture uniform sampler to use texture in shader
        GLES20.glUniform1i(locations.get(SHADER_UNIFORM_TEXTURE), 0);

        // Set Texture coords
        textureBuffer.bindBuffer();
        GLES20.glVertexAttribPointer(locations.get(SHADER_ATTRIBUTE_TEXTURE), 
        							NUMBER_TEXTURE_ELEMENTS, 
        							GLES20.GL_FLOAT, false,
        							0, 0);        
        GLES20.glEnableVertexAttribArray(locations.get(SHADER_ATTRIBUTE_TEXTURE));

        GLES20.glDrawArrays(mode, 0, count);			
   
	}
	
	
	/**
	 * Calls draw arrays after setting up shader variables
	 * @param mvpMatrix
	 * @param positionBuffer Remember to set position() to draw from correct indices 
	 * @param colorBuffer Remember to set position() to draw from correct indices
	 * @param textureHandle
	 * @param textureBuffer Remember to set position() to draw from correct indices
	 * @param mode GLES20.GL_TRIANGLE_STRIP for example
	 * @param count The number of indices to render
	 * <b>Preconditions:</b> Must have called initializedResources first
	 */
	public void draw(float[] mvpMatrix, FloatBuffer positionBuffer, FloatBuffer colorBuffer, int textureHandle, FloatBuffer textureBuffer, int mode, int count) {
		// Pass in mvpMatrix
        GLES20.glUniformMatrix4fv(locations.get(SHADER_UNIFORM_MVPMATRIX), 1, false, mvpMatrix, 0);
        
		// Pass in the position information
        positionBuffer.position(0);
        GLES20.glVertexAttribPointer(locations.get(SHADER_ATTRIBUTE_POSITION), 
        							NUMBER_POSITION_ELEMENTS, 
        							GLES20.GL_FLOAT, false,
        							0, positionBuffer);        
        GLES20.glEnableVertexAttribArray(locations.get(SHADER_ATTRIBUTE_POSITION));        
        
        // Pass in the color information
        colorBuffer.position(0);
        GLES20.glVertexAttribPointer(locations.get(SHADER_ATTRIBUTE_COLOR), 
        							NUMBER_COLOR_ELEMENTS, 
        							GLES20.GL_FLOAT, false,
        							0, colorBuffer);        
        GLES20.glEnableVertexAttribArray(locations.get(SHADER_ATTRIBUTE_COLOR));
        

        // Textures
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);
        
		// Tell texture uniform sampler to use texture in shader
        GLES20.glUniform1i(locations.get(SHADER_UNIFORM_TEXTURE), 0);

        // Set Texture coords
        textureBuffer.position(0);
        GLES20.glVertexAttribPointer(locations.get(SHADER_ATTRIBUTE_TEXTURE), 
        							NUMBER_TEXTURE_ELEMENTS, 
        							GLES20.GL_FLOAT, false,
        							0, textureBuffer);        
        GLES20.glEnableVertexAttribArray(locations.get(SHADER_ATTRIBUTE_TEXTURE));

        GLES20.glDrawArrays(mode, 0, count);			
   
	}
	
	public boolean isInitialized() {
		return isInitialized;
	}
}
