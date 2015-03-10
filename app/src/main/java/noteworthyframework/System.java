package noteworthyframework;

/**
 * Created by eric on 3/7/15.
 */
public abstract class System<N> {

    private BaseEngine baseEngine;

    public System() {
    }

    public void setBaseEngine(BaseEngine baseEngine) {
        this.baseEngine = baseEngine;
    }

    public BaseEngine getBaseEngine() {
        return baseEngine;
    }

    public void initialize() {}

    public abstract void addNode(Node node);
    public abstract void removeNode(Node node);

    public abstract void step(double ct, double dt);

    public abstract void flushQueues();

    //public abstract String serialize();
    //public abstract void diff(System other);
    //public abstract void deserialize(String string);
}
