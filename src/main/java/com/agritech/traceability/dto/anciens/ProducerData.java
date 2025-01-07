package com.agritech.traceability.dto.anciens;



import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class ProducerData {
    @NotBlank(message = "Le nom du producteur est obligatoire")
    private String name;
    @NotBlank(message = "La localisation est obligatoire")
    private String location;
    private String fertilizers; // Types d'engrais utilisés
    private String packaging;   // Conditionnement
    private String harvestDate; // Date de récolte
}
