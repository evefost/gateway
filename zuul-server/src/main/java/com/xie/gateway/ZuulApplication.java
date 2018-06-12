package com.xie.gateway;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.discovery.PatternServiceRouteMapper;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication(scanBasePackages = {"com.xie"})
@EnableEurekaClient
@EnableZuulProxy
@EnableCircuitBreaker
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class ZuulApplication {
	final static Logger logger = LoggerFactory.getLogger(ZuulApplication.class);
	//static JedisConnection sss = new JedisConnection(null);
	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(ZuulApplication.class)
				.web(true).run(args);
		logger.debug(applicationContext.getId() + "已经启动,当前host：{}",
				applicationContext.getEnvironment().getProperty("HOSTNAME"));
	}

	@Bean
//	@LoadBalanced //标识为负载策略
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public PatternServiceRouteMapper serviceRouteMapper() {
		return new PatternServiceRouteMapper(
				"(?<name>^.+)-(?<version>v.+$)",
				"${name}/${version}"){
			@Override
			public String apply(final String serviceId) {
				String route =  super.apply(serviceId);
				return route;
			}
		};
	}

	@Autowired
	private RedisProperties redisProperties;
	@Bean
	public JedisCluster getJedisCluster() {
		List<String> nodes1 = redisProperties.getCluster().getNodes();

		Set<HostAndPort> nodes = new HashSet<>();

		for (String ipPort : nodes1) {
			String[] ipPortPair = ipPort.split(":");
			nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
		}

		return new JedisCluster(nodes, redisProperties.getTimeout());
	}
	
}