package noteworthyengine.players;

import noteworthyframework.Unit;

/**
 * Created by eric on 2/1/16.
 */
public class PlayerUnit extends Unit {
    public PlayerNode playerNode = new PlayerNode(this);

    public PlayerUnit() {
        super();
    }

    public PlayerUnit(String playerTag, int team) {
        playerNode.playerData.playerTag = playerTag;
        playerNode.playerData.team = team;
    }

    public void configure(String playerTag) {
        playerNode.playerData.playerTag = playerTag;
    }
}
