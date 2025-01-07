package com.agritech.traceability.dto.request;

import com.agritech.traceability.dto.BlockDTO;
import com.agritech.traceability.util.HashUtil;

public class MineBlockRequest {
    private int id;
    private String previousHash;
    private BlockDTO blockDto;
        public String calculateHash() {
        return HashUtil.sha256(
                previousHash +
                blockDto.getTimestamp().toString() +
                        Integer.toString(blockDto.getNonce()) + blockDto.getData()
        );
    }

        public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!blockDto.getHash().substring(0, difficulty).equals(target)) {
            blockDto.setNonce(blockDto.getNonce() + 1);
            blockDto.setHash(calculateHash());
        }
    }
}
