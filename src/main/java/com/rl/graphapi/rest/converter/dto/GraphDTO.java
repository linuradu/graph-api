package com.rl.graphapi.rest.converter.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class GraphDTO {

    private Long id;
    private Set<NodeDTO> nodes;
}
