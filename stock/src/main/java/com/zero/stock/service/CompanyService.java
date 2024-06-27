package com.zero.stock.service;

import com.zero.stock.model.ScrapedResult;
import com.zero.stock.persist.entity.CompanyEntity;
import com.zero.stock.persist.entity.CompanyRepository;
import com.zero.stock.persist.entity.DividendEntity;
import com.zero.stock.persist.entity.DividendRepository;
import com.zero.stock.scraper.Scrapper;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.zero.stock.model.Company;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final Scrapper scrapper;

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public Company save(String ticker){
        // DB에 이미 존재하는 회사인지 확인

        // 존재하면,
        if(this.companyRepository.existsByTicker(ticker)){
            throw new RuntimeException("Already exists ticker -> "+ ticker);
        }

        return this.storeCompanyAndDividend(ticker);
    }

    private Company storeCompanyAndDividend(String ticker){
        // Ticker를 기준으로 회사를 스크래핑
        Company company = this.scrapper.scrapCompanyByTicker(ticker);
        if(ObjectUtils.isEmpty(company)){
            throw new RuntimeException("Failed to scrap ticker -> "+ ticker);
        }
        // 해당 회사가 존재할 경우, 해당 회사의 배당금 정보 스크래핑
        ScrapedResult scrapResult = this.scrapper.scrap(company);

        // 스크래핑한 정보를 저장
        CompanyEntity companyEntity = this.companyRepository.save(new CompanyEntity(company));

        List<DividendEntity> dividendEntities = scrapResult.getDividends().stream()
                                                    .map(dividend -> new DividendEntity(dividend, companyEntity.getId()))
                                                    .collect(Collectors.toList());

        this.dividendRepository.saveAll(dividendEntities);
        return company;
    }

    public Page<CompanyEntity> getAllCompanies(Pageable pageable){
        return this.companyRepository.findAll(pageable);
    }
}
