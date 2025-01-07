package com.agritech.traceability.service;

import com.agritech.traceability.dto.BlockDTO;
import com.agritech.traceability.dto.DistributorData;
import com.agritech.traceability.dto.request.CreateBlockRequest;
import com.agritech.traceability.entities.Block;
import com.agritech.traceability.entities.NodeType;
import com.agritech.traceability.exception.BlockchainException;
import com.agritech.traceability.mapper.BlockMapper;
import com.agritech.traceability.repository.BlockRepository;
import com.agritech.traceability.util.QRCodeGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlockchainServices {
    private static final int MINING_DIFFICULTY = 4;

    private final BlockRepository blockRepository;
    private final BlockMapper blockMapper;
    private  final ObjectMapper objectMapper;
    private final QRCodeGenerator qrCodeGenerator;

    /* Ancienne version sans minage de bloc existant
    @Transactional(readOnly = true)
    public List<BlockDTO> getAllBlocks() {
        return blockRepository.findAll().stream()
            .map(blockMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public BlockDTO addBlock(CreateBlockRequest request) {
        String previousHash = blockRepository.findLastBlock()
            .map(Block::getHash)
            .orElse("0");

        Block newBlock = new Block(previousHash, request.getData(), request.getNodeType());
        newBlock.mineBlock(MINING_DIFFICULTY);

        return blockMapper.toDTO(blockRepository.save(newBlock));
    }

    @Transactional(readOnly = true)
    public boolean isChainValid() {
        List<Block> chain = blockRepository.findAll();

        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                throw new BlockchainException("Hash invalide pour le bloc " + i);
            }

            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                throw new BlockchainException("Lien de hash rompu entre les blocs " + (i-1) + " et " + i);
            }
        }

        return true;
    }
    */

    // Nouvelle version avec minage de bloc existant
    @Transactional(readOnly = true)
    public List<BlockDTO> getAllBlocks() {
        return blockRepository.findAll().stream()
                .map(blockMapper::toDTO)
                .collect(Collectors.toList());
    }

//    @Transactional
//    public BlockDTO addBlock(CreateBlockRequest request) {
//        String previousHash = blockRepository.findLastBlock()
//                .map(Block::getHash)
//                .orElse("0");
//
//        Block newBlock = new Block(request.getData(), request.getNodeType());
////        newBlock.mineBlock(MINING_DIFFICULTY);
//
//        return blockMapper.toDTO(blockRepository.save(newBlock));
//    }

    @Transactional
    public BlockDTO addBlock(CreateBlockRequest request) throws JsonProcessingException {
        String previousHash = blockRepository.findLastBlock()
                .map(Block::getHash)
                .orElse("0");

        Map<String, Object> blockData = new HashMap<>();
        blockData.put("productName", request.getProductName());
        blockData.put("producer", request.getProducerData());
        blockData.put("status", "PRODUCER_CREATED");

        Block newBlock = new Block(objectMapper.writeValueAsString(blockData), NodeType.PRODUCER);
//        newBlock.mineBlock(MINING_DIFFICULTY);
        blockData.put("status", "PRODUCER_MINED");
        newBlock.setData(objectMapper.writeValueAsString(blockData));

        return blockMapper.toDTO(blockRepository.save(newBlock));
    }

    @Transactional
    public BlockDTO updateBlockByDistributor(Long blockId, DistributorData distributorData) throws JsonProcessingException {
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new BlockchainException("Bloc non trouvé"));

        Map<String, Object> blockData = objectMapper.readValue(block.getData(), Map.class);

        if (!blockData.get("status").equals("PRODUCER_MINED")) {
            throw new BlockchainException("Ce bloc n'a pas encore été miné par le PRODUCER");
        }

        blockData.put("distributor", distributorData);
        blockData.put("status", "DISTRIBUTOR_UPDATED");

        block.setData(objectMapper.writeValueAsString(blockData));
        block.mineBlock(MINING_DIFFICULTY);

        blockData.put("status", "DISTRIBUTOR_MINED");
        block.setData(objectMapper.writeValueAsString(blockData));

        return blockMapper.toDTO(blockRepository.save(block));
    }

    public byte[] generateQRCode(Long blockId, NodeType requestingNode) throws JsonProcessingException {
        if (requestingNode != NodeType.RETAILER) {
            throw new BlockchainException("Seul un RETAILER peut générer un QR code");
        }

        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new BlockchainException("Bloc non trouvé"));

        Map<String, Object> blockData = objectMapper.readValue(block.getData(), Map.class);
        String status = (String) blockData.get("status");

        if (!status.equals("DISTRIBUTOR_MINED") && !status.equals("PRODUCER_MINED")) {
            throw new BlockchainException("Ce bloc n'a pas encore été complètement miné");
        }

//        return blockMapper.toDTO(blockRepository.save(block));
        BlockDTO blockDTO = blockMapper.toDTO(block);
        return qrCodeGenerator.generateQRCode(blockDTO, 250, 250);
    }

    @Transactional(readOnly = true)
    public boolean isChainValid() {
        List<Block> chain = blockRepository.findAll();

        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                throw new BlockchainException("Hash invalide pour le bloc " + i);
            }

            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                throw new BlockchainException("Lien de hash rompu entre les blocs " + (i-1) + " et " + i);
            }
        }

        return true;
    }

    @Transactional
    public BlockDTO mineExistingBlock(Long blockId) {
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new BlockchainException("Bloc non trouvé avec l'ID: " + blockId));

        block.setNonce(0);
        block.mineBlock(MINING_DIFFICULTY);
        updateSubsequentBlocks(block);

        return blockMapper.toDTO(blockRepository.save(block));
    }

    private void updateSubsequentBlocks(Block minedBlock) {
        List<Block> allBlocks = blockRepository.findAll();
        boolean updateNeeded = false;

        for (Block block : allBlocks) {
            if (updateNeeded) {
                block.setPreviousHash(blockRepository.findById(block.getId() - 1)
                        .map(Block::getHash)
                        .orElse(block.getPreviousHash()));
                block.setHash(block.calculateHash());
                blockRepository.save(block);
            }
            if (block.getId().equals(minedBlock.getId())) {
                updateNeeded = true;
            }
        }
    }
}

