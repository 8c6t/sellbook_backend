package com.hachicore.sellbook.api.search.aladin;

import com.hachicore.sellbook.api.search.BookSearchApi;
import com.hachicore.sellbook.api.search.aladin.dto.AladinBookResponse;
import com.hachicore.sellbook.api.search.aladin.dto.AladinSearchApiResponse;
import com.hachicore.sellbook.api.search.aladin.prop.AladinSearchApiProperties;
import com.hachicore.sellbook.api.search.dto.SearchApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AladinSearchApi implements BookSearchApi {

    private final RestTemplate restTemplate;
    private final AladinSearchApiProperties properties;

    @Override
    public SearchApiResponse search(String query, Pageable pageable) {
        URI uri = getUri(query, pageable);

        ResponseEntity<AladinSearchApiResponse> responseEntity = restTemplate.getForEntity(uri, AladinSearchApiResponse.class);
        AladinSearchApiResponse response = responseEntity.getBody();

        return SearchApiResponse.builder()
                .totalCount(response.getTotalResults())
                .pageable(PageRequest.of(response.getStartIndex() - 1, response.getItemsPerPage()))
                .data(getBookList(response))
                .build();
    }

    private List<AladinBookResponse> getBookList(AladinSearchApiResponse body) {
        List<AladinBookResponse> bookList = body.getItem();
        bookList.stream().forEach(e -> {
            e.setCover(convertImgBase64(e.getCover()));
        });

        return bookList;
    }

    private URI getUri(String query, Pageable pageable) {
        return UriComponentsBuilder
                .fromUriString(properties.getUrl())
                .queryParam("ttbkey", properties.getKey())
                .queryParam("Query", query)
                .queryParam("Sort", "Title")
                .queryParam("searchTarget", "Book")
                .queryParam("start", pageable.getPageNumber() + 1)
                .queryParam("MaxResults", pageable.getPageSize())
                .queryParam("Output", "js")
                .queryParam("Version", properties.getVersion())
                .build().toUri();
    }

    public String convertImgBase64(String imgUrl) {
        if (imgUrl == null || imgUrl.length() == 0) {
            return null;
        }

        byte[] byteImg = restTemplate.getForObject(imgUrl, byte[].class);
        return Base64.encodeBase64String(byteImg);
    }

}

