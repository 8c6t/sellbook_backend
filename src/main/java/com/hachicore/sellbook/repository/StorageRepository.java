package com.hachicore.sellbook.repository;

import com.hachicore.sellbook.domain.Account;
import com.hachicore.sellbook.domain.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StorageRepository extends JpaRepository<Storage, Long> {

    Optional<Storage> findByAccount(Account account);

}
