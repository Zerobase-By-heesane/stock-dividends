package com.zero.stock.persist.entity;

import com.zero.stock.model.Dividend;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "dividend")
@Getter
@ToString
@NoArgsConstructor
public class DividendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long companyId;

    private LocalDateTime date;

    private String dividend;

    public DividendEntity(Dividend dividend, Long companyId) {
        this.dividend = dividend.getDividend();
        this.date = dividend.getDate();
        this.companyId = companyId;
    }
}
