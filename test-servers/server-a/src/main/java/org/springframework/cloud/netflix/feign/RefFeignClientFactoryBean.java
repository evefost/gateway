package org.springframework.cloud.netflix.feign;

import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.cloud.netflix.feign.ribbon.LoadBalancerFeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * by overwrite {@link FactoryBean#getObject()} change proxy object
 *
 * <p>
 *
 * @author xieyang
 * @version 1.0.0
 * @date 2019/11/5
 */
public class RefFeignClientFactoryBean extends FeignClientFactoryBean {


    private Class<?> type;

    private String name;

    private String url;

    private String path;

    private boolean decode404;

    private ApplicationContext applicationContext;

    private Class<?> fallback = void.class;

    private Class<?> fallbackFactory = void.class;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasText(this.name, "Name must be set");
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.applicationContext = context;
    }

    @Override
    protected Feign.Builder feign(FeignContext context) {
        FeignLoggerFactory loggerFactory = get(context, FeignLoggerFactory.class);
        Logger logger = loggerFactory.create(this.type);

        // @formatter:off
        Feign.Builder builder = get(context, Feign.Builder.class)
                // required values
                .logger(logger)
                .encoder(get(context, Encoder.class))
                .decoder(get(context, Decoder.class))
                .contract(get(context, Contract.class));
        // @formatter:on

        configureFeign(context, builder);

        return builder;
    }

    @Override
    protected void configureFeign(FeignContext context, Feign.Builder builder) {
        FeignClientProperties properties = applicationContext.getBean(FeignClientProperties.class);
        if (properties != null) {
            if (properties.isDefaultToProperties()) {
                configureUsingConfiguration(context, builder);
                configureUsingProperties(properties.getConfig().get(properties.getDefaultConfig()), builder);
                configureUsingProperties(properties.getConfig().get(this.name), builder);
            } else {
                configureUsingProperties(properties.getConfig().get(properties.getDefaultConfig()), builder);
                configureUsingProperties(properties.getConfig().get(this.name), builder);
                configureUsingConfiguration(context, builder);
            }
        } else {
            configureUsingConfiguration(context, builder);
        }
    }

    @Override
    protected void configureUsingConfiguration(FeignContext context, Feign.Builder builder) {
        Logger.Level level = getOptional(context, Logger.Level.class);
        if (level != null) {
            builder.logLevel(level);
        }
        Retryer retryer = getOptional(context, Retryer.class);
        if (retryer != null) {
            builder.retryer(retryer);
        }
        ErrorDecoder errorDecoder = getOptional(context, ErrorDecoder.class);
        if (errorDecoder != null) {
            builder.errorDecoder(errorDecoder);
        }
        Request.Options options = getOptional(context, Request.Options.class);
        if (options != null) {
            builder.options(options);
        }
        Map<String, RequestInterceptor> requestInterceptors = context.getInstances(
                this.name, RequestInterceptor.class);
        if (requestInterceptors != null) {
            builder.requestInterceptors(requestInterceptors.values());
        }

        if (decode404) {
            builder.decode404();
        }
    }

    @Override
    protected void configureUsingProperties(FeignClientProperties.FeignClientConfiguration config, Feign.Builder builder) {
        if (config == null) {
            return;
        }

        if (config.getLoggerLevel() != null) {
            builder.logLevel(config.getLoggerLevel());
        }

        if (config.getConnectTimeout() != null && config.getReadTimeout() != null) {
            builder.options(new Request.Options(config.getConnectTimeout(), config.getReadTimeout()));
        }

        if (config.getRetryer() != null) {
            Retryer retryer = getOrInstantiate(config.getRetryer());
            builder.retryer(retryer);
        }

        if (config.getErrorDecoder() != null) {
            ErrorDecoder errorDecoder = getOrInstantiate(config.getErrorDecoder());
            builder.errorDecoder(errorDecoder);
        }

        if (config.getRequestInterceptors() != null && !config.getRequestInterceptors().isEmpty()) {
            // this will add request interceptor to builder, not replace existing
            for (Class<RequestInterceptor> bean : config.getRequestInterceptors()) {
                RequestInterceptor interceptor = getOrInstantiate(bean);
                builder.requestInterceptor(interceptor);
            }
        }

        if (config.getDecode404() != null) {
            if (config.getDecode404()) {
                builder.decode404();
            }
        }
    }

    private <T> T getOrInstantiate(Class<T> tClass) {
        try {
            return applicationContext.getBean(tClass);
        } catch (NoSuchBeanDefinitionException e) {
            return BeanUtils.instantiateClass(tClass);
        }
    }

    @Override
    protected <T> T get(FeignContext context, Class<T> type) {
        T instance = context.getInstance(this.name, type);
        if (instance == null) {
            throw new IllegalStateException("No bean found of type " + type + " for "
                    + this.name);
        }
        return instance;
    }

    @Override
    protected <T> T getOptional(FeignContext context, Class<T> type) {
        return context.getInstance(this.name, type);
    }


    protected <T> T loadBalance(Feign.Builder builder, FeignContext context,
                                Target.HardCodedTarget<T> target, Client client) {
        if (client != null) {
            builder.client(client);
            Targeter targeter = get(context, Targeter.class);
            return targeter.target(this, builder, context, target);
        }

        throw new IllegalStateException(
                "No Feign Client for loadBalancing defined. Did you forget to include spring-cloud-starter-netflix-ribbon?");
    }


    private DelegateClient delegateClient;
    @Override
    public Object getObject() throws Exception {
        delegateClient = buildDelegateClient();
        FeignContext context = applicationContext.getBean(FeignContext.class);
        Feign.Builder builder = feign(context);
        String url;
        if (!this.name.startsWith("http")) {
            url = "http://" + this.name;
        } else {
            url = this.name;
        }
        url += cleanPath();
        Client balanceClient = getOptional(context, Client.class);
        Client directClient = ((LoadBalancerFeignClient) balanceClient).getDelegate();
        delegateClient.setBalanceClient(balanceClient);
        delegateClient.setDefaultClient(directClient);
        Object proxy = loadBalance(builder, context, new Target.HardCodedTarget<>(this.type, this.name, url), delegateClient);
        return proxy;
    }

    private DelegateClient buildDelegateClient() {
        if(delegateClient == null){
            delegateClient = new DelegateClient();
        }
        boolean balance = !StringUtils.hasText(this.url);
        delegateClient.setBalance(balance);
        String balanceUrl;
        if (!this.name.startsWith("http")) {
            balanceUrl = "http://" + this.name;
        } else {
            balanceUrl = this.name;
        }
        balanceUrl += cleanPath();
        delegateClient.setBalanceUrl(balanceUrl);
        String directUrl;
        if (StringUtils.hasText(this.url) && !this.url.startsWith("http")) {
            directUrl = "http://" + this.url;
        } else {
            directUrl = this.url + cleanPath();
        }
        delegateClient.setDirectUrl(directUrl);
        return delegateClient;
    }


    public void refreshConfig(String url){
        if(StringUtils.isEmpty(url)){
            this.url = null;
        }else {
            this.url = url;
        }

        buildDelegateClient();
    }

    private String cleanPath() {
        String path = this.path.trim();
        if (StringUtils.hasLength(path)) {
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
        }
        return path;
    }

    @Override
    public Class<?> getObjectType() {
        return this.type;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public void setType(Class<?> type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean isDecode404() {
        return decode404;
    }

    @Override
    public void setDecode404(boolean decode404) {
        this.decode404 = decode404;
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public Class<?> getFallback() {
        return fallback;
    }

    @Override
    public void setFallback(Class<?> fallback) {
        this.fallback = fallback;
    }

    @Override
    public Class<?> getFallbackFactory() {
        return fallbackFactory;
    }

    @Override
    public void setFallbackFactory(Class<?> fallbackFactory) {
        this.fallbackFactory = fallbackFactory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RefFeignClientFactoryBean that = (RefFeignClientFactoryBean) o;
        return Objects.equals(applicationContext, that.applicationContext) &&
                decode404 == that.decode404 &&
                Objects.equals(fallback, that.fallback) &&
                Objects.equals(fallbackFactory, that.fallbackFactory) &&
                Objects.equals(name, that.name) &&
                Objects.equals(path, that.path) &&
                Objects.equals(type, that.type) &&
                Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationContext, decode404, fallback, fallbackFactory,
                name, path, type, url);
    }

    @Override
    public String toString() {
        return new StringBuilder("FeignClientFactoryBean{")
                .append("type=").append(type).append(", ")
                .append("name='").append(name).append("', ")
                .append("url='").append(url).append("', ")
                .append("path='").append(path).append("', ")
                .append("decode404=").append(decode404).append(", ")
                .append("applicationContext=").append(applicationContext).append(", ")
                .append("fallback=").append(fallback).append(", ")
                .append("fallbackFactory=").append(fallbackFactory)
                .append("}").toString();
    }

}
