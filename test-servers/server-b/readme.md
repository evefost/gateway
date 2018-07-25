com.netflix.appinfo.InstanceInfo
com.netflix.discovery.DiscoveryClient 



		@Bean
		@ConditionalOnMissingBean(value = ApplicationInfoManager.class, search = SearchStrategy.CURRENT)
		@org.springframework.cloud.context.config.annotation.RefreshScope
		@Lazy
		public ApplicationInfoManager eurekaApplicationInfoManager(EurekaInstanceConfig config) {
			//注册到eureka上的信息，目前是没有context-path
			InstanceInfo instanceInfo = new InstanceInfoFactory().create(config);
			return new ApplicationInfoManager(config, instanceInfo);
		}
		
		
		AbstractJerseyEurekaHttpClient.regiest