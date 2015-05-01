package noteworthyengine.levels;

import org.json.JSONException;

import java.util.ArrayList;

import noteworthyengine.units.Barracks;
import noteworthyengine.units.Cannon;
import noteworthyengine.units.CannonFactory;
import noteworthyengine.units.City;
import noteworthyengine.units.Mech;
import noteworthyengine.units.MechFactory;
import noteworthyengine.units.Mine;
import noteworthyengine.units.Platoon;
import noteworthyengine.units.TimelineCommand;
import noteworthyengine.units.UnitPool;
import noteworthyframework.BaseEngine;
import noteworthyframework.EngineDataLoader;
import noteworthyframework.Gamer;
import utils.Orientation;
import utils.Vector2;

/**
 * Created by eric on 4/27/15.
 */
public class LevelOne implements EngineDataLoader {

    public void spawnBase(BaseEngine baseEngine, Gamer gamer, Vector2 location) {

        Vector2 rot = new Vector2();
        rot.copy(location);
        rot.x = -rot.x;
        rot.y = -rot.y;
        rot.setNormalized();

        Vector2 perp = new Vector2();
        Orientation.getPerpendicular(perp, rot);

//        City city = UnitPool.cities.fetchMemory(); //new City(gamer);
//        city.configure(gamer);
//        city.battleNode.coords.pos.copy(location);
//        baseEngine.addUnit(city);

        CannonFactory cannonFactory = UnitPool.cannonFactories.fetchMemory(); //UnitPool.cities.fetchMemory(); //new City(gamer);
        cannonFactory.configure(gamer);
        cannonFactory.battleNode.coords.pos.copy(location);
        cannonFactory.battleNode.coords.pos.translate(0, -1);
        baseEngine.addUnit(cannonFactory);

        Barracks barracks = UnitPool.barracks.fetchMemory(); //new City(gamer);
        barracks.configure(gamer);
        barracks.battleNode.coords.pos.copy(location);
        barracks.battleNode.coords.pos.translate(1, 1);
        baseEngine.addUnit(barracks);

        MechFactory mechFactory = UnitPool.mechFactories.fetchMemory(); //UnitPool.cities.fetchMemory(); //new City(gamer);
        mechFactory.configure(gamer);
        mechFactory.battleNode.coords.pos.copy(location);
        mechFactory.battleNode.coords.pos.translate(-1, 1);
        baseEngine.addUnit(mechFactory);

        Mine mine = UnitPool.mines.fetchMemory(); //new Mine(gamer);
        mine.configure(gamer);
        mine.battleNode.coords.pos.copy(location);
        mine.battleNode.coords.pos.translate(-2, -2);
        baseEngine.addUnit(mine);

        mine = UnitPool.mines.fetchMemory();
        mine.configure(gamer);
        mine.battleNode.coords.pos.copy(location);
        mine.battleNode.coords.pos.translate(-2, 2);
        baseEngine.addUnit(mine);

        mine = UnitPool.mines.fetchMemory();
        mine.configure(gamer);
        mine.battleNode.coords.pos.copy(location);
        mine.battleNode.coords.pos.translate(2, 2);
        baseEngine.addUnit(mine);

        mine = UnitPool.mines.fetchMemory();
        mine.configure(gamer);
        mine.battleNode.coords.pos.copy(location);
        mine.battleNode.coords.pos.translate(2, -2);
        baseEngine.addUnit(mine);

        for (int i = -3; i <= 3; i++) {
            Mech mech = UnitPool.mechs.fetchMemory(); //new Mech(gamer);
            mech.configure(gamer);
            mech.battleNode.coords.pos.copy(location);
            mech.battleNode.coords.pos.translate(i * perp.x, i * perp.y);
            baseEngine.addUnit(mech);
        }

        for (int h = 0; h <= 2; h++) {
            for (int i = -4; i <= 4; i++) {
                Platoon platoon = UnitPool.platoons.fetchMemory(); //new Platoon();
                platoon.configure(gamer);
                platoon.battleNode.coords.pos.copy(location);
                platoon.battleNode.coords.pos.translate((i) * perp.x + h * rot.x, (i) * perp.y + h * rot.y);
                //platoon.battleNode.gamer.v = gamer;
                baseEngine.addUnit(platoon);
            }
        }

        for (int i = 0; i < 4; i++) {
            Cannon cannon = UnitPool.cannons.fetchMemory(); // new Cannon(gamer);
            cannon.configure(gamer);
            cannon.battleNode.coords.pos.copy(location);
            cannon.battleNode.coords.pos.translate((i) * perp.x + 2 * rot.x, ((i)) * perp.y + 2 * rot.y);
            baseEngine.addUnit(cannon);
        }

    }

    @Override
    public boolean loadFromJson(BaseEngine baseEngine, String json) throws JSONException {

        baseEngine.gameTime = 0;

        Gamer gamer0 = new Gamer("taco");
        gamer0.team = 0;

        Gamer gamer1 = new Gamer("avilo");
        gamer1.team = 1;

        Gamer gamer2 = new Gamer("dragon");
        gamer2.team = 2;

        Gamer gamer3 = new Gamer("connie");
        gamer3.team = 3;

        baseEngine.addGamer(gamer0);
        baseEngine.addGamer(gamer1);
        baseEngine.addGamer(gamer2);
        baseEngine.addGamer(gamer3);

        Vector2 spawnLocation0 = new Vector2(0, -13);
        Vector2 spawnLocation1 = new Vector2(0, 13);
        Vector2 spawnLocation2 = new Vector2(-13, 0);
        Vector2 spawnLocation3 = new Vector2(13, 0);

        ArrayList<Vector2> spawnLocations = new ArrayList<Vector2>(4);
        spawnLocations.add(spawnLocation0);
        spawnLocations.add(spawnLocation1);
        spawnLocations.add(spawnLocation2);
        spawnLocations.add(spawnLocation3);

        for (int i = 0; i < baseEngine.gamers.size(); i++) {
            spawnBase(baseEngine, baseEngine.gamers.get(i), spawnLocations.get(i));
        }


        Gamer gamer4 = new Gamer("neutral");
        gamer4.team = 4;

        baseEngine.addGamer(gamer4);

        ArrayList<Vector2> neutralSpawnLocations = new ArrayList<Vector2>(8);
        neutralSpawnLocations.add(new Vector2(-4, -4));
        neutralSpawnLocations.add(new Vector2(4, 4));
        neutralSpawnLocations.add(new Vector2(-4, 4));
        neutralSpawnLocations.add(new Vector2(4, -4));
        neutralSpawnLocations.add(new Vector2(-10.5, -10.5));
        neutralSpawnLocations.add(new Vector2(10.5, 10.5));
        neutralSpawnLocations.add(new Vector2(-10.5, 10.5));
        neutralSpawnLocations.add(new Vector2(10.5, -10.5));

        for (int i = 0; i < neutralSpawnLocations.size(); i++) {
            Barracks barracks = UnitPool.barracks.fetchMemory();
            barracks.configure(gamer4);
            barracks.battleNode.coords.pos.copy(neutralSpawnLocations.get(i));
            baseEngine.addUnit(barracks);
        }

        baseEngine.currentGamer = gamer0;

        baseEngine.flushQueues();

        return true;
    }
}
