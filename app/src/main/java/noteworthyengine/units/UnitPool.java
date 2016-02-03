package noteworthyengine.units;

import art.Constants;
import noteworthyengine.players.PlayerUnit;
import noteworthyframework.Unit;
import utils.MemoryPool;

/**
 * Created by eric on 4/27/15.
 */
public class UnitPool {

    public static final PlayerUnit NO_GAMER = new PlayerUnit() {{
      this.playerNode.playerData.playerTag = "No Team";
      this.playerNode.playerData.team = Constants.TeamColors.size() - 1;
    }};

    public static final int NUMBER_UNITS = 256;
    public static final int NUMBER_BUILDINGS = 63;

    public static final MemoryPool<Mech> mechs = new MemoryPool<Mech>(Mech.class, NUMBER_UNITS);

    public static final MemoryPool<Platoon> platoons = new MemoryPool<Platoon>(Platoon.class, NUMBER_UNITS);

    public static final MemoryPool<Cannon> cannons = new MemoryPool<Cannon>(Cannon.class, NUMBER_UNITS);

    public static final MemoryPool<Missle> missles = new MemoryPool<Missle>(Missle.class, NUMBER_UNITS);

    public static final MemoryPool<Mine> mines = new MemoryPool<Mine>(Mine.class, NUMBER_UNITS);

    public static final MemoryPool<Zug> zugs = new MemoryPool<Zug>(Zug.class, NUMBER_UNITS);

    public static final MemoryPool<City> cities = new MemoryPool<City>(City.class, NUMBER_BUILDINGS);

    public static final MemoryPool<MechFactory> mechFactories = new MemoryPool<MechFactory>(MechFactory.class, NUMBER_BUILDINGS);

    public static final MemoryPool<Barracks> barracks = new MemoryPool<Barracks>(Barracks.class, NUMBER_BUILDINGS);

    public static final MemoryPool<CannonFactory> cannonFactories = new MemoryPool<CannonFactory>(CannonFactory.class, NUMBER_BUILDINGS);

    public static final MemoryPool<Nanobot> nanobots = new MemoryPool<Nanobot>(Nanobot.class, NUMBER_UNITS);

    public static final MemoryPool<NanobotFactory> nanobotFactories = new MemoryPool<NanobotFactory>(NanobotFactory.class, NUMBER_BUILDINGS);

    public static final MemoryPool<ZugNest> zugNests = new MemoryPool<ZugNest>(ZugNest.class, NUMBER_BUILDINGS);

    public static void load() {
        // Does nothing. Just load class static fields via class loader.
    }

    public static void recycle(Unit unit) {
        if (unit.getClass() == Platoon.class) {
            UnitPool.platoons.recycleMemory((Platoon)unit);
        }
        else if (unit.getClass() == Cannon.class) {
            UnitPool.cannons.recycleMemory((Cannon)unit);
        }
        else if (unit.getClass() == Missle.class) {
            UnitPool.missles.recycleMemory((Missle)unit);
        }
        else if (unit.getClass() == Mech.class) {
            UnitPool.mechs.recycleMemory((Mech)unit);
        }
        else if (unit.getClass() == Mine.class) {
            UnitPool.mines.recycleMemory((Mine) unit);
        }
        else if (unit.getClass() == Zug.class) {
            UnitPool.zugs.recycleMemory((Zug) unit);
        }
        else if (unit.getClass() == City.class) {
            UnitPool.cities.recycleMemory((City)unit);
        }
        else if (unit.getClass() == MechFactory.class) {
            UnitPool.mechFactories.recycleMemory((MechFactory)unit);
        }
        else if (unit.getClass() == Barracks.class) {
            UnitPool.barracks.recycleMemory((Barracks)unit);
        }
        else if (unit.getClass() == CannonFactory.class) {
            UnitPool.cannonFactories.recycleMemory((CannonFactory)unit);
        }
        else if (unit.getClass() == Nanobot.class) {
            UnitPool.nanobots.recycleMemory((Nanobot)unit);
        }
        else if (unit.getClass() == NanobotFactory.class) {
            UnitPool.nanobotFactories.recycleMemory((NanobotFactory)unit);
        }
    }

    private UnitPool() {
    }
}
