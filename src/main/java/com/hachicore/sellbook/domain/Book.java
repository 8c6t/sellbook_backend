package com.hachicore.sellbook.domain;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @EqualsAndHashCode(of = "isbn")
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

    private LocalDate updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @BatchSize(size = 100)
    private List<SecondPrice> secondPrices = new ArrayList<>();

    public void updateSecondPriceDate() {
        this.updatedAt = LocalDate.now();
    }

}
