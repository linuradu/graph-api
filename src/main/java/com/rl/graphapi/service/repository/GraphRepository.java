package com.rl.graphapi.service.repository;

import com.rl.graphapi.service.exception.GraphNotFoundException;
import com.rl.graphapi.service.model.Edge;
import com.rl.graphapi.service.model.Graph;
import com.rl.graphapi.service.model.Node;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class GraphRepository {
    private static final Random ID_GENERATOR = new Random();
    private static final List<Graph> graphs = new ArrayList<>();

    /**
     * Based on the provided edges will be created the graph
     *
     * @param edges the graph edges
     * @return the created graph
     */
    public Graph createGraph(final Set<Edge> edges) {
        // generate the id and create the empty graph
        final Graph graph = new Graph(ID_GENERATOR.nextLong());

        // creating the graph edges
        edges.forEach(edge ->
            graph.addEdge(edge.getParent(), edge.getChild())
        );

        // persist
        graphs.add(graph);

        return graph;
    }

    /**
     * Return the graph based on the provided graph id.
     * In case the graph is not found will throw {@GraphNotFoundException}.
     *
     * @param graphId the graph id
     * @return the graph or throw exception if now found.
     */
    public Graph lookup(final Long graphId) {
        return graphs.stream().filter(graph -> graph.getId().equals(graphId)).findFirst()
                .orElseThrow(() -> new GraphNotFoundException(String.format("Graph with id: %s not found!", graphId)));
    }

    /**
     * Return the graph node ids having the provided no of parents.
     * In case the graph is not found will throw {@GraphNotFoundException}.
     *
     * @param graphId      the graph id
     * @param parentsCount the number of node parents
     * @return the graph node ids having the provided no of parents
     */
    public Set<Integer> getNodesHavingParentsCount(final Long graphId, final int parentsCount) {
        final Graph existingGraph = lookup(graphId);
        return existingGraph.getNodesHeap().values().stream()
                .filter(node -> node.getParentsCount() == parentsCount)
                .map(Node::getId).collect(Collectors.toSet());
    }
}
