package com.hachicore.sellbook.repository;

import com.hachicore.sellbook.domain.Book;
import com.hachicore.sellbook.domain.SecondPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SecondPriceRepository extends JpaRepository<SecondPrice, Long> {

    @Query("SELECT sp FROM SecondPrice sp INNER JOIN FETCH sp.book b WHERE b IN :bookList AND sp.createdAt >= CURRENT_DATE")
    List<SecondPrice> findTodayByBook(List<Book> bookList);

}
