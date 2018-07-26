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
		
		
		
		LoadBalancerContext
		
		FeignLoadBalancer extend LoadBalancerContext
		
			@Override
        	public URI reconstructURIWithServer(Server server, URI original) {
        		URI uri = updateToHttpsIfNeeded(original, this.clientConfig, this.serverIntrospector, server);
        		return super.reconstructURIWithServer(server, uri);
        	}
        		public RibbonResponse execute(RibbonRequest request, IClientConfig configOverride){
        		    //
        		    Response response = request.client().execute(request.toRequest(), options);
                	return new RibbonResponse(request.getUri(), response);
        		}