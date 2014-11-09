package model;

import java.util.ArrayList;

/**
 * Created by eric on 11/3/14.
 */
public class AbilityComponent extends Component {
    public ArrayList<String> abilities = new ArrayList<String>(8);

    public String currentAbility = Abilities.NONE;
}
