package com.hachicore.sellbook.dto;

import com.hachicore.sellbook.domain.BookStore;
import com.hachicore.sellbook.domain.SecondPrice;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class SecondPriceDto {

    private Long id;
    private BookStore site;
    private Integer gradeA;
    private Integer gradeB;
    private Integer gradeC;
    private LocalDate createdAt;

    public SecondPriceDto(SecondPrice secondPrice) {
        this.id = secondPrice.getId();
        this.site = secondPrice.getSite();
        this.gradeA = secondPrice.getGradeA();
        this.gradeB = secondPrice.getGradeB();
        this.gradeC = secondPrice.getGradeC();
        this.createdAt = secondPrice.getCreatedAt();
    }

}
