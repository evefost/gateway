package com.xie.gateway.controller;

import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

public class BaseController {

    @Resource
    protected HttpServletRequest request;

    protected int getCurrentPage(){
        String currentPage = request.getParameter("currentPage");
        if(StringUtils.isEmpty(currentPage)){
            return  1;
        }
        return Integer.parseInt(currentPage);
    }

    protected int getPageSize(){
        String pageSize = request.getParameter("pageSize");
        if(StringUtils.isEmpty(pageSize)){
            return  10;
        }
        return Integer.parseInt(pageSize);
    }
}
