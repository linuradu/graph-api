package com.rl.graphapi.service.model;

import lombok.Value;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

@Value
public class NodeProcessingData {
    private final Queue<Node> nextToVisit = new LinkedList<>();
    private final Set<Integer> visitedNodes = new HashSet<>();

    public void addNextToVisit(final Node node) {
        this.nextToVisit.add(node);
    }

    public void addVisited(final Node node) {
        this.visitedNodes.add(node.getId());
    }
}
