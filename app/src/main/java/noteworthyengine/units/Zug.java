package noteworthyengine.units;

import art.Animations;
import noteworthyengine.BattleNode;
import noteworthyengine.FieldNode;
import noteworthyengine.FormationNode;
import noteworthyengine.GridNode;
import noteworthyengine.MovementNode;
import noteworthyengine.RenderNode;
import noteworthyengine.RenderSystem;
import noteworthyengine.SelectionNode;
import noteworthyengine.SeparationNode;
import noteworthyframework.Gamer;
import noteworthyframework.Unit;
import structure.Sprite2dDef;
import structure.TemporarySprite2dDef;
import utils.Orientation;
import utils.VoidFunc;

/**
 * Created by eric on 10/26/15.
 */
public class Zug extends Unit {

    public static final String NAME = "Zug";

    public GridNode gridNode;
    public MovementNode movementNode;

    public FieldNode fieldNode;
    public BattleNode battleNode;
    public SeparationNode separationNode;
    public FormationNode formationNode;
    public SelectionNode selectionNode;

    public RenderNode renderNode;

    public Zug() {
        super();
        this.name = NAME;

        movementNode = new MovementNode(this);

        fieldNode = FieldNode.createAgentFieldNode(this);

        renderNode = new RenderNode(this);

        separationNode = new SeparationNode(this);
        formationNode = new FormationNode(this);
        battleNode = new BattleNode(this);

        gridNode = new GridNode(this, separationNode, battleNode);
        selectionNode = new SelectionNode(this);

        this.renderNode.onDraw = new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem system) {
                //renderNode.color.v = Gamer.TeamColors.get(battleNode.gamer.v.team) & 0xFF9999FF;
                renderNode.color.v = Gamer.TeamColors.get(battleNode.gamer.v.team);

                if (!battleNode.isAlive()) {
                    TemporarySprite2dDef tempSprite = system.beginNewTempSprite();

                    tempSprite.copy(Animations.ANIMATION_TROOPS_DYING_DEF);
                    tempSprite.setPosition((float) battleNode.coords.pos.x, (float) battleNode.coords.pos.y, 1);

                    system.endNewTempSprite(tempSprite, 0);
                }

                if (battleNode.battleState.v == BattleNode.BATTLE_STATE_SWINGING) {

                    if (battleNode.target.v != null) {
                        double ratio = (battleNode.battleProgress.v / battleNode.attackSwingTime.v);

                        Sprite2dDef sprite2dDef = system.defineNewSprite(
                                "Animations/Troops/Sword", 0,
                                (float) (ratio * (battleNode.target.v.coords.pos.x - battleNode.coords.pos.x) + battleNode.coords.pos.x),
                                (float) (ratio * (battleNode.target.v.coords.pos.y - battleNode.coords.pos.y) + battleNode.coords.pos.y),
                                1f,
                                0.55f, 0.55f,
                                (float) Orientation.getDegreesBaseX(battleNode.target.v.coords.pos.x - battleNode.coords.pos.x,
                                        battleNode.target.v.coords.pos.y - battleNode.coords.pos.y),
                                renderNode.color.v,
                                RenderNode.RENDER_LAYER_FOREGROUND);
                    }
                }
            }
        };
    }

    public void configure(final Gamer gamer) {
        this.movementNode.reset();
        this.battleNode.reset();

        this.renderNode.width.v = 1.2f;
        this.renderNode.height.v = 1.2f;

        this.battleNode.gamer.v = gamer;
        this.battleNode.hp.v = 24;
        this.battleNode.maxSpeed.v = 1.1;
        this.battleNode.attackDamage.v = 25;
        this.battleNode.attackRange.v = 1;
        this.battleNode.fractionToWalkIntoAttackRange.v = 0.4;
        this.battleNode.targetAcquisitionRange.v = 17;
        this.battleNode.attackSwingTime.v = 1;
        this.battleNode.attackCooldown.v = 2;
        this.battleNode.battleState.v = BattleNode.BATTLE_STATE_IDLE;

        this.renderNode.animationName.v = Animations.ANIMATION_ZUG_IDLING;
    }
}
