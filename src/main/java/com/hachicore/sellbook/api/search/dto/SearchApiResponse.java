package com.hachicore.sellbook.api.search.dto;

import lombok.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class SearchApiResponse {

    private Integer totalCount;
    private Pageable pageable;
    private List<? extends BookResponse> data;

}
