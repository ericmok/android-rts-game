package noteworthyframework;

/**
 * Created by eric on 3/6/15.
 */
public abstract class Node {

    public Unit unit;
    public String _name = "Node";

    /// If this is active, then systems will process it, otherwise not
    public boolean isActive = true;

    public Node() {
    }

    public Node(Class nodeClass, Unit unit) {
        this.unit = unit;
        //this._name = name;
        unit.addNode(nodeClass, this);
    }

    private static boolean isPublishedField(java.lang.reflect.Field field) {
        boolean hasUnderscore = field.getName().charAt(0) == '_';
        boolean isStatic = java.lang.reflect.Modifier.isStatic(field.getModifiers());
        boolean isAllCaps = field.getName().toUpperCase().equals(field.getName());

        boolean isPublic = java.lang.reflect.Modifier.isPublic(field.getModifiers());

        return !hasUnderscore && !isStatic && !isAllCaps && isPublic;
    }

    public static void instantiatePublicFieldsForUnit(Unit unit, Class klass, Node node) {
        java.lang.reflect.Field[] fields = klass.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            try {

                java.lang.reflect.Field field = fields[i];

                if (!isPublishedField(field)) { continue; }

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
                    Object valueInsideDictionary = unit.field(field.getName());
                    field.set(node, valueInsideDictionary);
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
