package noteworthyengine.units;

import noteworthyframework.Gamer;
import utils.MemoryPool;

/**
 * Created by eric on 4/27/15.
 */
public class UnitPool {

    public static final Gamer NO_GAMER = new Gamer("No gamer!");

    public static final int NUMBER_UNITS = 512;

    public static final MemoryPool<Mech> mechs = new MemoryPool<Mech>(Mech.class, NUMBER_UNITS) {
        @Override
        public Mech newInstance(Class cls) {
            return new Mech(NO_GAMER);
        }
    };

    public static final MemoryPool<Platoon> platoons = new MemoryPool<Platoon>(Platoon.class, NUMBER_UNITS);

    public static final MemoryPool<Cannon> cannons = new MemoryPool<Cannon>(Cannon.class, NUMBER_UNITS) {
        @Override
        public Cannon newInstance(Class cls) {
            return new Cannon(NO_GAMER);
        }
    };

    public static final MemoryPool<Missle> missles = new MemoryPool<Missle>(Missle.class, NUMBER_UNITS) {
        @Override
        public Missle newInstance(Class cls) {
            return new Missle(NO_GAMER);
        }
    };

    public static final MemoryPool<Mine> mines = new MemoryPool<Mine>(Mine.class, NUMBER_UNITS) {
        @Override
        public Mine newInstance(Class cls) {
            return new Mine(NO_GAMER);
        }
    };

    public static final MemoryPool<City> cities = new MemoryPool<City>(City.class, NUMBER_UNITS) {
        @Override
        public City newInstance(Class cls) {
            return new City(NO_GAMER);
        }
    };

    public static void load() {
        // Does nothing. Just load class static fields via class loader.
    }

    private UnitPool() {
//        mechs = new MemoryPool<Mech>(Mech.class, numberUnits) {
//            @Override
//            public Mech newInstance(Class cls) {
//                return new Mech(NO_GAMER);
//            }
//        };
//
//        platoons = new MemoryPool<Platoon>(Platoon.class, numberUnits);
//
//        cannons = new MemoryPool<Cannon>(Cannon.class, numberUnits) {
//            @Override
//            public Cannon newInstance(Class cls) {
//                return new Cannon(NO_GAMER);
//            }
//        };
//
//        missles = new MemoryPool<Missle>(Missle.class, numberUnits) {
//            @Override
//            public Missle newInstance(Class cls) {
//                return new Missle(NO_GAMER);
//            }
//        };
//
//        mines = new MemoryPool<Mine>(Mine.class, numberUnits) {
//            @Override
//            public Mine newInstance(Class cls) {
//                return new Mine(NO_GAMER);
//            }
//        };
//
//        cities = new MemoryPool<City>(City.class, numberUnits) {
//            @Override
//            public City newInstance(Class cls) {
//                return new City(NO_GAMER);
//            }
//        };
    }
}
