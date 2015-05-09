package noteworthyengine;

import noteworthyframework.*;
import utils.Vector2;

/**
 * Created by eric on 4/29/15.
 */
public class FieldCameraSystem extends noteworthyframework.System {

    QueueMutationList<FieldCameraNode> fieldCameraNodes = new QueueMutationList<FieldCameraNode>(2);

    public GridSystem gridSystem;
    public FieldSystem fieldSystem;

    public Vector2 vector = new Vector2();

    public FieldCameraSystem(GridSystem gridSystem, FieldSystem fieldSystem) {
        this.gridSystem = gridSystem;
        this.fieldSystem = fieldSystem;
    }

    @Override
    public void addNode(Node node) {
        if (node instanceof FieldCameraNode) {
            fieldCameraNodes.queueToAdd((FieldCameraNode)node);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node instanceof FieldCameraNode) {
            fieldCameraNodes.queueToRemove((FieldCameraNode)node);
        }
    }

    @Override
    public void step(double ct, double dt) {
        QueueMutationList<FieldNode> fieldNodes = fieldSystem.arrowsByGamer.get(this.getBaseEngine().currentGamer);

        // Since this is a hashmap.get, the result could be null
        if (fieldNodes == null) return;

        QueueMutationList<FieldNode> agents = fieldSystem.agentsByGamer.get(this.getBaseEngine().currentGamer);

        vector.zero();

        if (agents != null && agents.size() > 0) {
            int numberAgents = agents.size();
            for (int i = numberAgents - 1; i >= 0; i--) {
                vector.translate(agents.get(i)._fieldAgentNode.coords.pos);
            }
            vector.scale(1.0 / numberAgents);
        }
        else {
            vector.copy(gridSystem.grid.calculateCenterOfMass());
        }

        // Move towards center of mass for the units if no fields
        int size = fieldNodes.size();
        if (size < 1) {
            for (int i = 0; i < fieldCameraNodes.size(); i++) {

                FieldCameraNode fieldCameraNode = fieldCameraNodes.get(i);
                Vector2 cameraPos = fieldCameraNode.coords.pos;

                Vector2 cog = vector;
                Vector2.subtract(vector, cog, cameraPos);
                double dist = vector.magnitude();

                double curve = 0.18 * dt * Math.sqrt( Math.max(5, Math.min((5) - dist, 0)) );
                vector.scale(curve, curve);

                //vector.scale(1 / (dist * dist + 1), 1 / (dist * dist + 1));

                cameraPos.translate(vector.x * dt, vector.y * dt);

                // Zoom out
                fieldCameraNode.scale.v = (0.15f * (float)dt) * (fieldCameraNode._originalCameraScale - fieldCameraNode.scale.v) + fieldCameraNode.scale.v;
            }

            return;
        }

        // Move to center of mass for the fields
        for (int i = 0; i < fieldCameraNodes.size(); i++) {

            FieldCameraNode fieldCameraNode = fieldCameraNodes.get(i);

            vector.zero();

            for (int k = 0; k < size; k++) {
                FieldNode.FieldArrowNode fieldArrowNode = fieldNodes.get(k)._fieldArrowNode;

                vector.translate(fieldArrowNode.coords.pos.x, fieldArrowNode.coords.pos.y);
                vector.translate(2 * fieldArrowNode.coords.rot.x, 2 * fieldArrowNode.coords.rot.y);
            }
            vector.scale(1.0 / size, 1.0 / size);

            Vector2 cameraPos = fieldCameraNode.coords.pos;

            Vector2.subtract(vector, vector, cameraPos);
            double dist = vector.magnitude();

            double curve = 0.5 * dt * Math.sqrt( Math.max(5, Math.min((5) - dist, 0)) );
            vector.scale(curve, curve);

            //vector.scale(2 / (dist * dist + 1), 2 / (dist * dist + 1));

            cameraPos.translate(vector.x * dt, vector.y * dt);

            // Zoom in
            fieldCameraNode.scale.v = (0.15f * (float)dt) * (fieldCameraNode.zoomScale.v - fieldCameraNode.scale.v) + fieldCameraNode.scale.v;
        }
    }

    @Override
    public void flushQueues() {
        fieldCameraNodes.flushQueues();
    }
}