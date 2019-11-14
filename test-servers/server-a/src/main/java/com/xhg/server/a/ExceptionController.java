package com.xhg.server.a;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2017/7/5.
 */

@ControllerAdvice
public class ExceptionController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 处理校验或其它异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseBean handValidationError(HttpServletRequest request, HttpServletResponse response, Exception e) {
        logger.info("请求路径:{}", request.getRequestURI());
        ResponseBean responseDataVo = null;
        if (e instanceof XhgException) {
            logger.debug("处理异常", e);
            XhgException wzException = (XhgException) e;
            responseDataVo = ResponseBean.failure(wzException.getCode(), wzException.getMessage());
        } else if (e instanceof BindException) {
            logger.error("参数校验异常", e);
            BindException bindException = (BindException) e;
            String message = bindException.getAllErrors().get(0).getDefaultMessage();
            responseDataVo = ResponseBean.failure(ResponeStatus.PARAM_FAIL_CODE.getValue(), message);
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            String method = ((HttpRequestMethodNotSupportedException) e).getMethod();
            logger.warn("不支持" + method + "请求");
            responseDataVo = ResponseBean.failure("不支持" + method + "请求");
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException arEx = (MethodArgumentNotValidException) e;
            String params = arEx.getBindingResult().getTarget().toString();
            logger.info("参数有误params:{}", params);
            String defaultMessage = arEx.getBindingResult().getAllErrors().get(0).getDefaultMessage();
            responseDataVo = ResponseBean.failure(defaultMessage);
        } else if (e instanceof InvalidFormatException) {
            responseDataVo = ResponseBean.failure("无效的格式");
        } else if (e instanceof HttpMediaTypeNotSupportedException) {
            responseDataVo = ResponseBean.failure("不支持的数据传输");
        } else if (e instanceof MissingServletRequestParameterException) {
            responseDataVo = ResponseBean.failure("缺少必需参数");
        } else if (e instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException parameterException = (MissingServletRequestParameterException) e;
            responseDataVo = ResponseBean.failure("缺少密必传参数" + parameterException.getParameterName());
        } else {
            logger.error("系统出错", e);
            responseDataVo = ResponseBean.failure(ResponeStatus.ERROR.getValue(), ResponeStatus.ERROR.getReasonPhrase());
        }
        return responseDataVo;
    }
}
