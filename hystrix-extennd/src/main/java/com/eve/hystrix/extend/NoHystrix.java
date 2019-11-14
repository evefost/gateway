package com.eve.hystrix.extend;

import java.lang.annotation.*;

@Target(value= {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoHystrix {

}
