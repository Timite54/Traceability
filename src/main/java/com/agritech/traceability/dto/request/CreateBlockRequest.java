package com.agritech.traceability.dto.request;

import com.agritech.traceability.dto.ProducerData;
import com.agritech.traceability.entities.NodeType;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBlockRequest {
    @NotBlank(message = "Le nom du produit est obligatoire")
    private String productName;

    @NotNull(message = "Les données du producteur sont obligatoires")
    @OneToOne
    private ProducerData producerData;
}