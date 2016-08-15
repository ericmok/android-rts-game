package noteworthyengine.battle;

/**
 * Created by eric on 2/5/16.
 */
public interface BattleBuff {
    public double buffAttackRange(double in);

    public double buffAttackDamage(double in);
    public double buffAttackSwingTime(double in);
    public double buffAttackCooldown(double in);
    public double buffArmorAmount(double in);

    public int buffIsAttackable(int in);
    public double buffHp(double in);

    public double buffMaxSpeed(double in);
}
