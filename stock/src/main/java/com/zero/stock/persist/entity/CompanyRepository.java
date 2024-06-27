package com.zero.stock.persist.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity,Long> {
    CompanyEntity findByTicker(String ticker);
    boolean existsByTicker(String ticker);

    Optional<CompanyEntity> findByName(String name);
}
