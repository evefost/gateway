package com.xie.gateway.service.impl;

import com.xie.gateway.api.event.GateWayEvent;
import com.xie.gateway.service.RefreshConfigService;
import org.springframework.stereotype.Service;

import javax.xml.ws.ServiceMode;

/**
 * Created by xieyang on 18/6/12.
 */
@Service
public class RefreshConfigServiceImpl implements RefreshConfigService {

    @Override
    public void syncRefresh(GateWayEvent event) {

    }
}
