package noteworthyengine;

/**
 * Created by eric on 3/7/15.
 */
public abstract class System {

    public abstract void addNode(Node node);
    public abstract void removeNode(Node node);

    public abstract void step(double ct, double dt);

    public abstract void flushQueues();

    //public abstract String serialize();
    //public abstract void diff(System other);
    //public abstract void deserialize(String string);
}
