package com.xhg.server.c;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xieyang on 17/11/13.
 */

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping(value = "queryInfo",method = RequestMethod.GET)

    public void addCompose() {

    }

}
