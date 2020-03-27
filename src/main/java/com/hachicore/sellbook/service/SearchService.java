package com.hachicore.sellbook.service;

import com.hachicore.sellbook.api.search.BookSearchApi;
import com.hachicore.sellbook.api.secondprice.SecondPriceParseApi;
import com.hachicore.sellbook.dto.BookDto;
import com.hachicore.sellbook.repository.BookRepository;
import com.hachicore.sellbook.repository.SecondPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchService {

    private final BookSearchApi bookSearchApi;
    private final SecondPriceParseApi priceParseApi;

    private final BookRepository bookRepository;
    private final SecondPriceRepository secondPriceRepository;

    // TODO: 2020.03.27. search 구현
    /* [책 저장 흐름]
     * 1. API 호출
     * 2. DB 내 유무 조회
     * 3. DB에 없는 책만 저장
     * ========================
     * [중고 가격 저장 흐름]
     * 4. DB 조회(당일 기준)
     * 5. DB에 없으면 API 호출
     * 6. API 결과 저장
     * =========================
     * 7. 결과 Dto 변환 후 리턴
     * */
    @Transactional
    public Page<BookDto> search(String query, Pageable pageable) {
        // TODO 2020.03.27 책 저장 로직 구현
        // TODO 2020.03.27 중고 가격 저장 로직 구현
        return null;
    }

}
