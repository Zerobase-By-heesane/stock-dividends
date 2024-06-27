package com.zero.stock.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
public class Dividend {
    private String dividend;
    private LocalDateTime date;

    @Builder
    public Dividend(String dividend, LocalDateTime date){
        this.dividend = dividend;
        this.date = date;
    }
}
