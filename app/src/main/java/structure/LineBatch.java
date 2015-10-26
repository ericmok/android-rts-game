package structure;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

/**
 * Created by eric on 6/30/15.
 */
public class LineBatch {

    private VertexBuffer reusableLineVB;

    private float[] lineBuffer = {
            0, 0, 0, 1, 1, 1
    };

    // TODO: Sort lines by projection matrices and batch drawArrays
    //private float[] lineBuffer = new float[2048];

    //private Quad reusableQuad;

    private LineShader lineShader;

    public LineBatch(LineShader lineShader) {
        this.lineShader = lineShader;
    }

    public void load() {
        // Allocate a buffer and point to it
        reusableLineVB = new VertexBuffer(3);
        reusableLineVB.initialize();
        reusableLineVB.bufferData(lineBuffer);

        lineShader.setVertexAttributePointers(reusableLineVB);

        //reusableQuad = new Quad(0, 0, 1f, 0.1f);
        //reusableQuad.initializeBuffers();
    }

    public void beginDrawing() {
        lineShader.setPositionVertexAttributePointers(reusableLineVB.getGLHandle());
        lineShader.useProgram();
    }

    public void draw(float[] projectionMatrix, float x, float y, float z, float x1, float y1, float z1, int lineWidth, int color) {

        lineBuffer[0] = x;
        lineBuffer[1] = y;
        lineBuffer[2] = z;
        lineBuffer[3] = x1;
        lineBuffer[4] = y1;
        lineBuffer[5] = z1;

        FloatBuffer internalBuffer = reusableLineVB.getFloatBuffer();
        internalBuffer.put(lineBuffer, 0, 6);
        internalBuffer.flip();

        //reusableLineVB.bufferSubData(reusableLineVB.getFloatBuffer());
        reusableLineVB.bufferSubData(reusableLineVB.getFloatBuffer(), 6);
        //reusableLineVB.bufferSubData(lineBuffer, lineBufferSize);

        lineShader.setUniforms(projectionMatrix, color);

        GLES20.glLineWidth(lineWidth);
        //GLES20.glDrawArrays(GLES20.GL_LINES, 0, lineBufferSize / 6);
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, 2);

//        lineBuffer[0] = x;
//        lineBuffer[1] = y;
//        lineBuffer[2] = z;
//        lineBuffer[3] = x1;
//        lineBuffer[4] = y1;
//        lineBuffer[5] = z1;
//
//        reusableLineVB.bufferSubData(lineBuffer);
//
//        GLES20.glLineWidth(lineWidth);
//
//        lineShader.setUniforms(projectionMatrix, 1, 1, 0, 0, 0, color);
//        lineShader.drawArrays(GLES20.GL_LINES, 2);

//        lineBuffer[lineBufferSize + 1] = x;
//        lineBuffer[lineBufferSize + 2] = y;
//        lineBuffer[lineBufferSize + 3] = z;
//        lineBuffer[lineBufferSize + 4] = x1;
//        lineBuffer[lineBufferSize + 5] = y1;
//        lineBuffer[lineBufferSize + 6] = z1;
//
//        lineBufferSize += 6;

//        reusableLineVB.bufferSubData(lineBuffer);
//
//        GLES20.glLineWidth(lineWidth);
//
//        lineShader.setUniforms(projectionMatrix, 1, 1, 0, 0, 0, color);
//        lineShader.drawArrays(GLES20.GL_LINES, 2);

        // For quad drawing
//        float mag = (float)Math.sqrt(x*x + y*y + z*z);
//        lineShader.setUniforms(projectionMatrix,
//                mag, mag,
//                (float)Orientation.getDegreesBaseX(x1 - x, y1 - y),
//                x, y, color);
//
//        lineShader.setPositionVertexAttributePointers(reusableQuad.vertexBuffer.getGLHandle());
//        GLES20.glLineWidth(1);
//        lineShader.setUniforms(projectionMatrix, Color.WHITE);
//
//        GLES20.glDrawArrays(GLES20.GL_LINES, 0, 2);
    }

    public void endDrawing() {

        // For batching drawing, but you'd have to sort by projection matrices first...
//        reusableLineVB.bufferSubData(lineBuffer, lineBufferSize);
//
//        GLES20.glLineWidth(1);
//        if (projectionMatrix != null) {
//            lineShader.setUniforms(projectionMatrix, Color.WHITE);
//        }
//
//        GLES20.glDrawArrays(GLES20.GL_LINES, 0, 4);
    }
}
