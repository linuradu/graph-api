package com.rl.graphapi.service;

import com.rl.graphapi.service.exception.GraphEdgesNotProvidedException;
import com.rl.graphapi.service.exception.GraphNotFoundException;
import com.rl.graphapi.service.model.Edge;
import com.rl.graphapi.service.model.Graph;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GraphServiceTest {
    private static final long NOT_EXISTING_GRAPH_ID = 123L;
    private static final Set<Edge> edges = new HashSet<>();

    static {
        edges.add(new Edge(10, 3));
        edges.add(new Edge(2, 3));
        edges.add(new Edge(3, 6));
        edges.add(new Edge(5, 6));
        edges.add(new Edge(5, 17));
        edges.add(new Edge(4, 5));
        edges.add(new Edge(4, 8));
        edges.add(new Edge(8, 9));
    }

    @Autowired
    GraphService graphService;

    private Graph graph;

    @Before
    public void before() {
        graph = graphService.createGraph(edges);
    }

    @Test
    public void createGraph_returnCreatedGraph_success() {
        final int expectedNodesNo = 9;

        assertThat(graph).withFailMessage("Graph shouldn't be null").isNotNull();
        assertThat(graph.getId()).withFailMessage("Graph id shouldn't be null").isNotNull();
        assertThat(graph.getNodesHeap().size()).isEqualTo(expectedNodesNo);
    }

    @Test
    public void createGraph_edgesListEmpty_exception() {
        assertThatThrownBy(() -> graphService.createGraph(new HashSet<>())).isInstanceOf(GraphEdgesNotProvidedException.class);
    }

    @Test
    public void lookup_returnExistingGraph_success() {
        final Graph result = graphService.lookup(graph.getId());
        assertThat(graph).withFailMessage("Graph shouldn't be null").isNotNull();
        assertThat(result.getId()).isEqualTo(graph.getId());
        assertThat(result.getNodesHeap().size()).isEqualTo(graph.getNodesHeap().size());
    }

    @Test
    public void lookup_graphNotFound_exception() {
        assertThatThrownBy(() -> graphService.lookup(NOT_EXISTING_GRAPH_ID)).isInstanceOf(GraphNotFoundException.class);
    }

    @Test
    public void getNodesHavingParentsCount_returnNodeWithOneParent_success() {
        final Set<Integer> nodesWithOneParent = new HashSet<>(Arrays.asList(17, 5, 8, 9));

        final Set<Integer> result = graphService.getNodesHavingParentsCount(graph.getId(), 1);
        assertThat(result).withFailMessage("Nodes list shouldn't be null").isNotNull();
        assertThat(result).isEqualTo(nodesWithOneParent);
    }

    @Test
    public void getNodesHavingParentsCount_graphNotFound_exception() {
        assertThatThrownBy(() -> graphService.getNodesHavingParentsCount(NOT_EXISTING_GRAPH_ID, 1)).isInstanceOf(GraphNotFoundException.class);
    }
}
