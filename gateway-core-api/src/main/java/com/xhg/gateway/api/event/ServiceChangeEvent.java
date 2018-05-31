package com.xhg.gateway.api.event;

import com.xhg.gateway.api.AppInfo;

public class ServiceChangeEvent extends GateWayEvent {

    private AppInfo data;

    public ServiceChangeEvent(Object source, AppInfo data) {
        super(source);
        this.data = data;
    }

    public AppInfo getData() {
        return data;
    }

    public void setData(AppInfo data) {
        this.data = data;
    }
}
