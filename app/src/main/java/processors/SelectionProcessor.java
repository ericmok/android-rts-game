package processors;

import java.util.ArrayList;

import components.Denormalizable;
import components.PositionComponent;
import components.SelectionComponent;
import game.androidgame2.Game;
import components.Entity;
import components.Engine;
import game.androidgame2.GameLoop;

/**
 * Created by eric on 10/31/14.
 */
public class SelectionProcessor {

    private Game game;
    public ArrayList<Entity> selectedEntities = new ArrayList<Entity>(64);

    public SelectionProcessor(Game game) {
        this.game = game;
    }

    public double square(double x) {
        return Math.pow(x, 2);
    }

    public void process(ArrayList<Entity> selectableEntities, float touchX, float touchY) {

        for (int i = 0; i < selectableEntities.size(); i++) {
            Entity entity = selectableEntities.get(i);
            PositionComponent pc = (PositionComponent)entity.cData.get(PositionComponent.class);
            SelectionComponent sc = (SelectionComponent)entity.cData.get(SelectionComponent.class);

            double sqDist = square(pc.x - touchX) + square(pc.y - touchY);

            if (sqDist < 2 * 0.07) {
                selectedEntities.add(entity);
                sc.isSelected = true;
            }
            else {
                sc.isSelected = false;
            }
        }
    }
}