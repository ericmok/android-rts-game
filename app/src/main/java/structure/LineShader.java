package structure;

import android.content.res.Resources;
import android.graphics.Color;
import android.opengl.GLES20;
import android.util.Log;

import java.io.InputStream;
import java.nio.FloatBuffer;

import game.androidgame2.R;

public class LineShader {

    /** How many bytes per float. */
    public static final int BYTES_PER_FLOAT = 4;

    /** Size of the position data in elements. */
    public static final int NUMBER_POSITION_ELEMENTS = 3;

    private int programHandle = -1;

    public static final String SHADER_UNIFORM_FLAT_COLOR = "u_FlatColor";
    public static final String SHADER_UNIFORM_PROJECTION_MATRIX = "u_ProjectionMatrix";

    public static final String SHADER_ATTRIBUTE_POSITION = "a_Position";

    private int shaderUniformFlatColorLocation = 0;
    private int shaderUniformProjectionMatrix = 0;

    private int shaderAttributePositionLocation = 0;

    public static final int VERTEX_SHADER_RESOURCE = R.raw.line_vertex_shader;
    public static final int FRAGMENT_SHADER_RESOURCE = R.raw.line_fragment_shader;

    private Resources resources;

    private Shader shader;

    private boolean isInitialized = false;

    public LineShader(Resources resources) {
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
        shaderUniformProjectionMatrix = GLES20.glGetUniformLocation(programHandle, SHADER_UNIFORM_PROJECTION_MATRIX);
        shaderUniformFlatColorLocation = GLES20.glGetUniformLocation(programHandle, SHADER_UNIFORM_FLAT_COLOR);

        shaderAttributePositionLocation = GLES20.glGetAttribLocation(programHandle, SHADER_ATTRIBUTE_POSITION);

        isInitialized = true;
    }

    public void useProgram() {
        shader.useProgram();
    }

    public void setVertexAttributePointers(VertexBuffer positionBuffer) {
        setPositionVertexAttributePointers(positionBuffer);
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

    public LineShader setUniforms(float[] projectionMatrix, int color) {
        GLES20.glUniformMatrix4fv(shaderUniformProjectionMatrix, 1, false, projectionMatrix, 0);

        GLES20.glUniform4f(shaderUniformFlatColorLocation,
                Color.red(color),
                Color.green(color),
                Color.blue(color),
                Color.alpha(color));

        return this;
    }

    public LineShader setColorUniform(int color) {
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

    public void draw(float[] projectionMatrix, VertexBuffer positionBuffer, int color, int mode, int count) {

        setUniforms(projectionMatrix, color);
        setVertexAttributePointers(positionBuffer);

        GLES20.glDrawArrays(mode, 0, count);
    }

    public boolean isInitialized() {
        return isInitialized;
    }
}
