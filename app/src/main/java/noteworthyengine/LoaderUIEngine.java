package noteworthyengine;

import noteworthyframework.BaseEngine;
import noteworthyframework.DrawCompat;
import structure.Game;

/**
 * Created by eric on 3/23/15.
 */
public class LoaderUIEngine extends BaseEngine {

    private Game game;

    public ButtonSystem buttonSystem;
    public RenderSystem renderSystem;

    public LoaderUIEngine(Game game) {
        this.game = game;

        buttonSystem = new ButtonSystem(game);
        this.addSystem(buttonSystem);

        renderSystem = new RenderSystem(new DrawCompat(game));
        this.addSystem(renderSystem);

        game.graphics.setCameraPositionAndScale(0, 0, 1);
        game.graphics.flushCameraModifications();
    }

    @Override
    public void step(double dt) {
        super.step(dt);
    }
}
