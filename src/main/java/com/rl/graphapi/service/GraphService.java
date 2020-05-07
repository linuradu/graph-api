package com.rl.graphapi.service;

import com.rl.graphapi.service.exception.GraphEdgesNotProvidedException;
import com.rl.graphapi.service.model.Edge;
import com.rl.graphapi.service.model.Graph;
import com.rl.graphapi.service.repository.GraphRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;

@AllArgsConstructor
@Service
public class GraphService {

    private final GraphRepository graphRepository;

    public Graph createGraph(final Set<Edge> edges){
        if(CollectionUtils.isEmpty(edges)){
            throw new GraphEdgesNotProvidedException("Graph edges not provided on graph creation!");
        }
        return graphRepository.createGraph(edges);
    }

    Graph lookup(final Long graphId) {
        return graphRepository.lookup(graphId);
    }

    public Set<Integer> getNodesHavingParentsCount(final Long graphId, final int parentsCount) {
        return graphRepository.getNodesHavingParentsCount(graphId, parentsCount);
    }
}
