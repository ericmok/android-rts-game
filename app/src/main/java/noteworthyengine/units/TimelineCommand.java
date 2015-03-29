package noteworthyengine.units;

import noteworthyengine.TimelineNode;
import noteworthyframework.Unit;

/**
 * Created by eric on 3/15/15.
 */
public class TimelineCommand extends Unit {

    public TimelineNode timelineNode;

    public TimelineCommand() {
        this.name = "timelineCommand";

        timelineNode = new TimelineNode(this);
    }
}
