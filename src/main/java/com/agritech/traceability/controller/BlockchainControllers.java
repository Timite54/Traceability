//package com.agritech.traceability.controller;
//
//import com.agritech.traceability.dto.BlockDTO;
//import com.agritech.traceability.dto.request.CreateBlockRequest;
//import com.agritech.traceability.service.BlockchainService;
//import com.agritech.traceability.util.QRCodeGenerator;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//import static java.lang.String.format;
//
//@RestController
//@RequestMapping("/api/blockchain")
//@RequiredArgsConstructor
//@Tag(name = "Blockchain", description = "API de gestion de la blockchain agricole")
//public class BlockchainControllers {
//    private final BlockchainService blockchainService;
//    private final QRCodeGenerator qrCodeGenerator;
//
//    @GetMapping
//    @Operation(summary = "Récupérer tous les blocs", description = "Retourne la liste complète des blocs de la blockchain")
//    public ResponseEntity<List<BlockDTO>> getAllBlocks() {
//        return ResponseEntity.ok(blockchainService.getAllBlocks());
//    }
//
//    @PostMapping
//    @Operation(summary = "Ajouter un nouveau bloc", description = "Crée et mine un nouveau bloc dans la blockchain")
//    public ResponseEntity<BlockDTO> addBlock(@Valid @RequestBody CreateBlockRequest request) {
//        return ResponseEntity.ok(blockchainService.addBlock(request));
//    }
//
//    @GetMapping("/validate")
//    @Operation(summary = "Valider la blockchain", description = "Vérifie l'intégrité de la blockchain")
//    public ResponseEntity<Boolean> validateChain() {
//        return ResponseEntity.ok(blockchainService.isChainValid());
//    }
//
//    @GetMapping(value = "/qr/{blockId}", produces = MediaType.IMAGE_PNG_VALUE)
//    @Operation(summary = "Générer un QR code", description = "Génère un QR code pour un bloc spécifique")
//    public ResponseEntity<byte[]> generateQRCode(@PathVariable Long blockId) {
//        BlockDTO block = blockchainService.getAllBlocks().stream()
//            .filter(b -> b.getId().equals(blockId))
//            .findFirst()
//            .orElseThrow(() -> new RuntimeException("Bloc non trouvé"));
//
////        byte[] qrCode = qrCodeGenerator.generateQRCode( block,
////            250,
////            250
////        );
//        byte[] qrCode = qrCodeGenerator.generateQRCode(block,
//                250,
//                250
//        );
//
//        return ResponseEntity.ok()
//            .contentType(MediaType.IMAGE_PNG)
//            .body(qrCode);
//    }
//
//    @PostMapping("/mine/{blockId}")
//    @Operation(summary = "Miner un bloc existant",
//            description = "Mine un bloc spécifique de la blockchain avec la difficulté actuelle")
//    public ResponseEntity<BlockDTO> mineBlock(@PathVariable Long blockId) {
//        return ResponseEntity.ok(blockchainService.mineExistingBlock(blockId));
//    }
//}