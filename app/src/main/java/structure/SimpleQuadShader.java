package structure;

import android.content.res.Resources;
import android.graphics.Color;
import android.opengl.GLES20;
import android.util.Log;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.HashMap;

import game.androidgame2.R;

/**
 * Created by eric on 3/24/15.
 */
public class SimpleQuadShader {

    /** How many bytes per float. */
    public static final int BYTES_PER_FLOAT = 4;

    /** Size of the position data in elements. */
    public static final int NUMBER_POSITION_ELEMENTS = 3;

    /** Size of the color data in elements. */
    public static final int NUMBER_COLOR_ELEMENTS = 4;

    /** Size of the texture data in elements. */
    public static final int NUMBER_TEXTURE_ELEMENTS = 2;

    private int programHandle = -1;

    //public static final String SHADER_UNIFORM_MVPMATRIX = "u_MVPMatrix";
    public static final String SHADER_UNIFORM_TEXTURE = "u_Texture";
    public static final String SHADER_UNIFORM_FLAT_COLOR = "u_FlatColor";
    public static final String SHADER_UNIFORM_PROJECTION_MATRIX = "u_ProjectionMatrix";
    public static final String SHADER_UNIFORM_MODEL_MATRIX = "u_ModelMatrix";
    public static final String SHADER_UNIFORM_SCALING_X = "u_ScalingX";
    public static final String SHADER_UNIFORM_SCALING_Y = "u_ScalingY";
    public static final String SHADER_UNIFORM_ANGLE = "u_Angle";
    public static final String SHADER_UNIFORM_TRANSLATION_X = "u_TranslationX";
    public static final String SHADER_UNIFORM_TRANSLATION_Y = "u_TranslationY";

    public static final String SHADER_ATTRIBUTE_POSITION = "a_Position";
    //public static final String SHADER_ATTRIBUTE_COLOR = "a_Color";
    public static final String SHADER_ATTRIBUTE_TEXTURE = "a_TexCoord";

    //private int shaderUniformMvpMatrixLocation = 0;
    private int shaderUniformTextureLocation = 0;
    private int shaderUniformFlatColorLocation = 0;
    private int shaderUniformProjectionMatrix = 0;
    private int shaderUniformModelMatrix = 0;
    private int shaderUniformScalingX = 0;
    private int shaderUniformScalingY = 0;
    private int shaderUniformAngle = 0;
    private int shaderUniformTranslationX = 0;
    private int shaderUniformTranslationY = 0;

    private int shaderAttributePositionLocation = 0;
    private int shaderAttributeColorLocation = 0;
    private int shaderAttributeTextureLocation = 0;

    public static final int VERTEX_SHADER_RESOURCE = R.raw.simple_quad_vertex_shader;
    public static final int FRAGMENT_SHADER_RESOURCE = R.raw.simple_quad_fragment_shader;

    /**
     * Stores the locations of the shader variables
     */
    private HashMap<String, Integer> locations;

    private Resources resources;

    private String vertexShaderSource;
    private String fragmentShaderSource;
    private int vertexShaderHandle;
    private int fragmentShaderHandle;

    private int cacheGlTexture = -1;

    private boolean isInitialized = false;


