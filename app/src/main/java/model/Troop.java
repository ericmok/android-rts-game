package model;

import behaviors.AcquiresTargetInRangeBehavior;
import behaviors.AttackTargetsInRangeBehavior;
import behaviors.Behaviors;
import behaviors.DiesBehavior;
import behaviors.LivingComponent;
import behaviors.MoveTowardsDestinationBehavior;
import behaviors.ProjectileCasterComponent;
import behaviors.RadiusComponent;
import behaviors.SelectionComponent;

/**
 * Created by eric on 11/9/14.
 */
public class Troop extends Entity {

    public Troop() {
        this.labels().add(Behaviors.UNIT_TROOP);
        this.labels().add(Behaviors.BEHAVIOR_DRAWN_AS_TROOP);

        RadiusComponent radiusComponent = new RadiusComponent();
        this.cData.put(RadiusComponent.class, radiusComponent);

        //this.labels().add(Behaviors.BEHAVIOR_AQUIRES_TARGET_IN_RANGE);

        AcquiresTargetInRangeBehavior.mixin(this);
        AttackTargetsInRangeBehavior.mixin(this);

        this.labels().add(Behaviors.BEHAVIOR_TAKES_DAMAGE_ON_COLLISION);
        //this.labels().add(Behaviors.BEHAVIOR_DIES_ON_NO_HP);
        //this.cData.put(MeleeAttackComponent.class, new MeleeAttackComponent());

        DiesBehavior.mixin(this);

        this.labels().add(Behaviors.BEHAVIOR_CASTS_PROJECTILE);
        this.cData.put(ProjectileCasterComponent.class, new ProjectileCasterComponent());

        this.labels().add(Behaviors.BEHAVIOR_GETS_SELECTED);
        SelectionComponent selectionComponent = new SelectionComponent();
        this.cData.put(SelectionComponent.class, selectionComponent);

        MoveTowardsDestinationBehavior.mixin(this);

        //AbilityComponent abilityComponent = new AbilityComponent();
        //abilityComponent.abilities.add(Abilities.SPECIAL_ATTACK);
        //this.cData.put(AbilityComponent.class, abilityComponent);

        LivingComponent livingComponent = (LivingComponent)this.cData.get(LivingComponent.class);
        livingComponent.hitPoints = 5;
        //LivingComponent livingComponent = new LivingComponent();
        //livingComponent.hitPoints = 5;
        //this.cData.put(LivingComponent.class, livingComponent);
    }
}
