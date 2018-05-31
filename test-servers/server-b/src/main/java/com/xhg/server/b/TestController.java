package com.xhg.server.b;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xieyang on 17/11/13.
 */

@RestController
@RequestMapping("/v1/test")
public class TestController {

    @RequestMapping(value = "queryInfo",method = RequestMethod.GET)
    public String addCompose() {
        return "serverA info v1";
    }

}
