package com.hachicore.sellbook.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
@ToString(exclude = "secondPrices")
public class Book {

    @Id @GeneratedValue
    @Column(name = "BOOK_ID")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(unique = true, nullable = false)
    private String isbn;

    private String author;

    private String publisher;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String image;

    private Integer price;

    @Builder.Default
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<SecondPrice> secondPrices = new ArrayList<>();

}
