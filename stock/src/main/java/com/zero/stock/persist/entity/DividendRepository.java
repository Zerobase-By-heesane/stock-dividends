package com.zero.stock.persist.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DividendRepository extends JpaRepository<DividendEntity,Long> {
    List<DividendEntity> findByCompanyId(Long companyId);
}
