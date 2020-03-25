package com.hachicore.sellbook.repository;

import com.hachicore.sellbook.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
