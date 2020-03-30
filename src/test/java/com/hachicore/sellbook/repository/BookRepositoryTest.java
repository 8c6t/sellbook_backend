package com.hachicore.sellbook.repository;

import com.hachicore.sellbook.domain.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @BeforeEach
    void beforeEach() {
        for (int i = 0; i < 100; i++) {
            bookRepository.save(Book.builder()
                    .title("테스트" + i)
                    .isbn(String.valueOf(i))
                    .author("테스트" + i)
                    .publisher("테스트" + i)
                    .price(i)
                    .build());
        }
    }

    @AfterEach
    void afterEach() {
        bookRepository.deleteAll();
    }

    @DisplayName("ISBN 리스트 검색 - 모두 존재시")
    @Test
    void findAllByIsbnTest_all_exist() {
        List<String> isbnList = IntStream.range(0, 100)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());

        List<Book> searchResult = bookRepository.findAllByIsbnIn(isbnList);
        assertEquals(searchResult.size(), 100);
    }

    @DisplayName("ISBN 리스트 검색 - 일부 존재시")
    @Test
    void findAllByIsbnTest_some_exist() {
        List<String> isbnList = Arrays.asList("1", "2", "101");

        List<Book> searchResult = bookRepository.findAllByIsbnIn(isbnList);
        assertEquals(searchResult.size(), 2);
    }

    @DisplayName("ISBN 리스트 검색 - 미존재시")
    @Test
    void findAllByIsbnTest_no_exist() {
        List<String> isbnList = Arrays.asList("101", "102", "103");

        List<Book> searchResult = bookRepository.findAllByIsbnIn(isbnList);
        assertEquals(searchResult.size(), 0);
    }

}