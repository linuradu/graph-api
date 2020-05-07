package com.rl.graphapi.service.model;

import lombok.Value;

import java.util.HashMap;
import java.util.Map;

@Value
public class Graph {
    private Long id;
    private Map<Integer, Node> nodesHeap = new HashMap<>();

    public Graph(final Long id) {
        this.id = id;
    }

    public void addEdge(final Integer parentNodeId, final Integer childNodeId) {
        final Node parentNode = getOrCreateNode(parentNodeId);
        final Node childNode = getOrCreateNode(childNodeId);
        childNode.addAncestor(parentNode);
    }

    /**
     * Return the existing node by the given key or
     * a new created one in case of absence.
     *
     * @param id the node id to get.
     * @return the node.
     */
    private Node getOrCreateNode(final Integer id) {
        // create new node if does not exists
        if (!nodesHeap.containsKey(id)) {
            nodesHeap.put(id, new Node(id));
        }
        return nodesHeap.get(id);
    }
}
