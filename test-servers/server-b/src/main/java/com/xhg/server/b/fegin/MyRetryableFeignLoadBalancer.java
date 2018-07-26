/*
 *
 *  * Copyright 2013-2016 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.xhg.server.b.fegin;

import com.netflix.client.DefaultLoadBalancerRetryHandler;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.cloud.client.loadbalancer.LoadBalancedBackOffPolicyFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryListenerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryPolicyFactory;
import org.springframework.cloud.netflix.feign.ribbon.RetryableFeignLoadBalancer;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;

import java.net.URI;

import static org.springframework.cloud.netflix.ribbon.RibbonUtils.updateToHttpsIfNeeded;


public class MyRetryableFeignLoadBalancer extends RetryableFeignLoadBalancer {
	public MyRetryableFeignLoadBalancer(ILoadBalancer lb, IClientConfig clientConfig, ServerIntrospector serverIntrospector, LoadBalancedRetryPolicyFactory loadBalancedRetryPolicyFactory) {
		super(lb, clientConfig, serverIntrospector, loadBalancedRetryPolicyFactory);
	}

	public MyRetryableFeignLoadBalancer(ILoadBalancer lb, IClientConfig clientConfig, ServerIntrospector serverIntrospector, LoadBalancedRetryPolicyFactory loadBalancedRetryPolicyFactory, LoadBalancedBackOffPolicyFactory loadBalancedBackOffPolicyFactory) {
		super(lb, clientConfig, serverIntrospector, loadBalancedRetryPolicyFactory, loadBalancedBackOffPolicyFactory);
	}

	public MyRetryableFeignLoadBalancer(ILoadBalancer lb, IClientConfig clientConfig, ServerIntrospector serverIntrospector, LoadBalancedRetryPolicyFactory loadBalancedRetryPolicyFactory, LoadBalancedBackOffPolicyFactory loadBalancedBackOffPolicyFactory, LoadBalancedRetryListenerFactory loadBalancedRetryListenerFactory) {
		super(lb, clientConfig, serverIntrospector, loadBalancedRetryPolicyFactory, loadBalancedBackOffPolicyFactory, loadBalancedRetryListenerFactory);
	}


	@Override
	public URI reconstructURIWithServer(Server server, URI original) {
		URI uri = updateToHttpsIfNeeded(original, this.clientConfig, this.serverIntrospector, server);
		return super.reconstructURIWithServer(server, uri);
	}

}
