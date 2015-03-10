package noteworthyengine;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Hashtable;

/**
 * Created by eric on 3/7/15.
 */
public abstract class System {

    public System() {
    }

    public void initialize(EngineDataPack engineDataPack) {}

    public abstract void addNode(Node node);
    public abstract void removeNode(Node node);

    public abstract void step(EngineDataPack engineDataPack, double ct, double dt);

    public abstract void flushQueues();

    //public abstract String serialize();
    //public abstract void diff(System other);
    //public abstract void deserialize(String string);
}
