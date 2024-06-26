package com.zero.stock.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Dividend {
    private String dividend;
    private LocalDateTime date;
}
