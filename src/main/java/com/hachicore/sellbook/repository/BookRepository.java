package com.hachicore.sellbook.repository;

import com.hachicore.sellbook.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByIsbnIn(Iterable<String> isbnList);

}
