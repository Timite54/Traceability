package com.agritech.traceability.mapper;

import com.agritech.traceability.entities.Block;
import com.agritech.traceability.dto.BlockDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BlockMapper {
    BlockDTO toDTO(Block block);

    @Mapping(target = "hash", ignore = true)
    @Mapping(target = "nonce", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    Block toEntity(BlockDTO blockDTO);
}