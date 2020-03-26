package com.hachicore.sellbook.api.secondprice;

import com.hachicore.sellbook.api.secondprice.lambda.dto.SecondPriceResponse;

import java.util.List;

public interface SecondPriceParseApi {

    List<SecondPriceResponse> parseSecondPrices(List<String> isbnList);

}
