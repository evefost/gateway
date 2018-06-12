package com.xie.gateway.zuul;

import java.util.*;

import com.netflix.zuul.context.RequestContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.annotation.PostConstruct;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVICE_ID_KEY;

@Component
// spring会将对应配置项的值注入进来的
@ConfigurationProperties("gateway.zuul.tokenFilter")
public class ZuulConfigurationBean {

	// 这个列表存的是routeId。这个列表里面的路由，不需要进行token校验，在TokenValidataFilter中会用到
	private List<String> noAuthenticationRoutes;



	private Map<String,List<String>> noAuthServerUris = new HashMap<>();

	PathMatcher pathMatcher = new AntPathMatcher();

	@PostConstruct
	public void init(){

		List<String> list = new ArrayList<>();
		list.add("/a-path/test/login");
		list.add("/a-path/test2/**");
		noAuthServerUris.put("server-a",list);
	}


	public List<String> getNoAuthenticationRoutes() {
		return noAuthenticationRoutes;
	}

	public void setNoAuthenticationRoutes(List<String> noAuthenticationRoutes) {
		this.noAuthenticationRoutes = noAuthenticationRoutes;
	}



	
}
