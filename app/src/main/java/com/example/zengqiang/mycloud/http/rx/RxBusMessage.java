package com.example.zengqiang.mycloud.http.rx;

/**
 * Created by zengqiang on 2017/8/23.
 */

public class RxBusMessage {
    private int code;
    private Object object;

    public RxBusMessage(){

    }

    public RxBusMessage(int code,Object object){
        this.code=code;
        this.object=object;
    }

    public int getCode(){
        return code;
    }

    public Object getObject(){
        return object;
    }
}
