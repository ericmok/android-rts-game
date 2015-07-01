package structure;

import android.graphics.Color;
import android.opengl.GLES20;

import utils.Orientation;

/**
 * Created by eric on 6/30/15.
 */
public class LineBatch {

    private VertexBuffer reusableLine;

    private float[] lineBuffer = {
            0, 0, 0,
            1, 0, 0,
    };

    private SimpleQuadShader simpleQuadShader;

    public LineBatch(SimpleQuadShader simpleQuadShader) {
        this.simpleQuadShader = simpleQuadShader;
    }

    public void load() {
        reusableLine = new VertexBuffer(3);
        reusableLine.initialize();
        reusableLine.bufferData(lineBuffer);
    }

    public void beginDrawing(int glTexture) {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, glTexture);
        //reusableLine.bufferData(lineBuffer);
        simpleQuadShader.setVertexAttributePointers(reusableLine, reusableLine);
    }

    public void draw(float[] projectionMatrix, float x, float y, float z, float x1, float y1, float z1, int lineWidth, int color) {
        lineBuffer[0] = x;
        lineBuffer[1] = y;
        lineBuffer[2] = z;
        lineBuffer[3] = x1;
        lineBuffer[4] = y1;
        lineBuffer[5] = z1;

        reusableLine.bufferData(lineBuffer);

        GLES20.glLineWidth(lineWidth);

//        float mag = (float)Math.sqrt(x*x + y*y + z*z);
//        simpleQuadShader.setUniforms(projectionMatrix,
//                mag, mag,
//                (float)Orientation.getDegreesBaseX(x1 - x, y1 - y),
//                x, y, color);

        simpleQuadShader.setUniforms(projectionMatrix, 1, 1, 0, 0, 0, color);
        simpleQuadShader.drawArrays(GLES20.GL_LINES, 2);
    }

    public void endDrawing() {
    }
}
