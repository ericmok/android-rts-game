package model;

/**
 * Created by eric on 11/7/14.
 */
public class LivingComponent extends Component {
    public int hitPoints = 10;

    public void takeDamage(int hits) {
        hitPoints = hitPoints - hits;
    }
}
