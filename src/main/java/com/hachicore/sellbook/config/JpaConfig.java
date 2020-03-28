package com.hachicore.sellbook.config;

import com.hachicore.sellbook.domain.event.SecondPriceSavedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

    @Bean
    public ApplicationListener<SecondPriceSavedEvent> secondPriceListener() {
        return event -> {
            event.getSecondPrice()
                    .getBook()
                    .updateSecondPriceDate();
        };
    }

}
