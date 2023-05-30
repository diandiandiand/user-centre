package com.liyaoyun.usercentre.common;


import lombok.Data;

//返回给前端的对象
@Data
public class BaseResponse<T> {   //这里泛型 是因为不同的方法有不同的返回类型
    private int code;
    private T data;
    private String message;
    private String description;


    public BaseResponse() {
    }

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }
    public BaseResponse(int code, T data,String message){
        this(code,data,message,"");
    }

    public BaseResponse(int code, T data) {
        this(code,data,"","");
    }


    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDescription());
    }
}
