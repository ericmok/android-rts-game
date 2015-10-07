package noteworthyengine;

import noteworthyengine.events.GameEvents;
import noteworthyengine.units.ArrowCommandInput;
import noteworthyengine.units.CameraUnit;
import noteworthyengine.units.MainGameCamera;
import noteworthyengine.units.MoveInputModifier;
import noteworthyengine.units.UnitPool;
import noteworthyframework.*;
import structure.Game;
import structure.OrthographicCamera;

/**
 * Created by eric on 3/6/15.
 *
 * Third attempt of an RTS engine
 */
public class NoteworthyEngine extends BaseEngine {

    public int frameNumber = 0;

    public Game game;

    public InputSystem inputSystem;
    public CameraSystem cameraSystem;
    public FieldCameraSystem fieldCameraSystem;

    public GridSystem gridSystem;
    //public CommandSystem commandSystem;
    public TimelineSystem timelineSystem;
    public SeparationSystem separationSystem;
    public FieldSystem fieldSystem;
    public FormationSystem formationSystem;
    public MovementSystem movementSystem;
    public BattleSystem battleSystem;
    public RenderSystem renderSystem;
    public DecaySystem decaySystem;

    public FactorySystem factorySystem;

    public OrthographicCamera mainCamera;

    public NoteworthyEngine(Game game) {
        super();

        this.game = game;

        cameraSystem = new CameraSystem(game);
        inputSystem = new InputSystem(game, cameraSystem);

        gridSystem = new GridSystem();
        //commandSystem = new CommandSystem(game);
        timelineSystem = new TimelineSystem();
        separationSystem = new SeparationSystem(gridSystem);
        fieldSystem = new FieldSystem();
        fieldCameraSystem = new FieldCameraSystem(gridSystem, fieldSystem);
        formationSystem = new FormationSystem(gridSystem);
        movementSystem = new MovementSystem();
        battleSystem = new BattleSystem(gridSystem);
        renderSystem = new RenderSystem(new DrawCompat(game), cameraSystem);
        decaySystem = new DecaySystem();

        factorySystem = new FactorySystem();

        this.addSystem(inputSystem);
        this.addSystem(cameraSystem);

        this.addSystem(gridSystem);
        //this.addSystem(commandSystem);
        this.addSystem(timelineSystem);
        this.addSystem(fieldSystem);
        this.addSystem(fieldCameraSystem);

        this.addSystem(separationSystem);
        this.addSystem(formationSystem);
        this.addSystem(movementSystem);
        this.addSystem(battleSystem);
        this.addSystem(renderSystem);
        this.addSystem(decaySystem);

        this.addSystem(factorySystem);

        UnitPool.load();
    }

    public void initialize() {
        super.initialize();

        MainGameCamera activeGameCamera = new MainGameCamera(0.068f, 0.081f);
        CameraUnit auxGameCamera = new CameraUnit(1);
        this.addUnit(activeGameCamera);
        this.addUnit(auxGameCamera);

        ArrowCommandInput arrowCommandInput = new ArrowCommandInput(game, this);
        this.addUnit(arrowCommandInput);

        this.addUnit(new MoveInputModifier(game, this));
    }

    @Override
    public void recycleUnit(Unit unit) {
        UnitPool.recycle(unit);
    }

    public void step(double dt) {
        super.step(dt);

        //game.graphics.setCameraPositionAndScale((float) gameCameraPosition.x, (float) gameCameraPosition.y, (float) cameraScale);
        //game.graphics.flushCameraModifications();
    }
}
