package processors;

import android.util.Log;

import java.util.ArrayList;

import components.Denormalizable;
import components.PositionComponent;
import components.SelectionComponent;
import game.androidgame2.Game;
import components.Entity;
import components.Engine;
import game.androidgame2.GameCamera;
import game.androidgame2.GameLoop;
import game.androidgame2.GameSettings;
import game.androidgame2.Vector2;
import utils.VoidFunc;

/**
 * Created by eric on 10/31/14.
 */
public class SelectionProcessor {

    public static final int SELECTION_WIDTH = 3;

    private Game game;
    public ArrayList<Entity> userSelection = new ArrayList<Entity>(64);

    private Vector2 temp = new Vector2();
    private Vector2 temp2 = new Vector2();

    public SelectionProcessor(Game game) {
        this.game = game;
    }

    public double square(double x) {
        return Math.pow(x, 2);
    }

    /**
     * Given input, mark entities selected if they fall inside the selection width
     * @param selectableEntities
     * @param gameCamera
     * @param touchPosition
     */
    public void process(ArrayList<Entity> selectableEntities, GameCamera gameCamera, Vector2 touchPosition) {

        for (int i = 0; i < selectableEntities.size(); i++) {
            Entity entity = selectableEntities.get(i);
            PositionComponent pc = (PositionComponent)entity.cData.get(PositionComponent.class);
            SelectionComponent sc = (SelectionComponent)entity.cData.get(SelectionComponent.class);

            Vector2 worldCoords = temp;
            gameCamera.getTouchToWorldCords(worldCoords, touchPosition);

            //Log.i("SelectionProcessor", "select? " + pc.x + "," + pc.y + " when [" + worldCoordX + "," + worldCoordY + "]");

            Vector2 ray = temp2;
            Vector2.subtract(ray, pc.pos, worldCoords);
            double sqDist = ray.magnitude();

            /*
            If you zoom out, your selection circle will actually be bigger.
            If the camera is at default, you'd expect all multipliers to cancel each other
             */
            if (sqDist < GameSettings.UNIT_LENGTH_MULTIPLIER * (SELECTION_WIDTH / gameCamera.scale)) {
                userSelection.add(entity);
                sc.isSelected = true;
            }
        }
    }

    public static final VoidFunc<Entity> FN_SELECT = new VoidFunc<Entity>() {
        public void apply(Entity element) {
            SelectionComponent sc = (SelectionComponent)element.cData.get(SelectionComponent.class);
            sc.isSelected = true;
        }
    };

    public static final VoidFunc<Entity> FN_DESELECT = new VoidFunc<Entity>() {
        public void apply(Entity element) {
            SelectionComponent sc = (SelectionComponent)element.cData.get(SelectionComponent.class);
            sc.isSelected = false;
        }
    };
}