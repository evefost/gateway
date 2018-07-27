package com.xhg.server.b.anno;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;

public class MyFeigEncoder implements Encoder {
    private  SpringEncoder springEncoder;
    private SpringFormEncoder springFormEncoder;

    public MyFeigEncoder(SpringEncoder springEncoder, SpringFormEncoder springFormEncoder){
        this.springEncoder = springEncoder;
        this.springFormEncoder = springFormEncoder;
    }

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        if (bodyType.equals(MultipartFile.class)) {
            springFormEncoder.encode(object,bodyType,template);
        }else {
            springEncoder.encode(object,bodyType,template);
        }
    }
}
