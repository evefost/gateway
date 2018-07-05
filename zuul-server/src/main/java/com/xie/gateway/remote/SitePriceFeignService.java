package com.xie.gateway.remote;


import com.xie.gateway.bo.AppBo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 站点价格管理
 */
@FeignClient(name = "devxhg-sys-config")
@RequestMapping(value = "/sysconfg/site/price")

public interface SitePriceFeignService {

	/**
	 * 添加站站box价格
	 * @param params
	 * @return
	 */

	@RequestMapping(value = "/queryInfo", method = RequestMethod.POST)
	Integer queryInfo(@RequestBody AppBo params);



}
