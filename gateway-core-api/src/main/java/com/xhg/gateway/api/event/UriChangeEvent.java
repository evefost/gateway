package com.xhg.gateway.api.event;

import com.xhg.gateway.api.UriInfo;

public class UriChangeEvent extends GateWayEvent {

    private UriInfo data;

    public UriChangeEvent(Object source, UriInfo data) {
        super(source);
        this.data = data;
    }

    public UriInfo getData() {
        return data;
    }

    public void setData(UriInfo data) {
        this.data = data;
    }
}
