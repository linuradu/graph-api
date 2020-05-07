package com.rl.graphapi.rest;

import com.rl.graphapi.rest.converter.GraphConverter;
import com.rl.graphapi.rest.converter.dto.GraphDTO;
import com.rl.graphapi.rest.converter.dto.NodeDTO;
import com.rl.graphapi.service.GraphProcessingService;
import com.rl.graphapi.service.GraphService;
import com.rl.graphapi.service.exception.GraphNotFoundException;
import com.rl.graphapi.service.exception.NodeNotFoundException;
import com.rl.graphapi.service.model.Edge;
import com.rl.graphapi.service.model.Graph;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class GraphControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected GraphService graphService;

    @MockBean
    protected GraphProcessingService graphProcessingService;

    @MockBean
    protected GraphConverter graphConverter;

    @Before
    public void setUp() {
        Mockito.reset(graphService, graphProcessingService, graphConverter);
    }

    @Test
    public void createGraph_validInput_success() throws Exception {
        final Graph graph = createGraph();
        final GraphDTO graphDTO = createGraphDTO();
        final String edgesBody = "[{\"parent\": 10,\"child\": 3},{\"parent\": 2,\"child\": 3}]";
        final Set<Edge> edges = new HashSet<>();
        edges.add(new Edge(10, 3));
        edges.add(new Edge(2, 3));

        when(graphService.createGraph(edges)).thenReturn(graph);
        when(graphConverter.toGraphDTO(graph)).thenReturn(graphDTO);

        mockMvc.perform(put("/graphs")
                .content(edgesBody)
                .contentType(APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.nodes").isArray())
                .andExpect(jsonPath("$.nodes[0].id", is(3)))
                .andExpect(jsonPath("$.nodes[0].parents").isArray())
                .andExpect(jsonPath("$.nodes[0].parents[0]", is(2)))
                .andExpect(jsonPath("$.nodes[0].parents[1]", is(10)));
    }

    @Test
    public void createGraph_graphEdgesNotProvided_exception() throws Exception {
        final String exceptionMessage = "Graph edges should be provided!";
        final String edgesBody = "[]";

        when(graphService.createGraph(new HashSet<>())).thenThrow(new GraphNotFoundException(exceptionMessage));

        mockMvc.perform(put("/graphs")
                .content(edgesBody)
                .contentType(APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(exceptionMessage)));
    }

    @Test
    public void getNodesHavingParentsCount_validInput_success() throws Exception {
        final Set<Integer> nodesWithOneParent = new HashSet<>(Arrays.asList(17, 5, 8, 9));

        when(graphService.getNodesHavingParentsCount(16985968L, 1)).thenReturn(nodesWithOneParent);

        mockMvc.perform(get("/graphs/16985968/nodes?parentsCount=1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0]", is(17)))
                .andExpect(jsonPath("$[1]", is(5)))
                .andExpect(jsonPath("$[2]", is(8)))
                .andExpect(jsonPath("$[3]", is(9)));
    }

    @Test
    public void getNodesHavingParentsCount_graphNotFound_exception() throws Exception {
        final String exceptionMessage = String.format("Graph with id: %s not found!", 4321L);

        when(graphService.getNodesHavingParentsCount(4321L, 1)).thenThrow(new GraphNotFoundException(exceptionMessage));

        mockMvc.perform(get("/graphs/4321/nodes?parentsCount=1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(exceptionMessage)));
    }

    @Test
    public void haveNodesCommonAncestor_validInput_success() throws Exception {
        when(graphProcessingService.haveNodesCommonAncestor(16985968L, 5, 8)).thenReturn(true);

        mockMvc.perform(get("/graphs/16985968/nodes/5/has-common-ancestor/8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
    }

    @Test
    public void haveNodesCommonAncestor_graphNotFound_exception() throws Exception {
        final String exceptionMessage = String.format("Graph with id: %s not found!", 4321L);

        when(graphProcessingService.haveNodesCommonAncestor(4321L, 5, 8)).thenThrow(new GraphNotFoundException(exceptionMessage));

        mockMvc.perform(get("/graphs/4321/nodes/5/has-common-ancestor/8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(exceptionMessage)));
    }

    @Test
    public void haveNodesCommonAncestor_nodeNotFound_exception() throws Exception {
        final String exceptionMessage = String.format("Node with key %s does not exists!", 38);

        when(graphProcessingService.haveNodesCommonAncestor(4321L, 5, 38)).thenThrow(new NodeNotFoundException(exceptionMessage));

        mockMvc.perform(get("/graphs/4321/nodes/5/has-common-ancestor/38")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(exceptionMessage)));
    }

    private GraphDTO createGraphDTO() {
        final Set<NodeDTO> graphNodes = new HashSet<>();
        graphNodes.add(NodeDTO.builder().id(3).parents(new HashSet<>(Arrays.asList(10, 2))).build());

        return GraphDTO.builder()
                .id(16985968L)
                .nodes(graphNodes).build();
    }

    private Graph createGraph() {
        return new Graph(16985968L);
    }
}
