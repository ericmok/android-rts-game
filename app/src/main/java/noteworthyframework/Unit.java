package noteworthyframework;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import utils.JsonSerializable;

/**
 * Created by eric on 3/6/15.
 */
public class Unit implements JsonSerializable {
    public Hashtable<String, Object> fields = new Hashtable<String, Object>(64);

    private Hashtable<String, Node> nodes = new Hashtable<String, Node>(32);
    public ArrayList<Node> nodeList = new ArrayList<Node>(32);

    /// For storing the unit in a group with same labels
    //public ArrayList<Integer> labels = new ArrayList<Integer>();

    public String name = "Unit";

    public Unit() {
    }

    public Object field(String key) { return fields.get(key); }
    public Node node(String key) {
        return nodes.get(key);
    }

    public void addNode(String nodeName, Node node) {
        node._name = nodeName; // safeguard?
        nodes.put(nodeName, node);
        nodeList.add(node);
    }

    public void addFieldIfNotExist(String name, Object field) {
        if (this.fields.get(name) == null) {
            this.fields.put(name, field);
        }
    }

    public boolean hasField(String name) {
        return this.fields.get(name) != null;
    }

    //@Override
    //public ArrayList<Integer> labels() {
    //    return labels;
    //}

    public String json() {
        StringBuilder sb = new StringBuilder();

        Enumeration<String> keys = this.fields.keys();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();

            // Skip event handlers
            if (key.startsWith("on")) {
                continue;
            }

            sb.append(key + ":");
            Object field = this.fields.get(key);

            sb.append(((JsonSerializable)field).json());

            if (keys.hasMoreElements()) {
                sb.append(",\n");
            }
        }

        return sb.toString();
    }

    public void step(BaseEngine engine, double dt) {
    }
}
