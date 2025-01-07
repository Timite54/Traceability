package com.agritech.traceability.dto.anciens;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class DistributorData {
    private List<String> certifications;

    @NotNull(message = "Les informations de transport sont obligatoires")
    private TransportInfo transport;

//    @NotNull(message = "Le type de nœud est obligatoire")
//    private NodeType nodeType;
//
//    @NotBlank(message = "Les données sont obligatoires")
//    private String data;
//        private List<String> certifications;
//
//        @NotNull(message = "Les informations de transport sont obligatoires")
//        private TransportInfo transport;
    }

