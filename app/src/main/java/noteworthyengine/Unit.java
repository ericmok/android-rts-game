package noteworthyengine;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import model.Denormalizable;

/**
 * Created by eric on 3/6/15.
 */
public class Unit {
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
}
