package com.hachicore.sellbook.domain.event;

import com.hachicore.sellbook.domain.SecondPrice;
import org.springframework.context.ApplicationEvent;

public class SecondPriceSavedEvent extends ApplicationEvent {

    private final SecondPrice secondPrice;

    public SecondPriceSavedEvent(Object source) {
        super(source);
        this.secondPrice = (SecondPrice) source;
    }

    public SecondPrice getSecondPrice() {
        return secondPrice;
    }

}
