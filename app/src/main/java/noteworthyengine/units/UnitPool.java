package noteworthyengine.units;

import noteworthyframework.Gamer;
import noteworthyframework.Unit;
import utils.MemoryPool;

/**
 * Created by eric on 4/27/15.
 */
public class UnitPool {

    public static final Gamer NO_GAMER = new Gamer("No gamer!") {{
      this.team = Gamer.TeamColors.size() - 1;
    }};

    public static final int NUMBER_UNITS = 512;
    public static final int NUMBER_BUILDINGS = 63;

    public static final MemoryPool<Mech> mechs = new MemoryPool<Mech>(Mech.class, NUMBER_UNITS) {
        @Override
        public Mech newInstance(Class cls) {
            return new Mech(NO_GAMER);
        }

        @Override
        public synchronized Mech fetchMemory() {
            Mech ret = super.fetchMemory();
            ret.reset();
            return ret;
        }
    };

    public static final MemoryPool<Platoon> platoons = new MemoryPool<Platoon>(Platoon.class, NUMBER_UNITS) {
        @Override
        public synchronized Platoon fetchMemory() {
            Platoon ret = super.fetchMemory();
            ret.reset();
            return ret;
        }
    };

    public static final MemoryPool<Cannon> cannons = new MemoryPool<Cannon>(Cannon.class, NUMBER_UNITS) {
        @Override
        public Cannon newInstance(Class cls) {
            return new Cannon(NO_GAMER);
        }

        @Override
        public synchronized Cannon fetchMemory() {
            Cannon ret = super.fetchMemory();
            ret.reset();
            return ret;
        }
    };

    public static final MemoryPool<Missle> missles = new MemoryPool<Missle>(Missle.class, NUMBER_UNITS) {
        @Override
        public Missle newInstance(Class cls) {
            return new Missle(NO_GAMER);
        }

        @Override
        public synchronized Missle fetchMemory() {
            Missle ret = super.fetchMemory();
            ret.reset();
            return ret;
        }
    };

    public static final MemoryPool<Mine> mines = new MemoryPool<Mine>(Mine.class, NUMBER_UNITS) {
        @Override
        public Mine newInstance(Class cls) {
            return new Mine(NO_GAMER);
        }

        @Override
        public synchronized Mine fetchMemory() {
            Mine ret = super.fetchMemory();
            ret.reset();
            return ret;
        }
    };

    public static final MemoryPool<City> cities = new MemoryPool<City>(City.class, NUMBER_BUILDINGS) {
        @Override
        public City newInstance(Class cls) {
            return new City(NO_GAMER);
        }

        @Override
        public synchronized City fetchMemory() {
            City ret = super.fetchMemory();
            ret.reset();
            return ret;
        }
    };

    public static final MemoryPool<MechFactory> mechFactories = new MemoryPool<MechFactory>(MechFactory.class, NUMBER_BUILDINGS) {
        @Override
        public MechFactory newInstance(Class cls) {
            return new MechFactory(NO_GAMER);
        }

        @Override
        public synchronized MechFactory fetchMemory() {
            MechFactory ret = super.fetchMemory();
            ret.reset();
            return ret;
        }
    };

    public static final MemoryPool<Barracks> barracks = new MemoryPool<Barracks>(Barracks.class, NUMBER_BUILDINGS) {
        @Override
        public Barracks newInstance(Class cls) {
            return new Barracks(NO_GAMER);
        }

        @Override
        public synchronized Barracks fetchMemory() {
            Barracks ret = super.fetchMemory();
            ret.reset();
            return ret;
        }
    };

    public static final MemoryPool<CannonFactory> cannonFactories = new MemoryPool<CannonFactory>(CannonFactory.class, NUMBER_BUILDINGS) {

        @Override
        public CannonFactory newInstance(Class cls) {
            return new CannonFactory(NO_GAMER);
        }

        @Override
        public synchronized CannonFactory fetchMemory() {
            CannonFactory ret = super.fetchMemory();
            ret.reset();
            return ret;
        }
    };

    public static final MemoryPool<Nanobot> nanobots = new MemoryPool<Nanobot>(Nanobot.class, NUMBER_UNITS) {
        @Override
        public synchronized Nanobot fetchMemory() {
            Nanobot ret = super.fetchMemory();
            ret.reset();
            return ret;
        }
    };

    public static final MemoryPool<NanobotFactory> nanobotFactories = new MemoryPool<NanobotFactory>(NanobotFactory.class, NUMBER_BUILDINGS) {

        @Override
        public NanobotFactory newInstance(Class cls) {
            return new NanobotFactory(NO_GAMER);
        }

        @Override
        public synchronized NanobotFactory fetchMemory() {
            NanobotFactory ret = super.fetchMemory();
            ret.reset();
            return ret;
        }
    };


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
