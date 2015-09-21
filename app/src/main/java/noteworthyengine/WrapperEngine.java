package noteworthyengine;

import android.graphics.Color;

import org.json.JSONException;

import art.Animations;
import noteworthyengine.events.GameEvents;
import noteworthyengine.levels.LevelOne;
import noteworthyengine.levels.LevelTwo;
import noteworthyengine.units.ButtonUnit;
import noteworthyengine.units.CameraUnit;
import noteworthyframework.BaseEngine;
import structure.Game;

/**
 * Created by eric on 5/15/15.
 */
public class WrapperEngine extends BaseEngine {

    public enum State {
        IN_MENU,
        LOADING,
        IN_GAME
    };

    public LoaderUIEngine loaderUIEngine;
    public NoteworthyEngine noteworthyEngine;

    private Game game;
    private State state = State.IN_MENU;

    public WrapperEngine(Game game) {
        this.game = game;

        this.loaderUIEngine = new LoaderUIEngine(game);
        this.noteworthyEngine = new NoteworthyEngine(game);
    }

    @Override
    public void initialize() {
        super.initialize();

        loaderUIEngine.initialize();
        noteworthyEngine.initialize();

        CameraUnit loaderUICamera = new CameraUnit(1f);

        loaderUIEngine.addUnit(loaderUICamera);

        BackgroundUnit backgroundUnit = new BackgroundUnit();
        backgroundUnit.renderNode.cameraType.v = 0;
        backgroundUnit.renderNode.width.v = 1;
        backgroundUnit.renderNode.height.v = 1;
        loaderUIEngine.addUnit(backgroundUnit);

        ButtonUnit buttonUnit = new ButtonUnit() {
            @Override
            public void onTap() {

                noteworthyEngine.initialize();

                try {
                    LevelOne level = new LevelOne(game);
                    level.loadFromJson(noteworthyEngine, "");
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                state = State.IN_GAME;
            }
        };
        buttonUnit.renderNode.animationName.v = Animations.ANIMATION_BUTTONS_PLAY;
        //buttonUnit.renderNode.coords.pos.set(-0.85, 0);
        buttonUnit.renderNode.coords.pos.set(0, 0);
        buttonUnit.renderNode.coords.rot.setDegrees(0);
        buttonUnit.renderNode.width.v = 1f;
        buttonUnit.renderNode.height.v = 0.5f;
        buttonUnit.renderNode.color.v = Color.WHITE;
        loaderUIEngine.addUnit(buttonUnit);

        buttonUnit = new ButtonUnit() {
            @Override
            public void onTap() {

                noteworthyEngine.initialize();

                try {
                    LevelTwo level = new LevelTwo(game);
                    level.loadFromJson(noteworthyEngine, "");
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                state = State.IN_GAME;
            }
        };
        buttonUnit.renderNode.animationName.v = Animations.ANIMATION_BUTTONS_PLAY;
        //buttonUnit.renderNode.coords.pos.set(-0.85, 0);
        buttonUnit.renderNode.coords.pos.set(0, -0.5);
        buttonUnit.renderNode.coords.rot.setDegrees(0);
        buttonUnit.renderNode.width.v = 1f;
        buttonUnit.renderNode.height.v = 0.5f;
        buttonUnit.renderNode.color.v = Color.WHITE;
        loaderUIEngine.addUnit(buttonUnit);

        loaderUIEngine.flushQueues();


        noteworthyEngine.addEventListener(new BaseEngine.EventListener() {
            @Override
            public void onEvent(int event) {
                if (event == GameEvents.QUIT_WIN || event == GameEvents.QUIT_LOSE) {
                    //game.noteworthyEngine.clear();
                    noteworthyEngine.clear();
                    //game.activeEngine = game.loaderUIEngine;
                    state = State.IN_MENU;
                }
            }
        });
    }

    @Override
    public void clear() {
        super.clear();
        noteworthyEngine.clear(); // Should probably initialize again
    }

    @Override
    public void step(double dt) {
        if (state == State.IN_MENU) {
            loaderUIEngine.step(dt);
        }
        else if (state == State.IN_GAME) {
            noteworthyEngine.step(dt);
        }
    }
}
