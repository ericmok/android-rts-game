package structure;

import android.opengl.GLES20;

public class Shader {

    private String vertexShaderSource;
    private String fragmentShaderSource;

    private int vertexShaderHandle = 0;
    private int fragmentShaderHandle = 0;
    private int programHandle = 0;

    public Shader(String vertexSource, String fragmentSource) {
        this.vertexShaderSource = vertexSource;
        this.fragmentShaderSource = fragmentSource;
    }

    public int compile() throws ShaderCreationException, ShaderCompilationException {
        // Load in the vertex shader.
        vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

        if (vertexShaderHandle != 0) {
            // Pass in the shader source.
            GLES20.glShaderSource(vertexShaderHandle, vertexShaderSource);

            // Compile the shader.
            GLES20.glCompileShader(vertexShaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(vertexShaderHandle);
                vertexShaderHandle = 0;
                throw new ShaderCompilationException("Error compiling vertex shader.");
            }
        }

        if (vertexShaderHandle == 0) {
            throw new ShaderCreationException("Error creating vertex shader.");
        }

        // Load in the fragment shader shader.
        fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        if (fragmentShaderHandle != 0) {
            // Pass in the shader source.
            GLES20.glShaderSource(fragmentShaderHandle, fragmentShaderSource);

            // Compile the shader.
            GLES20.glCompileShader(fragmentShaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(fragmentShaderHandle);
                fragmentShaderHandle = 0;
                throw new ShaderCompilationException("Error compiling fragment shader.");
            }
        }

        if (fragmentShaderHandle == 0) {
            throw new ShaderCreationException("Error creating fragment shader.");
        }


        // Create a program object and store the handle to it.
        this.programHandle = GLES20.glCreateProgram();

        if (programHandle != 0) {
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
            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0) {
            throw new ShaderCompilationException("Error creating program.");
        }

        return programHandle;
    }

    public int useProgram() {
        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(programHandle);
        return programHandle;
    }

    public int glHandle() {
        return programHandle;
    }

    public static class ShaderCreationException extends Exception {
        public ShaderCreationException(String msg) {
            super(msg);
        }
    }

    public static class ShaderCompilationException extends Exception {
        public ShaderCompilationException(String msg) {
            super(msg);
        }
    }
}
