package noteworthyengine;

/**
 * Created by eric on 3/6/15.
 */
public abstract class Node {

    public Unit unit;
    public String _name = "Node";

    public Node() {
    }

    public Node(String name, Unit unit) {
        this.unit = unit;
        this._name = name;
        unit.addNode(this._name, this);
    }

    private static boolean isNotPublishedField(java.lang.reflect.Field field) {
        return field.getName().charAt(0) == '_';
    }

    public static void instantiatePublicFieldsForUnit(Unit unit, Class klass, Node node) {
        java.lang.reflect.Field[] fields = klass.getFields();
        for (int i = 0; i < fields.length; i++) {
            try {

                java.lang.reflect.Field field = fields[i];

                if (isNotPublishedField(field)) { continue; }

                if (unit.field(field.getName()) == null) {
                    // If node's dictionary doesn't have it, add it

                    if (field.get(node) == null) {
                        // If public instance variable is not defined, create new instance

                        Object instantiation = field.getType().newInstance();
                        field.set(node, instantiation);
                        unit.fields.put(field.getName(), instantiation);

                    } else {
                        // If public instance variable has value, copy it to dictionary
                        unit.fields.put(field.getName(), field.get(node));
                    }
                }
                else {
                    // If node already has the field in its dictionary, lets set the field
                    field.set(node, unit.field(field.getName()));
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

//    public Field[] getFields() {
//        return NO_FIELDS;
//    }
//
//    public void mixin() {
//        Field[] dependencies = this.getFields();
//
//        for (int i = 0; i < dependencies.length; i++) {
//            if (!unit.hasField(dependencies[i].name)) {
//                try {
//                    unit.fields.put(dependencies[i].name, (Object)dependencies[i].type.newInstance());
//                } catch (InstantiationException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}
