package noteworthyengine.screens;

import noteworthyengine.InputSystem;
import noteworthyengine.RenderSystem;
import noteworthyframework.BaseEngine;
import structure.Game;
import structure.GameCamera;

/**
 * Created by eric on 5/4/15.
 */
public class LevelSelector extends BaseEngine {

    public final Game game;

    public final GameCamera gameCamera;

    public InputSystem inputSystem;
    public RenderSystem renderSystem;

    public LevelSelector(Game game) {
        this.game = game;
        this.gameCamera = this.game.getGameRenderer().registerNewOrthoCamera();
    }

    @Override
    public void initialize() {
        super.initialize();
    }
}
