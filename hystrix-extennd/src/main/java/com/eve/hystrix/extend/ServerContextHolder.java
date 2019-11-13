package com.eve.hystrix.extend;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xieyang on 19/11/13.
 */
public class ServerContextHolder {


    private static ThreadLocal<Map<String, Object>> contextHolder = ThreadLocal.withInitial(() -> new HashMap<>());


    public static Object get(String key){
       return contextHolder.get().get(key);
    }

    public static  void set(String key,Object value){
        contextHolder.get().put(key,value);
    }


    public static void remove(String key){
        contextHolder.get().remove(key);
    }


    public static void main(String[] args) {
        Object o = get("123");
        set("123","456");

        o = get("123");
        System.out.println(0);
    }



}
