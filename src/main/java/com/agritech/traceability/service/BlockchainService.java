//package com.agritech.traceability.service;
//
//import com.agritech.traceability.dto.BlockDTO;
//import com.agritech.traceability.dto.request.CreateBlockRequest;
//import com.agritech.traceability.dto.anciens.UpdateBlockRequest;
//import com.agritech.traceability.entities.Block;
//import com.agritech.traceability.entities.NodeType;
//import com.agritech.traceability.exception.BlockchainException;
//import com.agritech.traceability.mapper.BlockMapper;
//import com.agritech.traceability.repository.BlockRepository;
//import com.agritech.traceability.util.QRCodeGenerator;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class BlockchainService {
//    private static final int MINING_DIFFICULTY = 4;
//    private final BlockRepository blockRepository;
//    private final BlockMapper blockMapper;
//    private final ObjectMapper objectMapper;
//    private final QRCodeGenerator qrCodeGenerator;
//
//    @Transactional
//    public BlockDTO addBlock(CreateBlockRequest request) throws JsonProcessingException {
//        if (request.getNodeType() != NodeType.PRODUCER) {
//            throw new BlockchainException("Seul un PRODUCER peut créer un nouveau bloc");
//        }
//
//        String previousHash = blockRepository.findLastBlock()
//                .map(Block::getHash)
//                .orElse("0");
//
//        // Création du JSON des données
//        Map<String, Object> blockData = new HashMap<>();
//        blockData.put("productName", request.getProductName());
//        blockData.put("producer", request.getProducerData());
//        blockData.put("createdBy", NodeType.PRODUCER);
//        blockData.put("status", "CREATED");
//
//        String jsonData = objectMapper.writeValueAsString(blockData);
//
//        Block newBlock = new Block(previousHash, jsonData, request.getNodeType());
//        newBlock.mineBlock(MINING_DIFFICULTY);
//        newBlock.setStatus("MINED");
//
//        return blockMapper.toDTO(blockRepository.save(newBlock));
//    }
//
//    @Transactional
//    public BlockDTO updateBlockByDistributor(Long blockId, UpdateBlockRequest request) throws JsonProcessingException {
//        if (request.getNodeType() != NodeType.DISTRIBUTOR) {
//            throw new BlockchainException("Seul un DISTRIBUTOR peut mettre à jour ce bloc");
//        }
//
//        Block block = blockRepository.findById(blockId)
//                .orElseThrow(() -> new BlockchainException("Bloc non trouvé"));
//
//        if (block.getNodeType() != NodeType.PRODUCER) {
//            throw new BlockchainException("Ce bloc n'a pas été créé par un PRODUCER");
//        }
//
//        if (!block.getStatus().equals("MINED")) {
//            throw new BlockchainException("Ce bloc n'a pas encore été miné par le PRODUCER");
//        }
//
//        // Mise à jour des données du bloc
//        Map<String, Object> existingData = objectMapper.readValue(block.getData(), Map.class);
//        existingData.put("distributor", request.getDistributorData());
//        existingData.put("lastUpdatedBy", NodeType.DISTRIBUTOR);
//        existingData.put("status", "UPDATED_BY_DISTRIBUTOR");
//
//        block.setData(objectMapper.writeValueAsString(existingData));
//        block.mineBlock(MINING_DIFFICULTY);
//
//        return blockMapper.toDTO(blockRepository.save(block));
//    }
//
//    @Transactional
//    public byte[] generateQRCode(Long blockId, NodeType requestingNode) {
//        if (requestingNode != NodeType.RETAILER) {
//            throw new BlockchainException("Seul un RETAILER peut générer un QR code");
//        }
//
//        Block block = blockRepository.findById(blockId)
//                .orElseThrow(() -> new BlockchainException("Bloc non trouvé"));
//
//        if (!block.getStatus().equals("UPDATED_BY_DISTRIBUTOR")) {
//            throw new BlockchainException("Ce bloc n'a pas encore été complètement traité et miné");
//        }
//        return qrCodeGenerator.generateQRCode(blockMapper.toDTO(block), 250, 250);
//    }
//}