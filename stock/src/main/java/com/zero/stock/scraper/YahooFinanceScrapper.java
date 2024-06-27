package com.zero.stock.scraper;

import com.zero.stock.model.Company;
import com.zero.stock.model.Dividend;
import com.zero.stock.model.ScrapedResult;
import com.zero.stock.model.constant.Month;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Primary
@Component
public class YahooFinanceScrapper implements Scrapper{

    private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history/?frequency=1mo&period1=%d&period2=%d&filter=div";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";

    private static final long START_TIME = 86400;

    @Override
    public ScrapedResult scrap(Company company) {

        ScrapedResult scrapedResult = new ScrapedResult();
        scrapedResult.setCompany(company);

        String url = String.format(STATISTICS_URL, company.getTicker(), START_TIME, System.currentTimeMillis() / 1000);

        Connection connect = Jsoup.connect(url);

        try {
            Document document = connect.get();

            Elements parsedDivs = document.getElementsByTag("tr");

            List<Dividend> dividendList = new ArrayList<>();

            for (int i = 1; i < parsedDivs.size(); i++) {
                String[] dividends = parsedDivs.get(i).text().split(" ");

                int year = Integer.parseInt(dividends[2]);
                int month = Month.getNumber(dividends[0]);
                int day = Integer.parseInt(dividends[1].replace(",",""));
                String dividend = dividends[3];

                if(month < 0){
                    throw new RuntimeException("Unexpected Month enum value -> " + dividends[0]);
                }

                dividendList.add( Dividend.builder()
                        .date(LocalDateTime.of(year, month, day, 0, 0, 0))
                        .dividend(dividend)
                        .build());
            }
            scrapedResult.setDividends(dividendList);

        } catch (IOException e) {
            // TODO
            throw new RuntimeException(e);
        }
        return scrapedResult;
    }

    @Override
    public Company scrapCompanyByTicker(String ticker){
        Connection connection = Jsoup.connect(String.format(SUMMARY_URL, ticker, ticker));
        try {
            Document document = connection.get();
            Element titleEle = document.getElementsByClass("svelte-3a2v0c").get(0);
            String title = titleEle.text().split(" ")[0];
            System.out.println(title);

            return Company.builder()
                    .ticker(ticker)
                    .name(title)
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
