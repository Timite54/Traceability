package com.agritech.traceability.dto.anciens;

import lombok.Data;

@Data
    public class TransportInfo {
        private String temperature;
        private String humidity;
        private String transportMethod;
        private String departureDate;
        private String arrivalDate;
    }
