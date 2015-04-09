package noteworthyengine.units;

import android.graphics.Color;

import art.Animations;
import noteworthyengine.BattleNode;
import noteworthyengine.FieldNode;
import noteworthyengine.FieldSystem;
import noteworthyengine.FormationNode;
import noteworthyengine.GridNode;
import noteworthyengine.MovementNode;
import noteworthyengine.RenderNode;
import noteworthyengine.RenderSystem;
import noteworthyengine.SeparationNode;
import noteworthyframework.Gamer;
import noteworthyframework.Unit;
import structure.Sprite2dDef;
import utils.Vector2;
import utils.VoidFunc;

/**
 * Created by eric on 4/7/15.
 */
public class PlatoonLeader extends Unit {

    public GridNode gridNode;
    public SeparationNode separationNode = new SeparationNode(this);
    public MovementNode movementNode = new MovementNode(this);
    public FieldNode fieldNode = new FieldNode(this);
    public FieldNode.FieldAgentNode fieldAgentNode = new FieldNode.FieldAgentNode(this);
    public BattleNode battleNode = new BattleNode(this);
    public RenderNode renderNode = new RenderNode(this);
    //public FormationNode.FormationSheep formationSheep = new FormationNode.FormationSheep(this);
    public FormationNode.FormationLeader formationLeader = new FormationNode.FormationLeader(this);

    private static Vector2 temp = new Vector2();

    public PlatoonLeader() {
        super();

        fieldNode._fieldAgentNode = fieldAgentNode;
        //this.battleNode.hp.v = 1000;

        gridNode = new GridNode(this, separationNode, battleNode);

        battleNode.hp.v = 100;

        this.renderNode.color.v = Color.WHITE;
        this.renderNode.animationName.v = Animations.ANIMATION_CAPITAL_SHIPS_IDLING;
        this.renderNode.width.v = 1f;
        this.renderNode.height.v = 1f;
        this.renderNode.onDraw = new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem system) {
                renderNode.color.v = Gamer.colorForTeam(battleNode.gamer.v.team);
//
//                for (int i = 0; i < formationLeader.freeIndices.size(); i++) {
//                    formationLeader.calculateSheepPosition(temp, formationLeader.freeIndices.get(i));
//
//                    Sprite2dDef sprite = system.drawCompat.spriteAllocator.takeNextWritable();
//                    sprite.set(Animations.ANIMATION_TROOPS_SELECTED, 0, (float) temp.x, (float) temp.y, 0,
//                            1.2f, 1.2f, 0f, Color.WHITE, 0);
//                }
                for (int i = 0; i < formationLeader.sheeps.size(); i++) {
                    Sprite2dDef sprite = system.drawCompat.spriteAllocator.takeNextWritable();
                    sprite.set(Animations.ANIMATION_RETICLE_TAP, 0, (float)formationLeader.sheeps.get(i).coords.pos.x, (float) formationLeader.sheeps.get(i).coords.pos.y, 0,
                            1.2f, 1.2f, 0f, Color.argb(50, 255, 255, 255), 0);
                }
            }
        };
    }
}
