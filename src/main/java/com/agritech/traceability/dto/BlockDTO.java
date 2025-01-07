package com.agritech.traceability.dto;

import com.agritech.traceability.entities.NodeType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
public class BlockDTO {
    private Long id;
    private String hash;
    private String previousHash;
    private LocalDateTime timestamp;
    private NodeType nodeType;
    private String data;
//    @Getter
    private int nonce;

}