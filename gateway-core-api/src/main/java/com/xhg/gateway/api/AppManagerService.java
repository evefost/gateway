package com.xhg.gateway.api;

import java.util.List;
import java.util.Map;

public interface AppManagerService {

    /**
     * 禁用的服务列表
     * @return
     */
    List<String> disableServiceList();

    /**
     * 非受权资源列表
     * key:serviceId,value：uris
     * @return
     */
    Map<String,List<String>> noAuthUriList();

    /**
     * 刷新关配置信息
     */
    void refresh();


}
