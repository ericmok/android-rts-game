package components;

/**
 * Created by eric on 10/30/14.
 */
public class Entity {

    private static int NEXT_UNIQUE_ID = 1;
    private int id;

    public Entity() {
        id = NEXT_UNIQUE_ID;
        NEXT_UNIQUE_ID += 1;
    }


}
