package noteworthyengine.players;

import noteworthyframework.Node;
import noteworthyframework.Unit;

/**
 * Created by eric on 2/1/16.
 */
public class PlayerNode extends Node {
    public static final String _NAME = "playerNode";

    public PlayerData playerData = new PlayerData();

    public PlayerNode(Unit unit) {
        super(PlayerNode.class, unit);
        Node.instantiatePublicFieldsForUnit(unit, PlayerNode.class, this);
    }

    /**
     * Created by eric on 3/6/15.
     */
    public static class PlayerData {

        public String playerTag = "player_" + Math.floor(Math.random() * 10000);
        public int team = 0;

        // IP Address and all

        public PlayerData() {
        }

        public PlayerData(String playerTag) {
            this.playerTag = playerTag;
        }

        public boolean equals(PlayerData otherPlayer) {
            return this.playerTag == otherPlayer.playerTag;
        }

        public PlayerData copy(PlayerData other) {
            this.playerTag = other.playerTag;
            this.team = other.team;

            return this;
        }

        @Override
        // TODO: Test
        public int hashCode() {
            return playerTag.hashCode();
        }

        @Override
        // TODO: Test
        public boolean equals(Object o) {
            if (o instanceof PlayerData) {
                return this.playerTag == ((PlayerData) o).playerTag;
            }
            else {
                return false;
            }
        }

        @Override
        public String toString() {
            return playerTag;
        }
    }

    public static class PlayerNodePtr {
        public PlayerNode v;
    }
}
