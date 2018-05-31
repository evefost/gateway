package com.xhg.gateway.zuul.filters;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xhg.gateway.bo.VRequestBody;
import com.xhg.gateway.api.AppManagerService;
import com.xhg.gateway.api.UriInfo;
import com.xhg.gateway.api.authorize.AuthoInfo;
import com.xhg.gateway.api.authorize.AuthorizeService;
import com.xhg.gateway.api.event.GateWayEvent;
import com.xhg.gateway.api.event.RefreshEvent;
import com.xhg.gateway.api.event.UriChangeEvent;
import com.xhg.gateway.vo.ResponseVo;
import com.xhg.gateway.vo.RsBody;
import com.xhg.gateway.zuul.ZuulConfigurationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private volatile Map<String, List<String>> noAuthServerUris = new HashMap<>();

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
        return isNeedAuthentication(ctx);
    }

    @Override
    public Object run() {
        String token = parseToken();
        if (token == null) {
            forbidden();
            return null;
        } else {
            AuthoInfo check = authorizeService.check(token);
            if (check == null) {
                forbidden();
            }
        }
        return null;
    }

    private String parseToken() {
        logger.debug("parseToken");
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = request.getHeader("access-token");
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
        } catch (IOException e) {
            e.printStackTrace();
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
    void forbidden() {
        logger.warn("请求无访问权限");
        RequestContext currentContext = RequestContext.getCurrentContext();
        currentContext.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
        //清掉服务id将不再待下走了
        ResponseVo responseVo = new ResponseVo();
        responseVo.setCode(1);
        RsBody rb = new RsBody();
        rb.setCode(HttpStatus.FORBIDDEN.value());
        rb.setMessage("无访问权限");
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


    private void refresh() {
        logger.debug("刷新非受限资源列表");
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
