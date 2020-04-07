package com.hachicore.sellbook.controller;

import com.hachicore.sellbook.config.security.account.CurrentUser;
import com.hachicore.sellbook.domain.Account;
import com.hachicore.sellbook.dto.BookDto;
import com.hachicore.sellbook.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/storage")
public class StorageController {

    private final StorageService storageService;

    @GetMapping
    public ResponseEntity storageList(@CurrentUser Account account,
                                      @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<BookDto> storageBookList = storageService.getStorageBookList(account, pageable);

        return ResponseEntity.ok(storageBookList);
    }

    @PostMapping
    public ResponseEntity addStorageBook(@CurrentUser Account account, @RequestBody List<Long> bookIds) {
        storageService.addBook(account, bookIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity removeStorageBook(@CurrentUser Account account, @RequestBody List<Long> bookIds) {
        storageService.removeBook(account, bookIds);
        return ResponseEntity.noContent().build();
    }

}
