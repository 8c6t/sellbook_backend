package com.hachicore.sellbook.dto;

import com.hachicore.sellbook.domain.Book;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class BookDto {

    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private Integer price;
    private String image;
    private List<SecondPriceDto> secondPrices;

    public BookDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.isbn = book.getIsbn();
        this.price = book.getPrice();
        this.image = book.getImage();
        this.secondPrices = book.getSecondPrices().stream()
                .map(SecondPriceDto::new)
                .collect(Collectors.toList());
    }
}
