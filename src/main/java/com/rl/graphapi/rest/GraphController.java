package com.rl.graphapi.rest;

import com.rl.graphapi.rest.converter.GraphConverter;
import com.rl.graphapi.rest.converter.dto.GraphDTO;
import com.rl.graphapi.service.GraphProcessingService;
import com.rl.graphapi.service.GraphService;
import com.rl.graphapi.service.model.Edge;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/graphs")
public class GraphController {

    private final GraphConverter graphConverter;
    private final GraphService graphService;
    private final GraphProcessingService graphProcessingService;

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GraphDTO createGraph(@RequestBody Set<Edge> edges) {

        return graphConverter.toGraphDTO(graphService.createGraph(edges));
    }

    @GetMapping(value = "/{graphId}/nodes")
    @ResponseStatus(HttpStatus.OK)
    public Set<Integer> getNodesHavingParentsCount(@PathVariable Long graphId, @RequestParam Integer parentsCount) {

        return graphService.getNodesHavingParentsCount(graphId, parentsCount);
    }

    @GetMapping(value = "/{graphId}/nodes/{firstNodeId}/has-common-ancestor/{secondNodeId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean haveNodesCommonAncestor(@PathVariable Long graphId,
                                           @PathVariable int firstNodeId,
                                           @PathVariable int secondNodeId) {
        return graphProcessingService.haveNodesCommonAncestor(graphId, firstNodeId, secondNodeId);
    }
}
