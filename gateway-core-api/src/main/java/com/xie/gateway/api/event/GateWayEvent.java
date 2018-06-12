package com.xie.gateway.api.event;

import org.springframework.context.ApplicationEvent;

public class GateWayEvent  extends ApplicationEvent {
    public GateWayEvent(Object source) {
        super(source);
    }
}
