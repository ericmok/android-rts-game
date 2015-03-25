package noteworthyengine;

import org.json.JSONException;

import noteworthyframework.*;
import structure.Game;
import utils.Vector2;

/**
 * Created by eric on 3/6/15.
 *
 * Third attempt of an RTS engine
 */
public class NoteworthyEngine extends BaseEngine {

    public int frameNumber = 0;

    public Game game;

    public GridSystem gridSystem;
    public CommandSystem commandSystem;
    public TimelineSystem timelineSystem;
    public SeparationSystem separationSystem;
    public FieldSystem fieldSystem;
    //public FormationSystem formationSystem;
    public MovementSystem movementSystem;
    public BattleSystem battleSystem;
    public RenderSystem renderSystem;
    public DecaySystem decaySystem;

    public Vector2 gameCameraPosition = new Vector2();
    public double cameraScale = GameSettings.UNIT_LENGTH_MULTIPLIER;

    public NoteworthyEngine(Game game) {
        super();

        this.game = game;

        gridSystem = new GridSystem();
        commandSystem = new CommandSystem(game);
        timelineSystem = new TimelineSystem();
        separationSystem = new SeparationSystem(gridSystem);
        fieldSystem = new FieldSystem();
        //formationSystem = new FormationSystem();
        movementSystem = new MovementSystem();
        battleSystem = new BattleSystem(gridSystem);
        renderSystem = new RenderSystem(new DrawCompat(game));
        decaySystem = new DecaySystem();

        this.addSystem(gridSystem);
        this.addSystem(commandSystem);
        this.addSystem(timelineSystem);
        this.addSystem(fieldSystem);
        this.addSystem(separationSystem);
        //this.addSystem(formationSystem);
        this.addSystem(movementSystem);
        this.addSystem(battleSystem);
        this.addSystem(renderSystem);
        this.addSystem(decaySystem);
    }

    public void initialize() {
        super.initialize();
    }

    public void step(double dt) {
        super.step(dt);

        game.graphics.setCameraPositionAndScale((float)gameCameraPosition.x, (float)gameCameraPosition.y, (float)cameraScale);
        game.graphics.flushCameraModifications();
    }
}
