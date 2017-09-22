package com.example.zengqiang.mycloud.http.rx;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observers.Observers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by zengqiang on 2017/8/22.
 */

public class RxBus {
    private static volatile RxBus rxBus;

    public static RxBus getInstance(){
        if(rxBus==null){
            synchronized(RxBus.class){
                if(rxBus==null){
                    rxBus=new RxBus();
                }
            }
        }
        return rxBus;
    }

    private Subject<Object,Object> bus=new SerializedSubject<>(PublishSubject.create());

    public void post(int code,Object o){
        bus.onNext(new RxBusMessage(code,o));
    }

    public <T> Observable<T> toObservable(final int code,final Class<T> eventType){
        return bus.ofType(RxBusMessage.class)
                .filter(new Func1<RxBusMessage,Boolean>(){

                    @Override
                    public Boolean call(RxBusMessage rxBusMessage) {
                        return rxBusMessage.getCode()==code&&eventType.isInstance(rxBusMessage.getObject());
                    }
                })
                .map(new Func1<RxBusMessage,Object>(){

                    @Override
                    public Object call(RxBusMessage rxBusMessage) {
                        return rxBusMessage.getObject();
                    }
                })
                .cast(eventType);
    }
}
