package com.zero.stock.service;

import com.zero.stock.model.Company;
import com.zero.stock.model.Dividend;
import com.zero.stock.model.ScrapedResult;
import com.zero.stock.persist.entity.CompanyEntity;
import com.zero.stock.persist.entity.CompanyRepository;
import com.zero.stock.persist.entity.DividendEntity;
import com.zero.stock.persist.entity.DividendRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public ScrapedResult getDividendsByCompanyName(String companyName){
        // 1. 회사명을 기준으로 회사 정보를 조회
        CompanyEntity companyEntity = this.companyRepository.findByName(companyName).orElseThrow(
                () -> new RuntimeException("Not found company -> "+ companyName)
        );

        // 2. 조회한 회사 정보를 기반으로 배당금 정보를 조회
        List<Dividend> dividendEntities = this.dividendRepository.findByCompanyId(companyEntity.getId())
                .stream()
                .map(dividendEntity -> Dividend.builder()
                    .dividend(dividendEntity.getDividend())
                    .date(dividendEntity.getDate())
                    .build())
                .collect(Collectors.toList());

        // 3. 조회한 배당금 정보를 ScrapedResult 로 변환하여 반환

        return new ScrapedResult(Company.builder()
                .name(companyEntity.getName())
                .ticker(companyEntity.getTicker())
                .build(), dividendEntities);
    }


}
