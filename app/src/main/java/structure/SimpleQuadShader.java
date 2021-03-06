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

    private Resources resources;

//    private String vertexShaderSource;
//    private String fragmentShaderSource;
//    private int vertexShaderHandle;
//    private int fragmentShaderHandle;

    private Shader shader;

    private int cacheGlTexture = -1;

    private boolean isInitialized = false;


    public SimpleQuadShader(Resources resources) {
        this.resources = resources;
    }

    public String getRawResourceString(int resource) throws Exception {
        String ret;
        InputStream inputStream = resources.openRawResource(resource);

        try {
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            ret = new String(b);
            return ret;
        }
        catch (Exception e) {
            throw new Exception("Cannot read resource!");
        }
    }

    public void initializeResources() throws Exception {
        String vertexShaderSource;
        String fragmentShaderSource;

        try {
            vertexShaderSource = getRawResourceString(VERTEX_SHADER_RESOURCE);
        }
        catch (Exception e) {
            Log.d("SimpleQuadShader", "Failed to read vertex shader resource");
            throw new Exception("Cannot read vertex shader resource!");
        }

        try {
            fragmentShaderSource = getRawResourceString(FRAGMENT_SHADER_RESOURCE);
        }
        catch (Exception e) {
            Log.d("SimpleQuadShader", "Failed to read fragment shader resource");
            throw new Exception("Cannot read fragment shader resource!");
        }

        this.shader = new Shader(vertexShaderSource, fragmentShaderSource);
        this.programHandle = shader.compile();
        shader.useProgram();

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

        isInitialized = true;
    }

    public void useProgram() {
        shader.useProgram();
    }

    public void setVertexAttributePointers(VertexBuffer positionBuffer, VertexBuffer textureBuffer) {
        setPositionVertexAttributePointers(positionBuffer);
        setTextureVertexAttributePointer(textureBuffer);
    }

    public void setPositionVertexAttributePointers(int glPositionBuffer) {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glPositionBuffer);

        GLES20.glVertexAttribPointer(shaderAttributePositionLocation,
                NUMBER_POSITION_ELEMENTS,
                GLES20.GL_FLOAT, false,
                0, 0);

        GLES20.glEnableVertexAttribArray(shaderAttributePositionLocation);
    }

    public void setPositionVertexAttributePointers(VertexBuffer positionBuffer) {
        setPositionVertexAttributePointers(positionBuffer.getGLHandle());
    }

    public void setTextureVertexAttributePointer(int glTextureCoordsBuffer) {
        // Tell texture uniform sampler to use texture in shader
        GLES20.glUniform1i(shaderUniformTextureLocation, 0);

        // Set Texture coords
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glTextureCoordsBuffer);

        GLES20.glVertexAttribPointer(shaderAttributeTextureLocation,
                NUMBER_TEXTURE_ELEMENTS,
                GLES20.GL_FLOAT, false,
                0, 0);

        GLES20.glEnableVertexAttribArray(shaderAttributeTextureLocation);
    }

    public void setTextureVertexAttributePointer(VertexBuffer textureBuffer) {
        setTextureVertexAttributePointer(textureBuffer.getGLHandle());
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
        //GLES20.glUniformMatrix4fv(shaderUniformMvpMatrixLocation, 1, false, mvpMatrix, 0);
        GLES20.glUniformMatrix4fv(shaderUniformProjectionMatrix, 1, false, projectionMatrix, 0);
        GLES20.glUniformMatrix4fv(shaderUniformModelMatrix, 1, false, modelMatrix, 0);

        // Pass in the position information
        positionBuffer.position(0);
        GLES20.glVertexAttribPointer(shaderAttributePositionLocation,
                NUMBER_POSITION_ELEMENTS,
                GLES20.GL_FLOAT, false,
                0, positionBuffer);
        GLES20.glEnableVertexAttribArray(shaderAttributePositionLocation);

        // Pass in the color information
        colorBuffer.position(0);
        GLES20.glVertexAttribPointer(shaderAttributeColorLocation,
                NUMBER_COLOR_ELEMENTS,
                GLES20.GL_FLOAT, false,
                0, colorBuffer);
        GLES20.glEnableVertexAttribArray(shaderAttributeColorLocation);


        // Textures
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);

        // Tell texture uniform sampler to use texture in shader
        GLES20.glUniform1i(shaderUniformTextureLocation, 0);

        // Set Texture coords
        textureBuffer.position(0);

        GLES20.glVertexAttribPointer(shaderAttributeTextureLocation,
                NUMBER_TEXTURE_ELEMENTS,
                GLES20.GL_FLOAT, false,
                0, textureBuffer);

        GLES20.glEnableVertexAttribArray(shaderAttributeTextureLocation);

        GLES20.glDrawArrays(mode, 0, count);

    }

    public boolean isInitialized() {
        return isInitialized;
    }
}
