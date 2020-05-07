package com.rl.graphapi.rest.converter.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class NodeDTO {
    private Integer id;
    private Set<Integer> parents;
}
