package behaviors;

import utils.Vector2;

/**
 * Created by eric on 11/3/14.
 */
public class ButtonComponent extends Component {
    public String name;
    public int position = 0;
    public String texture;

    public Vector2 touchPoint = new Vector2();

    public Vector2 size = new Vector2(1, 1);

    public ButtonComponent(String name) {
        this.name = name;
    }
}
