package com.hachicore.sellbook.api.search.aladin.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class AladinSearchApiResponse {

    private Integer totalResults;
    private Integer startIndex;
    private Integer itemsPerPage;
    private List<AladinBookResponse> item;

}
