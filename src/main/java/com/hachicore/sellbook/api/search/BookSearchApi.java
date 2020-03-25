package com.hachicore.sellbook.api.search;

import com.hachicore.sellbook.api.search.dto.SearchApiResponse;
import org.springframework.data.domain.Pageable;

public interface BookSearchApi {
    SearchApiResponse search(String query, Pageable pageable);
}
