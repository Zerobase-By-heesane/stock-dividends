package com.zero.stock.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class Company {
    private String ticker;
    private String name;

    @Builder
    public Company(String ticker, String name){
        this.ticker = ticker;
        this.name = name;
    }
}
