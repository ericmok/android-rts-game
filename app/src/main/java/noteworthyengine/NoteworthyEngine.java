package noteworthyengine;

import java.util.ArrayList;

/**
 * Created by eric on 3/6/15.
 *
 * Third attempt of an RTS engine
 */
public class NoteworthyEngine {

    public int frameNumber = 0;

    ArrayList<System> systems = new ArrayList<System>(16);

    public NoteworthyEngine() {
        //systems.add(new RenderSystem());
    }

    /**
     * Takes game commands
     */
    public void exec(EngineDataPack engineDataPack) {

    }

    public void step(EngineDataPack engineDataPack, double ct, double dt) {

        engineDataPack.addQueuedUnits();

        engineDataPack.removeQueuedUnits();

        frameNumber += 1;
    }
}
