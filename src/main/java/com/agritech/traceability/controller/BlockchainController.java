package com.agritech.traceability.controller;


import com.agritech.traceability.dto.BlockDTO;
import com.agritech.traceability.dto.DistributorData;
import com.agritech.traceability.dto.request.CreateBlockRequest;
import com.agritech.traceability.entities.NodeType;
import com.agritech.traceability.service.BlockchainServices;
import com.agritech.traceability.util.QRCodeGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blockchain")
@RequiredArgsConstructor
@Tag(name = "Blockchain", description = "API de gestion de la blockchain agricole")
public class BlockchainController {
    private final BlockchainServices blockchainServices;
    private final QRCodeGenerator qrCodeGenerator;

    @GetMapping
    @Operation(summary = "Récupérer tous les blocs", description = "Retourne la liste complète des blocs de la blockchain")
    public ResponseEntity<List<BlockDTO>> getAllBlocks() {
        return ResponseEntity.ok(blockchainServices.getAllBlocks());
    }
    @PostMapping
    @Operation(summary = "Créer un nouveau bloc (PRODUCER uniquement)")
    public ResponseEntity<BlockDTO> addBlock(@Valid @RequestBody CreateBlockRequest request) throws JsonProcessingException {
        return ResponseEntity.ok(blockchainServices.addBlock(request));
    }

//    @PostMapping
//    @Operation(summary = "Ajouter un nouveau bloc", description = "Crée et mine un nouveau bloc dans la blockchain")
//    public ResponseEntity<BlockDTO> addBlock(@Valid @RequestBody CreateBlockRequest request) throws JsonProcessingException {
//        return ResponseEntity.ok(blockchainServices.addBlock(request));
//    }

    @GetMapping("/validate")
    @Operation(summary = "Valider la blockchain", description = "Vérifie l'intégrité de la blockchain")
    public ResponseEntity<Boolean> validateChain() {
        return ResponseEntity.ok(blockchainServices.isChainValid());
    }

//    @GetMapping(value = "/qr/{blockId}", produces = MediaType.IMAGE_PNG_VALUE)
//    @Operation(summary = "Générer un QR code", description = "Génère un QR code pour un bloc spécifique")
//    public ResponseEntity<byte[]> generateQRCode(@PathVariable Long blockId) {
//        BlockDTO block = blockchainServices.getAllBlocks().stream()
//                .filter(b -> b.getId().equals(blockId))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("Bloc non trouvé"));
//
//        byte[] qrCode = qrCodeGenerator.generateQRCode(block,250,250);
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.IMAGE_PNG)
//                .body(qrCode);
//    }

    @PostMapping("/{blockId}/distributor")
    @Operation(summary = "Mettre à jour un bloc par un distributeur")
    public ResponseEntity<BlockDTO> updateBlockByDistributor(
            @PathVariable Long blockId,
            @Valid @RequestBody DistributorData distributorData) throws JsonProcessingException {
        return ResponseEntity.ok(blockchainServices.updateBlockByDistributor(blockId, distributorData));
    }

    @PostMapping("/mine/{blockId}")
    @Operation(summary = "Miner un bloc existant",
            description = "Mine un bloc spécifique de la blockchain avec la difficulté actuelle")
    public ResponseEntity<BlockDTO> mineBlock(@PathVariable Long blockId) {
        return ResponseEntity.ok(blockchainServices.mineExistingBlock(blockId));
    }

    @GetMapping(value = "/qr/{blockId}", produces = MediaType.IMAGE_PNG_VALUE)
    @Operation(summary = "Générer un QR code (RETAILER uniquement)")
    public ResponseEntity<byte[]> generateQRCode(
            @PathVariable Long blockId,
            @RequestParam NodeType requestingNode) throws JsonProcessingException {
        byte[] qrCode = blockchainServices.generateQRCode(blockId, requestingNode);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCode);
    }
}