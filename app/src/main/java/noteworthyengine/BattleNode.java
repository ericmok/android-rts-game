package noteworthyengine;

import java.util.ArrayList;

import model.Entity;
import utils.Vector2;
import utils.VoidFunc;

/**
 * Created by eric on 3/6/15.
 */
public class BattleNode extends Node {

    public String[] dependencies = {
        "position",
        "velocity"
    };

    public String[] fields = {
        "startingHp",
        "hp",
        "armor",

        "isAttackable",

        "attackRange",
        "targetAcquisitionRange",

        "attackDamage",
        "attackSwingTime",
        "attackCooldown",

        "onAttack",
        "onDie",
        "onTargetAcquired",

        "onHpHit",
        "onArmorHit",

        "events"
    };

    public Entity entity;

    public Vector2 position;
    public Vector2 velocity;

    public double startingHp;
    public double hp;
    public double armor;

    public boolean isAttackable;

    public double attackRange;
    public double targetAcquisitionRange;

    public double attackDamage;
    public double attackSwingTime;
    public double attackCooldown;

    public VoidFunc<Void> onAttack;
    public VoidFunc<Void> onDie;
    public VoidFunc<Void> onTargetAcquired;

    public VoidFunc<Double> onHpHit;
    public VoidFunc<Double> onArmorHit;

    public ArrayList<String> events;

    public void revive() {}
    public void kill() {}


    public BattleNode(Entity entity, Vector2 position, Vector2 velocity) {
        this.entity = entity;

        this.position = position;
        this.velocity = velocity;

        //this.entity.publish(fields);
        this.entity.nodes.put("battleNode", this);
    }
}