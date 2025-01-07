package com.agritech.traceability.repository;

import com.agritech.traceability.entities.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {
    /* Ancienne version sans recherche des blocs suivants
    @Query("SELECT b FROM Block b WHERE b.hash = ?1")
    Optional<Block> findByHash(String hash);

    @Query("SELECT b FROM Block b ORDER BY b.id DESC LIMIT 1")
    Optional<Block> findLastBlock();
    */

    // Nouvelle version avec recherche des blocs suivants
    @Query("SELECT b FROM Block b WHERE b.hash = ?1")
    Optional<Block> findByHash(String hash);

    @Query("SELECT b FROM Block b ORDER BY b.id DESC LIMIT 1")
    Optional<Block> findLastBlock();

    @Query("SELECT b FROM Block b WHERE b.id > :blockId ORDER BY b.id")
    List<Block> findBlocksAfter(@Param("blockId") Long blockId);
}
