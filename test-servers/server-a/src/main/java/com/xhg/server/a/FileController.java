package com.xhg.server.a;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xieyang on 17/11/13.
 */

@RestController

public class FileController {


    @Autowired
    private FeignServerC serverC;


    @RequestMapping(value = "/upload/image", method = RequestMethod.POST)
    public RestResult getUser(UploadImageRequest uploadImageRequest) {
        RestResult restResult = new RestResult();
        restResult.setCode(200);
        restResult.setMessage("OK");
        System.out.println("====================");
        return restResult;
    }


}
