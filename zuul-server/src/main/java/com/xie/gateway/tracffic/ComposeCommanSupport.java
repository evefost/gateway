package com.xie.gateway.tracffic;

import java.util.List;

/**
 * Created by xieyang on 18/6/14.
 */
public class ComposeCommanSupport implements CommanSupport {

    private List<CommanSupport> supports;

    private volatile CommanSupport targetSupport;

    @Override
    public boolean suport(Object factory) {
        return false;
    }

    @Override
    public void handler() {
        if(targetSupport != null){

        }else {

        }

    }
}
