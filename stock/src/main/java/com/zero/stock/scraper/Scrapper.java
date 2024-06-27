package com.zero.stock.scraper;

import com.zero.stock.model.Company;
import com.zero.stock.model.ScrapedResult;
import org.springframework.stereotype.Component;

@Component
public interface Scrapper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
