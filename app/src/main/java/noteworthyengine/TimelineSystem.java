package noteworthyengine;

import noteworthyframework.*;

/**
 * Created by eric on 3/14/15.
 */
public class TimelineSystem extends noteworthyframework.System {

    public QueueMutationList<TimelineNode> timelineNodes = new QueueMutationList<TimelineNode>(127);

    @Override
    public void addNode(Node node) {
        if (node.getClass() == TimelineNode.class) {
            TimelineNode timelineNode = (TimelineNode)node;
            timelineNodes.queueToAdd(timelineNode);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node.getClass() == TimelineNode.class) {
            TimelineNode timelineNode = (TimelineNode)node;
            timelineNodes.queueToRemove(timelineNode);
        }
    }

    @Override
    public void step(double ct, double dt) {
        for (int i = 0; i < timelineNodes.size(); i++) {
            TimelineNode timelineNode = timelineNodes.get(i);
            if (timelineNode.frameTime.v <= ct) {
                if (timelineNode.type.v == TimelineNode.ADD_NODE) {
                    ArrowCommand arrowCommand = new ArrowCommand();
                    if (timelineNode.gamerPtr.v != null) {
                        arrowCommand.set(timelineNode.gamerPtr.v,
                                timelineNode.coords.pos.x, timelineNode.coords.pos.y,
                                timelineNode.coords.rot.x, timelineNode.coords.rot.y);
                        this.getBaseEngine().addUnit(arrowCommand);

                        this.getBaseEngine().removeUnit(timelineNode.unit);
                    }
                }
            }
        }
    }

    @Override
    public void flushQueues() {
        timelineNodes.flushQueues();
    }
}
