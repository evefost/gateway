//package com.xhg.server.b;
//
//import org.springframework.boot.autoconfigure.web.BasicErrorController;
//import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
//import org.springframework.boot.autoconfigure.web.ErrorProperties;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Map;
//
//@RestController
//@ControllerAdvice
//public class PageErrorController extends BasicErrorController {
//
//
//    public PageErrorController(){
//        super(new DefaultErrorAttributes(), new ErrorProperties());
//    }
//
//    private static final String PATH = "/error";
//    @RequestMapping
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
//
//        Map body = this.getErrorAttributes(request, this.isIncludeStackTrace(request, MediaType.ALL));
//        HttpStatus status = this.getStatus(request);
//
//        return new ResponseEntity("找不到页面servera-vb", status);
//    }
//
//    @Override
//    public String getErrorPath() {
//        return PATH;
//    }
//}