package com.agritech.traceability.entities;

import com.agritech.traceability.dto.request.MineBlockRequest;
import com.agritech.traceability.util.HashUtil;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

//@Data
@Getter @Setter @EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "blocks")
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String hash;
    private String previousHash;
    private LocalDateTime timestamp;
    private int nonce;

    @Enumerated(EnumType.STRING)
    private NodeType nodeType;

    @Column(columnDefinition = "TEXT")
    private String data;

    public Block(String data, NodeType nodeType) {
        this.previousHash = previousHash;
        this.data = data;
        this.nodeType = nodeType;
        this.timestamp = LocalDateTime.now();
//        this.hash = calculateHash();
    }

    public String calculateHash() {
        return HashUtil.sha256(
                previousHash +
                        timestamp.toString() +
                        Integer.toString(nonce) +
                        data
        );
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
    }
}