package model;

import behaviors.AcquiresTargetInRangeBehavior;
import behaviors.AttackTargetsInRangeBehavior;
import behaviors.Behaviors;
import behaviors.CastingProjectileCooldownBehavior;
import behaviors.DiesBehavior;
import behaviors.GetsSelectedBehavior;
import behaviors.LivingComponent;
import behaviors.MoveTowardsDestinationBehavior;
import behaviors.ProjectileCasterComponent;
import behaviors.RadiusComponent;
import behaviors.SelectionComponent;
import behaviors.TakesDamageOnCollisionBehavior;

/**
 * Created by eric on 11/9/14.
 */
public class Troop extends Entity {

    public Troop() {
        this.labels().add(Behaviors.UNIT_TROOP);
        this.labels().add(Behaviors.BEHAVIOR_DRAWN_AS_TROOP);

        AcquiresTargetInRangeBehavior.mixin(this);
        AttackTargetsInRangeBehavior.mixin(this);
        TakesDamageOnCollisionBehavior.mixin(this);
        DiesBehavior.mixin(this);
        CastingProjectileCooldownBehavior.mixin(this);
        MoveTowardsDestinationBehavior.mixin(this);
        GetsSelectedBehavior.mixin(this);

        LivingComponent livingComponent = (LivingComponent)this.cData.get(LivingComponent.class);
        livingComponent.hitPoints = 5;

        //AbilityComponent abilityComponent = new AbilityComponent();
        //abilityComponent.abilities.add(Abilities.SPECIAL_ATTACK);
        //this.cData.put(AbilityComponent.class, abilityComponent);
    }
}
