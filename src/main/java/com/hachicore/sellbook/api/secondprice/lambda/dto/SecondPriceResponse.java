package com.hachicore.sellbook.api.secondprice.lambda.dto;

import com.hachicore.sellbook.domain.BookStore;
import com.hachicore.sellbook.domain.SecondPrice;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class SecondPriceResponse {

    private String isbn13;
    private BookStore site;
    private Integer gradeA;
    private Integer gradeB;
    private Integer gradeC;

    public SecondPrice toEntity() {
        return SecondPrice.builder()
                .site(site)
                .gradeA(gradeA)
                .gradeB(gradeB)
                .gradeC(gradeC)
                .build();
    }
}
