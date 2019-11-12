package hystrix.feign;

import com.xhg.core.constant.HttpHeaders;
import com.xhg.core.web.DataHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * feign client 续传通用头部信息
 * @author xie
 */
@Component
public class HeadersInterceptor implements RequestInterceptor {

    protected Logger logger = LoggerFactory.getLogger(HeadersInterceptor.class);

    @Override
    public void apply(RequestTemplate template) {
        fillRequestCommonHeaders(template);
    }

    /**
     * 填充请求头信息 chenxiaojun 2019年2月15日
     */
    private void fillRequestCommonHeaders(RequestTemplate template) {
        HttpServletRequest request = DataHolder.getRequest();
        if (request == null) {
            logger.warn("http headers cannot post to next server ");
            return;
        }
        fillHeader(template, request, HttpHeaders.TOKEN_KEY);
        fillHeader(template, request, HttpHeaders.APP_NAME_KEY);
        fillHeader(template, request, HttpHeaders.CURRENT_USER_ID_KEY);
        fillHeader(template, request, HttpHeaders.CLIENT_NAME_KEY);
        //下面选传
        fillHeader(template, request, HttpHeaders.DEVICE_ID_KEY);
        fillHeader(template, request, HttpHeaders.APP_VERSION_KEY);
        fillHeader(template, request, HttpHeaders.CONFIG_VERSION_KEY);
        fillHeader(template, request, HttpHeaders.OS_TYPE_KEY);
        fillHeader(template, request, HttpHeaders.CHANNEL_KEY);
        fillHeader(template, request, HttpHeaders.PHOME_MODE_KEY);
        fillHeader(template, request, HttpHeaders.PHONE_RESOLUTION_KEY);
        fillHeader(template, request, HttpHeaders.SYSTEM_VERSION_KEY);
        fillHeader(template, request, HttpHeaders.SIGN_KEY);
        fillHeader(template, request, HttpHeaders.APP_ID_KEY);
        fillHeader(template, request, HttpHeaders.VALIDATE_TIME_KEY);
        fillHeader(template, request, HttpHeaders.TRACE_ID_KEY);
    }

    /**
     * 填充单个header chenxiaojun 2019年2月15日
     */
    private void fillHeader(RequestTemplate template, HttpServletRequest request, HttpHeaders key) {
        template.header(key.getHeaderName(), request.getHeader(key.getHeaderName()));
    }


}
