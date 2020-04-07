package com.hachicore.sellbook.service;

import com.hachicore.sellbook.api.search.BookSearchApi;
import com.hachicore.sellbook.api.search.dto.BookResponse;
import com.hachicore.sellbook.api.search.dto.SearchApiResponse;
import com.hachicore.sellbook.api.secondprice.SecondPriceParseApi;
import com.hachicore.sellbook.api.secondprice.lambda.dto.SecondPriceResponse;
import com.hachicore.sellbook.domain.Book;
import com.hachicore.sellbook.domain.SecondPrice;
import com.hachicore.sellbook.dto.BookDto;
import com.hachicore.sellbook.dto.SecondPriceDto;
import com.hachicore.sellbook.repository.BookRepository;
import com.hachicore.sellbook.repository.SecondPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchService {

    private final BookSearchApi bookSearchApi;
    private final SecondPriceParseApi priceParseApi;

    private final BookRepository bookRepository;
    private final SecondPriceRepository secondPriceRepository;

    /*
     * [책 저장 흐름]
     * 1. API 호출
     * 2. DB 내 유무 조회
     * 3. 없는 책만
     * ========================
     * [중고 가격 저장 흐름]
     * 4. DB 조회(당일)
     * 5. DB에도 없으면 API 호출
     * 6. API 결과 저장
     * =========================
     * 7. 결과 Dto 변환 후 리턴
     * */
    @Transactional
    public Page<BookDto> search(String query, Pageable pageable) {
        SearchApiResponse searchResult = bookSearchApi.search(query, pageable);
        List<Book> searchBookList = mapToEntity(searchResult.getData());

        List<Book> savedBookList = saveBooks(searchBookList);
        List<SecondPrice> secondPrices = saveSecondPrices(savedBookList);

        return new PageImpl(
                mapToDto(savedBookList, secondPrices),
                searchResult.getPageable(),
                searchResult.getTotalCount()
        );
    }

    private List<Book> saveBooks(List<Book> searchBookList) {
        List<String> isbnList = getIsbnList(searchBookList);
        List<Book> alreadySavedBooks = bookRepository.findAllByIsbnIn(isbnList);  // DB 내 유무 조회

        if (alreadySavedBooks.size() == 0) {  // 저장된 책이 없으면 전부 저장
            bookRepository.saveAll(searchBookList);
        } else if (alreadySavedBooks.size() != searchBookList.size()) {  // 일부만 저장되어 있다면 필터링 후 저장
            saveFilteredBooks(searchBookList, alreadySavedBooks);
        }

        // 중고 가격 저장시 JPA 라이프사이클 문제로 1번 조회
        alreadySavedBooks = bookRepository.findAllByIsbnIn(isbnList);

        return sortByIsbn(alreadySavedBooks, isbnList);
    }

    private void saveFilteredBooks(List<Book> books, List<Book> savedBooks) {
        books.removeAll(savedBooks);
        bookRepository.saveAll(books);
    }

    private List<Book> sortByIsbn(List<Book> alreadySavedBooks, List<String> isbnList) {
        Book[] result = new Book[isbnList.size()];

        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < isbnList.size(); i++) {
            map.put(isbnList.get(i), i);
        }

        for (int i = 0; i < map.size(); i++) {
            Integer curr = map.get(alreadySavedBooks.get(i).getIsbn());
            result[curr] = alreadySavedBooks.get(i);
        }

        return Arrays.asList(result);
    }

    public List<SecondPrice> saveSecondPrices(List<Book> savedBooks) {
        List<Book> filteredList = savedBooks.stream()
                .filter(e -> e.getUpdatedAt() == null || e.getUpdatedAt().isBefore(LocalDate.now()))
                .collect(toList());

        if (filteredList.size() > 0) {
            List<String> filteredIsbnList = getIsbnList(filteredList);  // 당일 기준으로 필터링
            Map<String, Book> bookMap = filteredList.stream()
                    .collect(toMap(e -> e.getIsbn(), e -> e));

            List<SecondPriceResponse> parseResult = priceParseApi.parseSecondPrices(filteredIsbnList);  // DB에 없으면 API 호출

            List<SecondPrice> secondPrices = parseResult.stream()
                    .map(apiSecondPrice -> {
                        SecondPrice secondPrice = apiSecondPrice.toEntity();
                        secondPrice.addBook(bookMap.get(apiSecondPrice.getIsbn13()));

                        return secondPrice;
                    })
                    .collect(toList());

            // API 결과 저장
            secondPriceRepository.saveAll(secondPrices);
        }

        return secondPriceRepository.findTodayByBook(savedBooks);
    }

    private List<Book> mapToEntity(List<? extends BookResponse> data) {
        return data.stream()
                .map(BookResponse::toEntity)
                .collect(toList());
    }

    private List<BookDto> mapToDto(List<Book> bookList, List<SecondPrice> secondPriceList) {
        List<BookDto> result = bookList.stream()
                .map(BookDto::new)
                .collect(toList());

        Map<String, List<SecondPrice>> secondPriceMap = secondPriceList.stream()
                .collect(groupingBy(e -> e.getBook().getIsbn()));

        result.forEach(e -> {
            List<SecondPriceDto> secondPriceDtoList = secondPriceMap.get(e.getIsbn()).stream()
                    .map(SecondPriceDto::new)
                    .collect(toList());

            e.setSecondPrices(secondPriceDtoList);
        });

        return result;
    }

    private List<String> getIsbnList(List<Book> books) {
        return books.stream()
                .map(Book::getIsbn)
                .collect(toList());
    }

}
