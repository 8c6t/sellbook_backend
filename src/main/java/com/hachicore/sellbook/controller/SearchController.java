package com.hachicore.sellbook.controller;

import com.hachicore.sellbook.dto.BookDto;
import com.hachicore.sellbook.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search/{query}")
    public ResponseEntity search(@PathVariable String query, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<BookDto> search = searchService.search(query, pageable);
        return ResponseEntity.ok(search);
    }

}
