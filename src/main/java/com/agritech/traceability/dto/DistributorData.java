package com.agritech.traceability.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class DistributorData {
    private List<String> certifications;
    @NotNull
    private TransportConditions transport;

    @Data
    public static class TransportConditions {
        private double temperature;
        private double humidity;
        private String transportMethod;
        private String storageConditions;
    }
}
