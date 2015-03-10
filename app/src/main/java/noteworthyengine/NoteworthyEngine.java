package noteworthyengine;

import java.util.ArrayList;

/**
 * Created by eric on 3/6/15.
 *
 * Third attempt of an RTS engine
 */
public class NoteworthyEngine {

    public int frameNumber = 0;
    public EngineDataPack engineDataPack;

    public NoteworthyEngine() {
    }

    public void initialize(EngineDataPack engineDataPack) {
        for (int i = 0; i < engineDataPack.systems.size(); i++) {
            System system = engineDataPack.systems.get(i);
            system.initialize(engineDataPack);
            system.flushQueues();
        }
    }

    /**
     * Takes game commands
     */
    public void exec(EngineDataPack engineDataPack) {

    }

    public void step(EngineDataPack engineDataPack, double ct, double dt) {

        this.engineDataPack = engineDataPack;
        //engineDataPack.addQueuedUnits();

        //movementSystem.step(engineDataPack.movementNodes, ct, dt);

        for (int i = 0; i < engineDataPack.systems.size(); i++) {
            System system = engineDataPack.systems.get(i);

            system.flushQueues();
            system.step(engineDataPack, ct, dt);
        }

        //engineDataPack.removeQueuedUnits();

        engineDataPack.flushQueues();

        frameNumber += 1;
    }
}
