package com.hachicore.sellbook.api.search.dto;

import com.hachicore.sellbook.domain.Book;

public interface BookResponse {
    Book toEntity();
}
