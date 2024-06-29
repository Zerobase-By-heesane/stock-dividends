package com.zero.stock.scheduler;

import com.zero.stock.model.Company;
import com.zero.stock.model.ScrapedResult;
import com.zero.stock.persist.entity.CompanyEntity;
import com.zero.stock.persist.entity.CompanyRepository;
import com.zero.stock.persist.entity.DividendEntity;
import com.zero.stock.persist.entity.DividendRepository;
import com.zero.stock.scraper.Scrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ScrapperScheduler {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final Scrapper scrapper;

    @Scheduled(fixedDelay = 1000)
    public void test1() throws InterruptedException {
        Thread.sleep(10000);
        log.info(Thread.currentThread().getName() + "test1 -> " + LocalDateTime.now());

    }

    @Scheduled(fixedDelay = 1000)
    public void test2() {
        log.info(Thread.currentThread().getName() + "test2 -> " + LocalDateTime.now());
    }

    //    @Scheduled(cron = "${scheduler.scrap.yahoo.cron}")
    public void yahooFinanceScheduling() {
        log.info("Yahoo Finance Scrapper Scheduler Start");
        // 저장된 회사 목록 조회
        List<CompanyEntity> companyList = companyRepository.findAll();

        log.info("Company List Size : " + companyList.size());

        // 회사마다 배당금 정보를 새로 스크래핑
        for (CompanyEntity company : companyList) {
            ScrapedResult scrapResult = this.scrapper.scrap(
                    Company.builder()
                            .ticker(company.getTicker())
                            .name(company.getName())
                            .build()
            );

            scrapResult.getDividends().stream()
                    .map(e -> new DividendEntity(e, company.getId()))
                    .forEach(e -> {
                        System.out.println(e);
                        boolean exists = this.dividendRepository.existsByCompanyIdAndDate(e.getId(), e.getDate());
                        if (!exists) {
                            this.dividendRepository.save(e);
                        }
                    });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }

        // 스크래핑한 배당금 정보 중 데이터베이스에 없는 값은 저장
    }
}
