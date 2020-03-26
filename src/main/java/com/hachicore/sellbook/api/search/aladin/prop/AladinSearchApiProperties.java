package com.hachicore.sellbook.api.search.aladin.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

@Component
@ConfigurationProperties(prefix = "aladin")
@Getter @Setter
public class AladinSearchApiProperties {

    @NotEmpty
    private String url;

    @NotEmpty
    private String key;

    @NotEmpty
    private String version;

}
