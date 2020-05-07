package com.rl.graphapi.rest.converter;

import com.rl.graphapi.rest.converter.dto.GraphDTO;
import com.rl.graphapi.rest.converter.dto.NodeDTO;
import com.rl.graphapi.service.model.Graph;
import com.rl.graphapi.service.model.Node;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GraphConverter {

    public GraphDTO toGraphDTO(final Graph graph) {
        final Set<NodeDTO> nodeDTOList = graph.getNodesHeap().values().stream().map(node -> NodeDTO.builder()
                .id(node.getId())
                .parents(node.getParents().stream().map(Node::getId).collect(Collectors.toSet())).build()
        ).collect(Collectors.toSet());

        return GraphDTO.builder()
                .id(graph.getId())
                .nodes(nodeDTOList).build();
    }
}

