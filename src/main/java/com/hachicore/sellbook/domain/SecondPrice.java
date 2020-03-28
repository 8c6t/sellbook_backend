package com.hachicore.sellbook.domain;

import com.hachicore.sellbook.domain.event.SecondPriceSavedEvent;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
@ToString(exclude = "book")
@EntityListeners(AuditingEntityListener.class)
public class SecondPrice extends AbstractAggregateRoot<SecondPrice> {

    @Id @GeneratedValue
    @Column(name = "SECOND_PRICE_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    private BookStore site;

    private Integer gradeA;

    private Integer gradeB;

    private Integer gradeC;

    @CreatedDate
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    public void addBook(Book book) {
        this.book = book;
        book.getSecondPrices().add(this);

        this.registerEvent(new SecondPriceSavedEvent(this));
    }

}
