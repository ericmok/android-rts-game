package noteworthyengine;

/**
 * Created by eric on 3/6/15.
 */
public class Node {

    public static final Field[] NO_FIELDS = {};

    public Unit unit;

    public Node() {
    }

    public Node(Unit unit) {
        this.unit = unit;
    }

    public static void instantiatePublicFieldsForUnit(Unit unit, Class klass, Node node) {
        java.lang.reflect.Field[] fields = klass.getFields();
        for (int i = 0; i < fields.length; i++) {
            try {

                java.lang.reflect.Field field = fields[i];

                if (unit.field(field.getName()) == null) {
                    if (field.get(node) == null) {
                        Object instantiation = field.getType().newInstance();
                        field.set(node, instantiation);
                        unit.fields.put(field.getName(), instantiation);
                    } else {
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
