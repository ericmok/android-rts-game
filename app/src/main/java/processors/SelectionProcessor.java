package processors;

import java.util.ArrayList;

import components.WorldComponent;
import components.SelectionComponent;
import structure.Game;
import components.Entity;
import structure.GameCamera;
import structure.GameSettings;
import structure.Vector2;
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
    public void process(ArrayList<Entity> selectableEntities, GameCamera gameCamera, Vector2 touchPosition, VoidFunc<Entity> selectedFunc) {

        for (int i = 0; i < selectableEntities.size(); i++) {
            Entity entity = selectableEntities.get(i);
            WorldComponent pc = (WorldComponent)entity.cData.get(WorldComponent.class);
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
                //sc.isSelected = true;
                selectedFunc.apply(entity);
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