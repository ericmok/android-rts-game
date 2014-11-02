package processors;

import android.util.Log;

import java.util.ArrayList;

import components.CameraSettingsComponent;
import components.Denormalizable;
import components.PositionComponent;
import components.SelectionComponent;
import game.androidgame2.Game;
import components.Entity;
import components.Engine;
import game.androidgame2.GameLoop;
import game.androidgame2.GameSettings;

/**
 * Created by eric on 10/31/14.
 */
public class SelectionProcessor {

    private Game game;
    public ArrayList<Entity> userSelection = new ArrayList<Entity>(64);

    public SelectionProcessor(Game game) {
        this.game = game;
    }

    public double square(double x) {
        return Math.pow(x, 2);
    }

    public void process(ArrayList<Entity> selectableEntities, Entity cameraEntity, float touchX, float touchY) {
        userSelection.clear();

        CameraSettingsComponent csm = (CameraSettingsComponent) cameraEntity.cData.get(CameraSettingsComponent.class);

        for (int i = 0; i < selectableEntities.size(); i++) {
            Entity entity = selectableEntities.get(i);
            PositionComponent pc = (PositionComponent)entity.cData.get(PositionComponent.class);
            SelectionComponent sc = (SelectionComponent)entity.cData.get(SelectionComponent.class);

            float worldCoordX = (touchX / csm.scale) + csm.x;
            float worldCoordY = (touchY  / csm.scale) + csm.y;

            //Log.i("SelectionProcessor", "select? " + pc.x + "," + pc.y + " when [" + worldCoordX + "," + worldCoordY + "]");

            double sqDist = Math.sqrt(square(pc.x - worldCoordX) + square(pc.y - worldCoordY));

            if (sqDist < 2) {
                userSelection.add(entity);
                sc.isSelected = true;
            }
            else {
                sc.isSelected = false;
            }
        }
    }
}