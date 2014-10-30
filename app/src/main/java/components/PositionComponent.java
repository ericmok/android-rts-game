package components;

/**
 * Created by eric on 10/30/14.
 */
public class PositionComponent extends Component{
    public double x = 0;
    public double y = 0;

    public PositionComponent set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }
}
