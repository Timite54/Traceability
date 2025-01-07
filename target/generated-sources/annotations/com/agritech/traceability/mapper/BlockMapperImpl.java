package com.agritech.traceability.mapper;

import com.agritech.traceability.dto.BlockDTO;
import com.agritech.traceability.entities.Block;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-07T14:24:08+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class BlockMapperImpl implements BlockMapper {

    @Override
    public BlockDTO toDTO(Block block) {
        if ( block == null ) {
            return null;
        }

        BlockDTO blockDTO = new BlockDTO();

        blockDTO.setId( block.getId() );
        blockDTO.setHash( block.getHash() );
        blockDTO.setPreviousHash( block.getPreviousHash() );
        blockDTO.setTimestamp( block.getTimestamp() );
        blockDTO.setNodeType( block.getNodeType() );
        blockDTO.setData( block.getData() );
        blockDTO.setNonce( block.getNonce() );

        return blockDTO;
    }

    @Override
    public Block toEntity(BlockDTO blockDTO) {
        if ( blockDTO == null ) {
            return null;
        }

        Block block = new Block();

        block.setId( blockDTO.getId() );
        block.setPreviousHash( blockDTO.getPreviousHash() );
        block.setNodeType( blockDTO.getNodeType() );
        block.setData( blockDTO.getData() );

        return block;
    }
}
