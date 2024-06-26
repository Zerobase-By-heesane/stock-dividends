package com.zero.stock.service;

import com.zero.stock.exception.impl.AlreadyExistTickerException;
import com.zero.stock.exception.impl.FailedToScrapeException;
import com.zero.stock.exception.impl.NoCompanyException;
import com.zero.stock.model.ScrapedResult;
import com.zero.stock.persist.entity.CompanyEntity;
import com.zero.stock.persist.entity.CompanyRepository;
import com.zero.stock.persist.entity.DividendEntity;
import com.zero.stock.persist.entity.DividendRepository;
import com.zero.stock.scraper.Scrapper;
import lombok.RequiredArgsConstructor;

import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.zero.stock.model.Company;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {


    private final Trie trie;
    private final Scrapper scrapper;

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public Company save(String ticker){
        // DB에 이미 존재하는 회사인지 확인

        // 존재하면,
        if(this.companyRepository.existsByTicker(ticker)){
            throw new AlreadyExistTickerException();
        }

        return this.storeCompanyAndDividend(ticker);
    }

    private Company storeCompanyAndDividend(String ticker){
        // Ticker를 기준으로 회사를 스크래핑
        Company company = this.scrapper.scrapCompanyByTicker(ticker);
        if(ObjectUtils.isEmpty(company)){
            throw new FailedToScrapeException();
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

    public void addAutoCompleteKeyword(String keyword){
        this.trie.put(keyword, null);
    }

    public List<String> autoComplete(String keyword){
        return (List<String>) this.trie
                .prefixMap(keyword)
                .keySet()
                .stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    public void deleteAutoCompleteKeyword(String keyword){
        this.trie.remove(keyword);
    }

    public List<String> getCompanyNamesByKeyword(String keyword){
        Pageable limit = PageRequest.of(0,10);
        Page<CompanyEntity> companyEntities = this.companyRepository.findByNameStartingWithIgnoreCase(keyword,limit);
        return companyEntities.stream()
                .map(CompanyEntity::getName)
                .collect(Collectors.toList());
    }

    public String deleteCompany(String ticker) {
        CompanyEntity companyEntity = this.companyRepository.findByTicker(ticker).orElseThrow(NoCompanyException::new);

        this.dividendRepository.deleteAllByCompanyId(companyEntity.getId());
        this.companyRepository.delete(companyEntity);

        this.deleteAutoCompleteKeyword(companyEntity.getName());

        return companyEntity.getName();
    }
}
