package com.xie.gateway.zuul.filters;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.xie.gateway.bo.VRequestBody;
import com.xie.gateway.api.AppManagerService;
import com.xie.gateway.api.UriInfo;
import com.xie.gateway.api.authorize.AuthoInfo;
import com.xie.gateway.api.authorize.AuthorizeService;
import com.xie.gateway.api.event.GateWayEvent;
import com.xie.gateway.api.event.RefreshEvent;
import com.xie.gateway.api.event.UriChangeEvent;
import com.xie.gateway.vo.ResponseVo;
import com.xie.gateway.vo.RsBody;
import com.xie.gateway.zuul.ZuulConfigurationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVICE_ID_KEY;

/**
 * 验证token。
 */
@Component
public class TokenValidataFilter extends ZuulFilter implements ApplicationListener<GateWayEvent> {

    protected static final Logger logger = LoggerFactory.getLogger(TokenValidataFilter.class);

    @Resource
    private AppManagerService appManagerService;

    @Resource
    private AuthorizeService authorizeService;

    /**
     * 非受权服务ids
     */
    private volatile List<String> noServicIds = new ArrayList<>();

    /**
     * 非受权url
     */
    private volatile Map<String, List<String>> noAuthServerUris = new ConcurrentHashMap<>();



    PathMatcher pathMatcher = new AntPathMatcher();


    // 自定义的配置
    ZuulConfigurationBean configurationBean;

    public TokenValidataFilter(ZuulConfigurationBean tonyConfigurationBean) {
        this.configurationBean = tonyConfigurationBean;
    }

    @PostConstruct
    public void init() {
        refresh();
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        logger.info("sessionId========:{}",ctx.getRequest().getSession().getId());
        return isNeedAuthentication(ctx);
    }

    @Override
    public Object run() {
        String token = parseToken();
        if (StringUtils.isEmpty(token)) {
            forbidden("无访问权限,需登录");
            return null;
        } else {
            AuthoInfo check = authorizeService.check(token);
            if (check == null) {
                forbidden("登录信息无效");
            }
        }
        return null;
    }

    private String parseToken() {
        logger.debug("parseToken");
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = request.getHeader("token");
        if(!StringUtils.isEmpty(token)){
            return token;
        }
        //兼容老的接口token解释
        String method = request.getMethod();
        String contentType = request.getContentType();
        if("GET".equals(method)){
            token = request.getParameter("requestHead.token");
        }else if("POST".equals(method)) {
            if ("application/json".equals(contentType)) {
                String bodyStr = readBodyString(request);
                if (bodyStr != null) {
                    VRequestBody vRequestBody = JSON.parseObject(bodyStr, VRequestBody.class);
                    if (vRequestBody.getRequestHead() != null) {
                        token = vRequestBody.getRequestHead().getToken();
                    }
                }
            }
        }

        if(token == null){
            token =  request.getSession().getId();

        }
        return token;
    }

    private String  readBodyString(HttpServletRequest request) {

        BufferedReader br = null;
        try {
            br = request.getReader();
            StringBuilder sb= new StringBuilder();
            String lineStr;
            while((lineStr = br.readLine()) != null){
                sb.append(lineStr);
            }
            return sb.toString();
        } catch (Exception e) {
            logger.error("网关解释body出错:{}",e);
            if(br != null){
                try {
                    br.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;

    }

    // 设置response的状态码为403
    void forbidden(String message) {
        logger.warn("请求无访问权限");
        RequestContext currentContext = RequestContext.getCurrentContext();
        currentContext.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
        //清掉服务id将不再待下走了
        ResponseVo responseVo = new ResponseVo();
        responseVo.setCode(1);
        RsBody rb = new RsBody();
        rb.setCode(HttpStatus.FORBIDDEN.value());
        rb.setMessage(message);
        responseVo.setResponseBody(rb);
        currentContext.remove(SERVICE_ID_KEY);
        currentContext.setResponseBody(JSON.toJSONString(responseVo));
        currentContext.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
        currentContext.getResponse().setContentType("application/json;charset=UTF-8");
       // ReflectionUtils.rethrowRuntimeException(new ZuulException("无访问权限", HttpStatus.FORBIDDEN.value(), "token校验不通过"));
    }

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 7;
    }


    private synchronized void refresh() {
        logger.debug("刷新非受限资源列表");
        List<String> serviceList = appManagerService.noAuthoServiceList();
        noServicIds = serviceList;
        Map<String, List<String>> serviceUris = appManagerService.noAuthUriList();
        if (!serviceUris.isEmpty()) {
            noAuthServerUris = serviceUris;
        }

    }


    public boolean isNeedAuthentication(RequestContext context) {
        String requestURI = (String) context.get("requestURI");
        String serviceId = (String) context.get(SERVICE_ID_KEY);

        if (serviceId == null) {
            //return !noAuthenticationRoutes.contains(requestURI);
            return false;
        } else {
            if(noServicIds.contains(serviceId)){
                return false;
            }
            Set<Map.Entry<String, List<String>>> entries = noAuthServerUris.entrySet();
            for (Map.Entry<String, List<String>> entry : entries) {
                List<String> values = entry.getValue();
                for (String parten : values) {
                    if (pathMatcher.match(parten, requestURI)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void onApplicationEvent(GateWayEvent gateWayEvent) {
        if (gateWayEvent instanceof UriChangeEvent) {
            UriChangeEvent uriChangeEvent = (UriChangeEvent) gateWayEvent;
            UriInfo data = uriChangeEvent.getData();
            logger.debug("收到服务uri信息修改:{}/operateStatus:{}", data.getServiceId() + ":" + data.getUrl(), data.getOperateStatus());
            refresh();
        }else if(gateWayEvent instanceof RefreshEvent){
            refresh();
        }

    }
}
