package com.hachicore.sellbook.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
@ToString(exclude = "book")
@EntityListeners(AuditingEntityListener.class)
public class SecondPrice {

    @Id @GeneratedValue
    @Column(name = "SECOND_PRICE_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    private BookStore site;

    private Integer gradeA;

    private Integer gradeB;

    private Integer gradeC;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    public void addBook(Book book) {
        this.book = book;
        book.getSecondPrices().add(this);
    }

}
