package com.hachicore.sellbook.api.search.aladin.dto;

import com.hachicore.sellbook.api.search.dto.BookResponse;
import com.hachicore.sellbook.domain.Book;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AladinBookResponse implements BookResponse {

    private String title;
    private String author;
    private String publisher;
    private String isbn13;
    private Integer priceStandard;
    private String cover;

    @Override
    public Book toEntity() {
        return Book.builder()
                .title(title)
                .author(author)
                .publisher(publisher)
                .isbn(isbn13)
                .price(priceStandard)
                .image(cover)
                .build()
        ;
    }

}
