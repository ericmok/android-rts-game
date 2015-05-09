package noteworthyengine;

import noteworthyframework.BaseEngine;
import noteworthyframework.DrawCompat;
import structure.Game;
import structure.GameCamera;

/**
 * Created by eric on 3/23/15.
 */
public class LoaderUIEngine extends BaseEngine {

    private Game game;

    public CameraSystem cameraSystem;
    public InputSystem inputSystem;
    public ButtonSystem buttonSystem;
    public RenderSystem renderSystem;

    public GameCamera mainCamera;

    public LoaderUIEngine(Game game) {
        this.game = game;

        cameraSystem = new CameraSystem(game);
        this.addSystem(cameraSystem);

        inputSystem = new InputSystem(game, cameraSystem);
        this.addSystem(inputSystem);

        buttonSystem = new ButtonSystem(game);
        this.addSystem(buttonSystem);

        renderSystem = new RenderSystem(new DrawCompat(game), cameraSystem);
        this.addSystem(renderSystem);

//        game.graphics.setCameraPositionAndScale(0, 0, 1);
//        game.graphics.flushCameraModifications();
    }

    @Override
    public void step(double dt) {
        super.step(dt);
    }
}
