package com.rl.graphapi.service;

import com.rl.graphapi.service.exception.NodeNotFoundException;
import com.rl.graphapi.service.model.Graph;
import com.rl.graphapi.service.model.Node;
import com.rl.graphapi.service.model.NodeProcessingData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class GraphProcessingService {

    private final GraphService graphService;

    /**
     * In order to have a more efficient way of finding if two nodes have common parents
     * we will process the ancestors of every node in parallel, until will find the first
     * known ancestor
     *
     * @param graphId      the graph id
     * @param firstNodeId  the id of the first node
     * @param secondNodeId the id of the first node
     * @return true if the given nodes shares at leas one known ancestor, false otherwise.
     */
    public boolean haveNodesCommonAncestor(final Long graphId, int firstNodeId, int secondNodeId) {
        final Graph graph = graphService.lookup(graphId);
        validateNodeExists(graph, firstNodeId);
        validateNodeExists(graph, secondNodeId);

        final Node firstNode = graph.getNodesHeap().get(firstNodeId);
        final NodeProcessingData firstNodeProcessing = new NodeProcessingData();
        firstNodeProcessing.addNextToVisit(firstNode);
        firstNodeProcessing.addVisited(firstNode);

        final Node secondNode = graph.getNodesHeap().get(secondNodeId);
        final NodeProcessingData secondNodeProcessing = new NodeProcessingData();
        secondNodeProcessing.addNextToVisit(secondNode);
        secondNodeProcessing.addVisited(secondNode);

        while (!firstNodeProcessing.getNextToVisit().isEmpty() && !secondNodeProcessing.getNextToVisit().isEmpty()) {
            // we need to check all parents from this level
            final List<Node> firstNextLevelParents = iterateParentsBucket(firstNodeProcessing);
            firstNodeProcessing.getNextToVisit().addAll(firstNextLevelParents);

            // we need to check all parents from this level
            final List<Node> secondNextLevelParents = iterateParentsBucket(secondNodeProcessing);
            secondNodeProcessing.getNextToVisit().addAll(secondNextLevelParents);

            // Verify if the targeted nodes shares at least one knows ancestor
            final Set<Integer> temp = new HashSet<>(firstNodeProcessing.getVisitedNodes());
            if (temp.retainAll(secondNodeProcessing.getVisitedNodes()) && !temp.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Iterating over all parents from this bucket.
     *
     * @param nodeProcessing the node processing data
     * @return the list of the ancestors from the nex level to be processed
     */
    private List<Node> iterateParentsBucket(final NodeProcessingData nodeProcessing) {
        final List<Node> nextLevelParents = new LinkedList<>();

        // we need to check all parents from this level
        IntStream.range(0, nodeProcessing.getNextToVisit().size()).forEach(index -> {
            final Node headNode = nodeProcessing.getNextToVisit().remove();
            headNode.getParents().forEach(child -> {
                if (!nodeProcessing.getVisitedNodes().contains(child.getId())) {
                    nodeProcessing.addVisited(child);
                    nextLevelParents.add(child);
                }
            });
        });

        return nextLevelParents;
    }

    private void validateNodeExists(final Graph graph, final int nodeId) {
        if (!graph.getNodesHeap().containsKey(nodeId)) {
            throw new NodeNotFoundException(String.format("Node with key %s does not exists!", nodeId));
        }
    }

}
