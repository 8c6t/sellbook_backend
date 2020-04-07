package com.hachicore.sellbook.repository;

import com.hachicore.sellbook.domain.Book;
import com.hachicore.sellbook.domain.Storage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByIsbnIn(Iterable<String> isbnList);

    @Query("SELECT b FROM Book b INNER JOIN b.storageBooks sb ON sb.storage = :storage")
    Page<Book> findBooksInStorage(Storage storage, Pageable pageable);

}