    public SimpleQuadShader(Resources resources) {
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
                throw new RuntimeException("Error compiling vertex shader.");
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
                throw new RuntimeException("Error compiling fragment shader.");
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
        //shaderUniformMvpMatrixLocation = GLES20.glGetUniformLocation(programHandle, SHADER_UNIFORM_MVPMATRIX);
        shaderUniformTextureLocation = GLES20.glGetUniformLocation(programHandle, SHADER_UNIFORM_TEXTURE);
        shaderUniformFlatColorLocation = GLES20.glGetUniformLocation(programHandle, SHADER_UNIFORM_FLAT_COLOR);
        shaderUniformProjectionMatrix = GLES20.glGetUniformLocation(programHandle, SHADER_UNIFORM_PROJECTION_MATRIX);
        shaderUniformModelMatrix = GLES20.glGetUniformLocation(programHandle, SHADER_UNIFORM_MODEL_MATRIX);

        shaderUniformScalingX = GLES20.glGetUniformLocation(programHandle, SHADER_UNIFORM_SCALING_X);
        shaderUniformScalingY = GLES20.glGetUniformLocation(programHandle, SHADER_UNIFORM_SCALING_Y);
        shaderUniformAngle = GLES20.glGetUniformLocation(programHandle, SHADER_UNIFORM_ANGLE);
        shaderUniformTranslationX = GLES20.glGetUniformLocation(programHandle, SHADER_UNIFORM_TRANSLATION_X);
        shaderUniformTranslationY = GLES20.glGetUniformLocation(programHandle, SHADER_UNIFORM_TRANSLATION_Y);

        shaderAttributePositionLocation = GLES20.glGetAttribLocation(programHandle, SHADER_ATTRIBUTE_POSITION);
        //shaderAttributeColorLocation = GLES20.glGetAttribLocation(programHandle, SHADER_ATTRIBUTE_COLOR);
        shaderAttributeTextureLocation = GLES20.glGetAttribLocation(programHandle, SHADER_ATTRIBUTE_TEXTURE);

        //locations.put(SHADER_UNIFORM_MVPMATRIX, GLES20.glGetUniformLocation(programHandle, SHADER_UNIFORM_MVPMATRIX) );
        locations.put(SHADER_UNIFORM_TEXTURE, GLES20.glGetUniformLocation(programHandle, SHADER_UNIFORM_TEXTURE) );
        locations.put(SHADER_UNIFORM_PROJECTION_MATRIX, GLES20.glGetUniformLocation(programHandle, SHADER_UNIFORM_PROJECTION_MATRIX) );
        locations.put(SHADER_UNIFORM_MODEL_MATRIX, GLES20.glGetUniformLocation(programHandle, SHADER_UNIFORM_MODEL_MATRIX) );
        //locations.put(SHADER_UNIFORM_SCALING, shaderUniformScaling);
        //locations.put(SHADER_UNIFORM_ANGLE, shaderUniformAngle);
        //locations.put(SHADER_UNIFORM_TRANSLATION, shaderUniformTranslation);

        locations.put(SHADER_ATTRIBUTE_POSITION, GLES20.glGetAttribLocation(programHandle, SHADER_ATTRIBUTE_POSITION) );
        locations.put(SHADER_UNIFORM_FLAT_COLOR, GLES20.glGetAttribLocation(programHandle, SHADER_UNIFORM_FLAT_COLOR) );
        locations.put(SHADER_ATTRIBUTE_TEXTURE, GLES20.glGetAttribLocation(programHandle, SHADER_ATTRIBUTE_TEXTURE) );

        isInitialized = true;
    }

    public void useProgram() {
        GLES20.glUseProgram(programHandle);
    }

    public void setVertexAttributePointers(VertexBuffer positionBuffer, VertexBuffer textureBuffer) {
        // Pass in the position information
        positionBuffer.bindBuffer();

        GLES20.glVertexAttribPointer(shaderAttributePositionLocation,
                NUMBER_POSITION_ELEMENTS,
                GLES20.GL_FLOAT, false,
                0, 0);

        GLES20.glEnableVertexAttribArray(shaderAttributePositionLocation);

        // Tell texture uniform sampler to use texture in shader
        GLES20.glUniform1i(shaderUniformTextureLocation, 0);

        // Set Texture coords
        textureBuffer.bindBuffer();

        GLES20.glVertexAttribPointer(shaderAttributeTextureLocation,
                NUMBER_TEXTURE_ELEMENTS,
                GLES20.GL_FLOAT, false,
                0, 0);

        GLES20.glEnableVertexAttribArray(shaderAttributeTextureLocation);
    }

    public SimpleQuadShader setUniforms(float[] projectionMatrix, float scalingX, float scalingY, float angle, float translationX, float translationY, int color) {
        GLES20.glUniformMatrix4fv(shaderUniformProjectionMatrix, 1, false, projectionMatrix, 0);
        GLES20.glUniform1f(shaderUniformScalingX, scalingX);
        GLES20.glUniform1f(shaderUniformScalingY, scalingY);
        GLES20.glUniform1f(shaderUniformAngle, angle);
        GLES20.glUniform1f(shaderUniformTranslationX, translationX);
        GLES20.glUniform1f(shaderUniformTranslationY, translationY);

        GLES20.glUniform4f(shaderUniformFlatColorLocation,
                Color.red(color),
                Color.green(color),
                Color.blue(color),
                Color.alpha(color));

        return this;
    }

