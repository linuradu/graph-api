package com.rl.graphapi.service.model;

import lombok.Value;

@Value
public class Edge {
    private final Integer parent;
    private final Integer child;

}
