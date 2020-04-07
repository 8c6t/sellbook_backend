package com.hachicore.sellbook.repository;

import com.hachicore.sellbook.domain.Storage;
import com.hachicore.sellbook.domain.StorageBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StorageBookRepository extends JpaRepository<StorageBook, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM StorageBook sb WHERE sb.storage = :storage AND sb.book.id IN :bookIds")
    void deleteBooks(Storage storage, List<Long> bookIds);

}
