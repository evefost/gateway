package com.xhg.server.a;


import java.io.Serializable;

public class ResponseBean<T> implements Serializable, Cloneable {

    public static final long serialVersionUID = 6334971409410600125L;

    private T data;

    /**
     * {@link ResponeStatus}
     */
    private int code = 1;

    private String message = "成功";

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ResponseBean() {
    }


    public ResponseBean(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static <T> ResponseBean<T> success(){
        ResponseBean<T> tResponseBean = new ResponseBean<>();
        tResponseBean.code = ResponeStatus.SUCCESS.getValue();
        return tResponseBean;
    }

    public static <T> ResponseBean<T> success(String message){
        ResponseBean<T> tResponseBean = new ResponseBean<>();
        tResponseBean.code = ResponeStatus.SUCCESS.getValue();
        tResponseBean.message=message;
        return tResponseBean;
    }

    public static <T> ResponseBean<T> success(T data){
        ResponseBean<T> tResponseBean = new ResponseBean<>(data);
        tResponseBean.code = ResponeStatus.SUCCESS.getValue();
        return tResponseBean;
    }

    public static <T> ResponseBean<T> failure(String message){
        ResponseBean<T> tResponseBean = new ResponseBean<>();
        tResponseBean.code = ResponeStatus.FAILURE.getValue();
        tResponseBean.message=message;
        return tResponseBean;
    }

    public static <T> ResponseBean<T> failure(int code,String message){
        ResponseBean<T> tResponseBean = new ResponseBean<>();
        tResponseBean.code = code;
        tResponseBean.message = message;
        return tResponseBean;
    }


    public void setResult(ResponeStatus statuEnum) {
        this.code = statuEnum.getValue();
        this.message = statuEnum.getReasonPhrase();
    }

    @SuppressWarnings("unchecked")
    public ResponseBean<T> clone() {
        ResponseBean<T> o = null;
        try {
            o = (ResponseBean<T>) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

}