    public void drawArrays(int mode, int count) {
        GLES20.glDrawArrays(mode, 0, count);
    }

    /**
     *
     * @param projectionMatrix
     * @param modelMatrix
     * @param positionBuffer Buffer of triangle points, Gets bound to vertex attribute pointer related to position
     * @param textureHandle GlBindTexture on this handle
     * @param textureBuffer Gets bound to shader uniform
     * @param mode
     * @param count
     */
    public void draw(float[] projectionMatrix, float scalingX, float scalingY, float angle, float translationX, float translationY, VertexBuffer positionBuffer, int color, int textureHandle, VertexBuffer textureBuffer, int mode, int count) {

        setUniforms(projectionMatrix, scalingX, scalingY, angle, translationX, translationY, color);
        setVertexAttributePointers(positionBuffer, textureBuffer);

        GLES20.glDrawArrays(mode, 0, count);
    }


    /**
     * Do not use. It is here for reference.
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
    public void draw(float[] projectionMatrix, float[] modelMatrix, FloatBuffer positionBuffer, FloatBuffer colorBuffer, int textureHandle, FloatBuffer textureBuffer, int mode, int count) {
        // Pass in mvpMatrix
        //GLES20.glUniformMatrix4fv(locations.get(SHADER_UNIFORM_MVPMATRIX), 1, false, mvpMatrix, 0);
        //GLES20.glUniformMatrix4fv(shaderUniformMvpMatrixLocation, 1, false, mvpMatrix, 0);
        GLES20.glUniformMatrix4fv(shaderUniformProjectionMatrix, 1, false, projectionMatrix, 0);
        GLES20.glUniformMatrix4fv(shaderUniformModelMatrix, 1, false, modelMatrix, 0);

        // Pass in the position information
        positionBuffer.position(0);
        //GLES20.glVertexAttribPointer(locations.get(SHADER_ATTRIBUTE_POSITION),
        GLES20.glVertexAttribPointer(shaderAttributePositionLocation,
                NUMBER_POSITION_ELEMENTS,
                GLES20.GL_FLOAT, false,
                0, positionBuffer);
        //GLES20.glEnableVertexAttribArray(locations.get(SHADER_ATTRIBUTE_POSITION));
        GLES20.glEnableVertexAttribArray(shaderAttributePositionLocation);

        // Pass in the color information
        colorBuffer.position(0);
        //GLES20.glVertexAttribPointer(locations.get(SHADER_ATTRIBUTE_COLOR),
        GLES20.glVertexAttribPointer(shaderAttributeColorLocation,
                NUMBER_COLOR_ELEMENTS,
                GLES20.GL_FLOAT, false,
                0, colorBuffer);
        //GLES20.glEnableVertexAttribArray(locations.get(SHADER_ATTRIBUTE_COLOR));
        GLES20.glEnableVertexAttribArray(shaderAttributeColorLocation);


        // Textures
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);

        // Tell texture uniform sampler to use texture in shader
        //GLES20.glUniform1i(locations.get(SHADER_UNIFORM_TEXTURE), 0);
        GLES20.glUniform1i(shaderUniformTextureLocation, 0);

        // Set Texture coords
        textureBuffer.position(0);
        //GLES20.glVertexAttribPointer(locations.get(SHADER_ATTRIBUTE_TEXTURE),
        GLES20.glVertexAttribPointer(shaderAttributeTextureLocation,
                NUMBER_TEXTURE_ELEMENTS,
                GLES20.GL_FLOAT, false,
                0, textureBuffer);
        //GLES20.glEnableVertexAttribArray(locations.get(SHADER_ATTRIBUTE_TEXTURE));
        GLES20.glEnableVertexAttribArray(shaderAttributeTextureLocation);

        GLES20.glDrawArrays(mode, 0, count);

    }

    public boolean isInitialized() {
        return isInitialized;
    }
}
