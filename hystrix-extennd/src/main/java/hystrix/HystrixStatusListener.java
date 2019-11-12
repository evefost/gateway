package hystrix;


import com.netflix.hystrix.HystrixCommand;

/**
 * 熔断器监听器
 * @author xie
 */
public interface HystrixStatusListener {

    /**
     * 熔断器打开（粒度为接口级）
     * @param methodInfo 接口信息
     */
    void onCircuitBreakerOpen(RequestMappingInfo methodInfo, HystrixCommand cmd);

    /**
     * 熔断器关闭 （粒度为接口级）
     * @param methodInfo 接口信息
     */
    void onCircuitBreakerClose(RequestMappingInfo methodInfo, HystrixCommand cmd);


    /**
     *  成功回调
     * @param methodInfo
     * @param cmd
     */
    void onSuccess(RequestMappingInfo methodInfo, HystrixCommand cmd);


    /**
     * 失败回调
     * @param methodInfo
     * @param cmd
     */
    void onFailure(RequestMappingInfo methodInfo, HystrixCommand cmd);
}
