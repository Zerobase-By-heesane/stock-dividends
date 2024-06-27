package com.zero.stock.scraper;

import com.zero.stock.model.Company;
import com.zero.stock.model.ScrapedResult;
import org.springframework.stereotype.Component;

@Component
public class NaverFinanceScrapper implements Scrapper{

    @Override
    public Company scrapCompanyByTicker(String ticker) {
        return null;
    }

    @Override
    public ScrapedResult scrap(Company company) {
        return null;
    }
}
