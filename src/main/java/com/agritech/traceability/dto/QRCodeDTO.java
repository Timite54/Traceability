package com.agritech.traceability.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class QRCodeDTO {
    private String productId;
    private String productName;
    private String basicInfo;
    private String producer;
    private LocalDateTime productionDate;
    private String certifications;
    private String transportConditions;
    private String retailerInfo;
    private String verificationUrl;
}