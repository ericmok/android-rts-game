package noteworthyengine;

/**
 * Created by eric on 3/6/15.
 *
 * Third attempt of an RTS engine
 */
public class NoteworthyEngine {

    public int frameNumber = 0;

    public NoteworthyEngine() {
    }

    /**
     * Takes game commands
     */
    public void exec(EngineDataPack engineDataPack) {

    }

    public void step(EngineDataPack engineDataPack, double ct, double dt) {

        engineDataPack.eachGamerAddQueuedUnits();

        engineDataPack.eachGanerRemoveQueuedUnits();

        frameNumber += 1;
    }
}
