package com.agritech.traceability.util;


import com.agritech.traceability.dto.BlockDTO;
import com.agritech.traceability.dto.QRCodeDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class QRCodeGenerator {
    private final ObjectMapper objectMapper;

    @Value("${app.verification.url}")
    private String verificationBaseUrl;
    @Value("${app.verification.url}")
    private String basicQrContent;

    public byte[] generateQRCode(BlockDTO block, int width, int height) {
        String qrContent = String.format("Block ID: %d\nHash: %s\nData: %s",
                block.getId(), block.getHash(), block.getData());
        String basicQrContent = String.format("Block ID: %d\nHash: %s\nData: %s",
                block.getId(), block.getHash(), block.getData());
        try {
            String verificationUrl = verificationBaseUrl + "/verify/" + block.getId();

            QRCodeDTO qrData = QRCodeDTO.builder()
                    .basicInfo(basicQrContent)
                    .productId(block.getId().toString())
                    .productName(extractProductName(block.getData()))
                    .producer(extractProducer(block.getData()))
                    .productionDate(block.getTimestamp())
                    .certifications(extractCertifications(block.getData()))
                    .transportConditions(extractTransportInfo(block.getData()))
                    .retailerInfo(extractRetailerInfo(block.getData()))
                    .verificationUrl(verificationUrl)
                    .build();

            String jsonData = objectMapper.writeValueAsString(qrData);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(jsonData, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("Erreur lors de la génération du QR code", e);
            throw new RuntimeException("Impossible de générer le QR code", e);
        }
    }

    private String extractProductName(String data) {
        try {
            JsonNode jsonNode = objectMapper.readTree(data);
            return jsonNode.path("productName").asText("N/A");
        } catch (Exception e) {
            log.error("Erreur lors de l'extraction du nom du produit", e);
            return "N/A";
        }
    }

    private String extractProducer(String data) {
        try {
            JsonNode jsonNode = objectMapper.readTree(data);
            JsonNode producer = jsonNode.path("producer");
            return String.format("%s (%s)",
                    producer.path("name").asText("N/A"),
                    producer.path("location").asText("N/A")
            );
        } catch (Exception e) {
            log.error("Erreur lors de l'extraction des informations du producteur", e);
            return "N/A";
        }
    }

    private String extractCertifications(String data) {
        try {
            JsonNode jsonNode = objectMapper.readTree(data);
            JsonNode certs = jsonNode.path("certifications");
            StringBuilder result = new StringBuilder();
            certs.forEach(cert ->
                    result.append(cert.asText()).append(", ")
            );
            return result.length() > 0 ?
                    result.substring(0, result.length() - 2) :
                    "Aucune certification";
        } catch (Exception e) {
            log.error("Erreur lors de l'extraction des certifications", e);
            return "N/A";
        }
    }

    private String extractTransportInfo(String data) {
        try {
            JsonNode jsonNode = objectMapper.readTree(data);
            JsonNode transport = jsonNode.path("transport");
            return String.format("Température: %s°C, Humidité: %s%%",
                    transport.path("temperature").asText("N/A"),
                    transport.path("humidity").asText("N/A")
            );
        } catch (Exception e) {
            log.error("Erreur lors de l'extraction des conditions de transport", e);
            return "N/A";
        }
    }

    private String extractRetailerInfo(String data) {
        try {
            JsonNode jsonNode = objectMapper.readTree(data);
            JsonNode retailer = jsonNode.path("retailer");
            return String.format("%s - %s",
                    retailer.path("name").asText("N/A"),
                    retailer.path("location").asText("N/A")
            );
        } catch (Exception e) {
            log.error("Erreur lors de l'extraction des informations du détaillant", e);
            return "N/A";
        }
    }
}
