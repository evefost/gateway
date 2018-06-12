package com.xie.gateway.service;

import com.xie.gateway.api.event.GateWayEvent;

/**
 * Created by xieyang on 18/6/12.
 */
public interface RefreshConfigService {

   void syncRefresh(GateWayEvent event);
}
