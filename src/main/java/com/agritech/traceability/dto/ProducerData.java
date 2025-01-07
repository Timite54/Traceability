package com.agritech.traceability.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProducerData {
    @NotBlank
    private String name;
    @NotBlank
    private String location;
    @NotBlank
    private String fertilizersUsed;
    @NotBlank
    private String packagin;
}
