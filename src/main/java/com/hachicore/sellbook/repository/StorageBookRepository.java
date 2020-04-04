package com.hachicore.sellbook.repository;

import com.hachicore.sellbook.domain.StorageBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageBookRepository extends JpaRepository<StorageBook, Long> {

}
