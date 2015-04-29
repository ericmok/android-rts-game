package noteworthyengine.units;

import noteworthyframework.Gamer;
import utils.MemoryPool;

/**
 * Created by eric on 4/27/15.
 */
public class UnitPool {

    public static final Gamer NO_GAMER = new Gamer("No gamer!") {{
      this.team = Gamer.TeamColors.size() - 1;
    }};

    public static final int NUMBER_UNITS = 512;

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

    public static final MemoryPool<City> cities = new MemoryPool<City>(City.class, NUMBER_UNITS) {
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

    public static void load() {
        // Does nothing. Just load class static fields via class loader.
    }

    private UnitPool() {
    }
}
