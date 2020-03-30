package com.hachicore.sellbook.repository;

import com.hachicore.sellbook.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByIsbnIn(Iterable<String> isbnList);

    @Query("SELECT DISTINCT b FROM Book b LEFT OUTER JOIN b.secondPrices s ON s.createdAt >= CURRENT_DATE WHERE b.isbn IN :isbnList")
    List<Book> findWithTodaySecondPrice(Iterable<String> isbnList);
}
