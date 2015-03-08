package noteworthyengine;

import java.util.ArrayList;

/**
 * Created by eric on 3/6/15.
 *
 * Third attempt of an RTS engine
 */
public class NoteworthyEngine {

    public int frameNumber = 0;

    public MovementSystem movementSystem = new MovementSystem();

    public NoteworthyEngine() {
        //systems.add(new RenderSystem());
    }

    /**
     * Takes game commands
     */
    public void exec(EngineDataPack engineDataPack) {

    }

    public void step(EngineDataPack engineDataPack, double ct, double dt) {

        //engineDataPack.addQueuedUnits();

        //movementSystem.step(engineDataPack.movementNodes, ct, dt);

        for (int i = 0; i < engineDataPack.systems.size(); i++) {
            System system = engineDataPack.systems.get(i);

            system.flushQueues();
            system.step(ct, dt);
        }

        //engineDataPack.removeQueuedUnits();

        frameNumber += 1;
    }
}
