package noteworthyengine;

/**
 * Created by eric on 10/10/15.
 * <p>
 * Balance Table inspired by Warcraft 3:
 * http://classic.battle.net/war3/basics/armorandweapontypes.shtml
 * <p>
 * Starcraft 2's unit balance encourages unit designs that counter specific units rather
 * than general classes of units.
 */
public class BattleBalance {

    public static final double[][] CHART = {

            //                LIGHT,   MEDIUM,  HEAVY,   FORT

            new double[]{1.00, 1.50, 1.00, 0.70},  // NORMAL
            new double[]{2.00, 0.75, 1.00, 0.35},  // PIERCING
            new double[]{1.00, 0.50, 1.00, 1.50},  // SIEGE
            new double[]{1.25, 0.75, 2.00, 0.35},  // MAGIC

    };

}
