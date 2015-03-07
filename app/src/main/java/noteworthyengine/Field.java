package noteworthyengine;

/**
 * Created by eric on 3/7/15.
 */
public class Field {
    public String name;
    public Class type;
    public Object value;

    public Field(String name, Class type, Object obj) {
        this.name = name;
        this.type = type;
        this.value = obj;
    }
}
