package structure;

import android.graphics.Color;
import android.opengl.GLES20;

import utils.Orientation;

/**
 * Created by eric on 6/30/15.
 */
public class LineBatch {

    private VertexBuffer reusableLine;

//    private float[] lineBuffer = {
//            0, 0, 0,
//            1, 0, 0,
//    };

    private float[] lineBuffer = new float[1024];
    private int lineBufferSize = 0;

    private Quad reusableQuad;

    private SimpleQuadShader simpleQuadShader;

    public LineBatch(SimpleQuadShader simpleQuadShader) {
        this.simpleQuadShader = simpleQuadShader;
    }

    public void load() {
        reusableLine = new VertexBuffer(3);
        reusableLine.initialize();
        reusableLine.bufferData(lineBuffer);

        reusableQuad = new Quad(0, 0, 1f, 0.1f);
        reusableQuad.initializeBuffers();
    }

    public void beginDrawing(int glTexture) {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, glTexture);
        //reusableLine.bufferData(lineBuffer);
        simpleQuadShader.setVertexAttributePointers(reusableLine, reusableLine);

        lineBufferSize = 0;
    }

    public void draw(float[] projectionMatrix, float x, float y, float z, float x1, float y1, float z1, int lineWidth, int color) {
//        lineBuffer[0] = x;
//        lineBuffer[1] = y;
//        lineBuffer[2] = z;
//        lineBuffer[3] = x1;
//        lineBuffer[4] = y1;
//        lineBuffer[5] = z1;
//
//        reusableLine.bufferData(lineBuffer);
//
//        GLES20.glLineWidth(lineWidth);
//
//        simpleQuadShader.setUniforms(projectionMatrix, 1, 1, 0, 0, 0, color);
//        simpleQuadShader.drawArrays(GLES20.GL_LINES, 2);

//        lineBuffer[lineBufferSize + 1] = x;
//        lineBuffer[lineBufferSize + 2] = y;
//        lineBuffer[lineBufferSize + 3] = z;
//        lineBuffer[lineBufferSize + 4] = x1;
//        lineBuffer[lineBufferSize + 5] = y1;
//        lineBuffer[lineBufferSize + 6] = z1;
//
//        lineBufferSize += 6;

//        reusableLine.bufferData(lineBuffer);
//
//        GLES20.glLineWidth(lineWidth);
//
//        simpleQuadShader.setUniforms(projectionMatrix, 1, 1, 0, 0, 0, color);
//        simpleQuadShader.drawArrays(GLES20.GL_LINES, 2);


        float mag = (float)Math.sqrt(x*x + y*y + z*z);
        simpleQuadShader.setUniforms(projectionMatrix,
                mag, mag,
                (float)Orientation.getDegreesBaseX(x1 - x, y1 - y),
                x, y, color);

        simpleQuadShader.setPositionVertexAttributePointers(reusableQuad.vertexBuffer.getGLHandle());

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    public void endDrawing() {
    }
}
