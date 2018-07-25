/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xhg.test.common;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.cloud.netflix.eureka.InstanceInfoFactory;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import java.lang.annotation.*;

/**
 * @author Dave Syer
 * @author Spencer Gibb
 * @author Jon Schneider
 * @author Matt Jenkins
 * @author Ryan Baxter
 * @author Daniel Lavoie
 */
@Configuration
public class MyAutoConfiguration extends EurekaClientAutoConfiguration {




	@Configuration
	@ConditionalOnMissingRefreshScope
	protected static class EurekaClientConfiguration2 extends EurekaClientConfiguration{

		@Autowired
		private Environment  environment;

		@Bean
		public ApplicationInfoManager eurekaApplicationInfoManager(
				EurekaInstanceConfig config) {
			String serverContextPath = environment.getProperty("server.context-path", "/");
			InstanceInfo instanceInfo = new InstanceInfoFactory().create(config);
			instanceInfo.getMetadata().put("server.context-path",serverContextPath);

			return new ApplicationInfoManager(config, instanceInfo);
		}
	}

	@Configuration
	@ConditionalOnRefreshScope
	protected static class RefreshableEurekaClientConfiguration2 extends RefreshableEurekaClientConfiguration {
		@Autowired
		private Environment  environment;

		@Bean
		@ConditionalOnMissingBean(value = ApplicationInfoManager.class, search = SearchStrategy.CURRENT)
		@org.springframework.cloud.context.config.annotation.RefreshScope
		@Lazy
		public ApplicationInfoManager eurekaApplicationInfoManager(EurekaInstanceConfig config) {
			String serverContextPath = environment.getProperty("server.context-path", "/");
			InstanceInfo instanceInfo = new InstanceInfoFactory().create(config);
			instanceInfo.getMetadata().put("server.context-path",serverContextPath);

			return new ApplicationInfoManager(config, instanceInfo);
		}

	}

	@Target({ ElementType.TYPE, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Conditional(OnMissingRefreshScopeCondition.class)
	@interface ConditionalOnMissingRefreshScope {

	}

	@Target({ ElementType.TYPE, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@ConditionalOnClass(RefreshScope.class)
	@ConditionalOnBean(RefreshAutoConfiguration.class)
	@interface ConditionalOnRefreshScope {

	}

	private static class OnMissingRefreshScopeCondition extends AnyNestedCondition {

		public OnMissingRefreshScopeCondition() {
			super(ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN);
		}

		@ConditionalOnMissingClass("org.springframework.cloud.context.scope.refresh.RefreshScope")
		static class MissingClass {
		}

		@ConditionalOnMissingBean(RefreshAutoConfiguration.class)
		static class MissingScope {
		}

	}


}
