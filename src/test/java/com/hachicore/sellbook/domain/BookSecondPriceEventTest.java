package com.hachicore.sellbook.domain;

import com.hachicore.sellbook.config.JpaConfig;
import com.hachicore.sellbook.repository.BookRepository;
import com.hachicore.sellbook.repository.SecondPriceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@Import(JpaConfig.class)
class BookSecondPriceEventTest {

    @Autowired BookRepository bookRepository;
    @Autowired SecondPriceRepository secondPriceRepository;

    @AfterEach
    void afterEach() {
        secondPriceRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @DisplayName("중고 가격 저장 시 해당 책 updateAt 반영")
    @Test
    void updateAtTest() {
        Book book = Book.builder()
                .title("테스트")
                .isbn("123456789")
                .publisher("테스트")
                .author("테스트")
                .price(10000)
                .build();

        Book savedBook = bookRepository.save(book);
        assertNull(savedBook.getUpdatedAt());

        SecondPrice secondPrice = SecondPrice.builder()
                .site(BookStore.ALADIN)
                .gradeA(5000)
                .gradeB(3000)
                .gradeC(1000)
                .build();

        secondPrice.addBook(book);

        SecondPrice savedSecondPrice = secondPriceRepository.save(secondPrice);

        savedBook = bookRepository.findById(savedBook.getId()).get();

        assertEquals(savedSecondPrice.getCreatedAt(), savedBook.getUpdatedAt());
    }

}