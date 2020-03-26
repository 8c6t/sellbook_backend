package com.hachicore.sellbook.api.secondprice.lambda.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

@Component
@ConfigurationProperties(prefix = "lambda")
@Getter @Setter
public class LambdaApiProperties {

    @NotEmpty
    private String key;

    @NotEmpty
    private String url;

}
