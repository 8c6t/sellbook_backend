package com.hachicore.sellbook.service;

import com.hachicore.sellbook.domain.*;
import com.hachicore.sellbook.dto.BookDto;
import com.hachicore.sellbook.dto.SecondPriceDto;
import com.hachicore.sellbook.repository.BookRepository;
import com.hachicore.sellbook.repository.StorageBookRepository;
import com.hachicore.sellbook.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final BookRepository bookRepository;
    private final StorageRepository storageRepository;
    private final StorageBookRepository storageBookRepository;

    private final SearchService searchService;

    @Transactional
    public void addBook(Account account, List<Long> bookIds) {
        Storage storage = findStorage(account);
        List<StorageBook> saveList = getSaveList(bookIds, storage);

        storageBookRepository.saveAll(saveList);
    }

    private Storage findStorage(Account account) {
        return storageRepository.findByAccount(account)
                .orElseThrow(() -> new RuntimeException());
    }

    private List<StorageBook> getSaveList(List<Long> bookIds, Storage storage) {
        List<Book> bookList = bookRepository.findAllById(bookIds);

        List<StorageBook> saveList = bookList.stream().map(book -> {
            StorageBook storageBook = StorageBook.builder()
                    .storage(storage)
                    .book(book)
                    .build();

            return storageBook;
        }).collect(Collectors.toList());

        return saveList;
    }

    @Transactional
    public Page<BookDto> getStorageBookList(Account account, Pageable pageable) {
        Page<Book> bookList = bookRepository.findBooksInStorage(findStorage(account), pageable);
        List<SecondPrice> secondPrices = searchService.saveSecondPrices(bookList.toList());

        return mapToDto(bookList, secondPrices);
    }

    private Page<BookDto> mapToDto(Page<Book> bookList, List<SecondPrice> secondPrices) {
        Map<String, List<SecondPrice>> secondPriceMap = secondPrices.stream()
                .collect(groupingBy(e -> e.getBook().getIsbn()));

        return bookList.map(e -> {
            BookDto bookDto = new BookDto(e);

            List<SecondPriceDto> secondPriceDtoList = secondPriceMap.get(e.getIsbn()).stream()
                    .map(SecondPriceDto::new)
                    .collect(toList());

            bookDto.setSecondPrices(secondPriceDtoList);
            return bookDto;
        });
    }

    @Transactional
    public void removeBook(Account account, List<Long> bookIds) {
        Storage storage = findStorage(account);
        storageBookRepository.deleteBooks(storage, bookIds);
    }

}
