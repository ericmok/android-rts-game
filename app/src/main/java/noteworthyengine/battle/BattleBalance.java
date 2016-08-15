package noteworthyengine.battle;

/**
 * Created by eric on 10/10/15.
 *
 * Balance Table inspired by Warcraft 3:
 * http://classic.battle.net/war3/basics/armorandweapontypes.shtml
 *
 * Starcraft 2's unit balance encourages unit designs that counter specific units rather
 * than general classes of units.
 *
 * Warcraft 3 Example:
 * 2 Pairs of Balanced Vectors
 * 1) NORMAL and SIEGE flip strengths and weaknesses (negation)
 * 2) PIERCING and MAGIC swap strengths but keep weaknesses (non-redundant)
 *
 public static final double[][] WARCARFT_3_EXAMPLE = {

 //            LIGHT,   MEDIUM,  HEAVY,   FORT

 new double[] {1.00,    1.50,    1.00,    0.70},  // NORMAL
 new double[] {2.00,    0.75,    1.00,    0.35},  // PIERCING
 new double[] {1.00,    0.50,    1.00,    1.50},  // SIEGE
 new double[] {1.25,    0.75,    2.00,    0.35},  // MAGIC
 };
 */
public class BattleBalance {

    public static final double[][] CHART = {

            //           NORM  EVAS  MECH  FORT  ENER

            new double[]{0.70, 1.25, 0.35, 1.00, 2.00},  // PHYSICAL
            new double[]{1.00, 0.75, 0.50, 1.00, 0.50},  // KINETIC
            new double[]{1.00, 1.25, 0.75, 0.75, 1.25},  // CHEMICAL
            new double[]{1.50, 1.25, 0.75, 2.00, 0.70},  // SIEGE
            new double[]{1.00, 0.75, 2.00, 0.75, 1.00},  // ENERGY
    };

    public static final int ATTACK_TYPE_PHYSICAL = 0;
    public static final int ATTACK_TYPE_KINETIC = 1;
    public static final int ATTACK_TYPE_CHEMICAL = 2;
    public static final int ATTACK_TYPE_SIEGE = 3;
    public static final int ATTACK_TYPE_ENERGY = 4;

    public static final int ARMOR_TYPE_NORMAL = 0;
    public static final int ARMOR_TYPE_EVASIVE = 1;
    public static final int ARMOR_TYPE_MECHANICAL = 2;
    public static final int ARMOR_TYPE_FORT = 3;
    public static final int ARMOR_TYPE_ENERGY = 4;

    public static double getDamageMultiplier(int attackType, int armorType) {
        return CHART[attackType][armorType];
    }
}
