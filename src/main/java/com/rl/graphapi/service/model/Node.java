package com.rl.graphapi.service.model;

import lombok.Value;

import java.util.LinkedList;
import java.util.List;

@Value
public class Node {
    private Integer id;
    private List<Node> parents = new LinkedList<>();

    Node(final Integer id) {
        this.id = id;
    }

    public int getParentsCount() {
        return parents.size();
    }

    void addAncestor(final Node parentNode) {
        this.parents.add(parentNode);
    }
}
