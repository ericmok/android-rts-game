package noteworthyengine;

import java.util.ArrayList;

/**
 * Created by eric on 3/7/15.
 */
public class RenderSystem extends System {
    public EngineDataPack engineDataPack;
    public DrawCompat drawCompat;

    public RenderSystem(EngineDataPack engineDataPack, DrawCompat drawCompat) {
        this.engineDataPack = engineDataPack;
        this.drawCompat = drawCompat;
    }

    public void step(double ct, double dt) {

//        ArrayList<Unit> unitsToDraw = engineDataPack.unitsByNodes.getListForLabel(RenderNode.class);
//
//        for (int i = 0; i < unitsToDraw.size(); i++) {
//            Unit unit = unitsToDraw.get(i);
//            RenderNode renderNode = (RenderNode)unit.node("renderNode");
//        }
    }
}
