package com.mmall.common;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
//保证序列化json的时候，如果是null对象，key也会消失
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ResponseService<T> implements Serializable {
    /**
     * 状态码
     */
    private Integer status;
    /**
     * 消息
     */
    private String msg;
    /**
     * 封装返回的数据
     */
    private T data;

    public Integer getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    private ResponseService(int status) {
        this.status = status;
    }

    private ResponseService(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ResponseService(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ResponseService(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    @JsonIgnore
    //使之不在json序列化结果之中
    public boolean isSuccess(){
        return this.status .equals(ResponseCodeEnum.SUCCESS.getCode()) ;
    }
    public static <T> ResponseService<T> creatBySuccess(){
        return new ResponseService<>(ResponseCodeEnum.SUCCESS.getCode());
    }
    public static <T> ResponseService<T> creatBySuccess(T data){
        return new ResponseService<>(ResponseCodeEnum.SUCCESS.getCode(),data);
    }
    public static <T> ResponseService<T> creatBySuccessMessage(String msg){
        return new ResponseService<>(ResponseCodeEnum.SUCCESS.getCode(),msg);
    }
    public static <T> ResponseService<T> creatBySuccess(String msg,T data){
        return new ResponseService<>(ResponseCodeEnum.SUCCESS.getCode(),msg,data);
    }
    public static <T> ResponseService<T> creatByError(){
        return new ResponseService<>(ResponseCodeEnum.ERROR.getCode(),ResponseCodeEnum.ERROR.getDesc());
    }
    public static <T> ResponseService<T> creatByError(String msg){
        return new ResponseService<>(ResponseCodeEnum.ERROR.getCode(),msg);
    }
    public static <T> ResponseService<T> createByErrorCodeMessage(int errorCode,String errorMessage){
        return new ResponseService<>(errorCode,errorMessage);
    }
}
