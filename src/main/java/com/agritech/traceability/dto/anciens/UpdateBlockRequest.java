package com.agritech.traceability.dto.anciens;

import com.agritech.traceability.entities.NodeType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateBlockRequest {
    @NotNull(message = "Le type de nœud est obligatoire")
    private NodeType nodeType;

    @NotNull(message = "Les données du distributeur sont obligatoires")
    private DistributorData distributorData;
}
