package com.hachicore.sellbook.api.secondprice.lambda;

import com.hachicore.sellbook.api.secondprice.SecondPriceParseApi;
import com.hachicore.sellbook.api.secondprice.lambda.dto.SecondPriceResponse;
import com.hachicore.sellbook.api.secondprice.lambda.prop.LambdaApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LambdaApi implements SecondPriceParseApi {

    private final RestTemplate restTemplate;
    private final LambdaApiProperties apiProperties;

    private HttpEntity httpEntity;
    private ParameterizedTypeReference<List<SecondPriceResponse>> responseType;

    @PostConstruct
    public void setHeader() {
        HttpHeaders header = new HttpHeaders();
        header.set("X-API-KEY", apiProperties.getKey());

        httpEntity = new HttpEntity(header);
        responseType = new ParameterizedTypeReference<List<SecondPriceResponse>>() {};
    }

    @Override
    public List<SecondPriceResponse> parseSecondPrices(List<String> isbnList) {
        ResponseEntity<List<SecondPriceResponse>> secondPrices = restTemplate.exchange(
                getUri(isbnList),
                HttpMethod.GET,
                httpEntity,
                responseType
        );

        return secondPrices.getBody();
    }

    private URI getUri(List<String> isbnList) {
        return UriComponentsBuilder
                    .fromUriString(apiProperties.getUrl())
                    .queryParam("isbn", isbnList)
                    .build().toUri();
    }

}
