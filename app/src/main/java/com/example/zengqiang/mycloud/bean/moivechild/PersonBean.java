package com.example.zengqiang.mycloud.bean.moivechild;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.http.ParamNames;
import com.example.zengqiang.mycloud.BR;

import java.io.Serializable;

/**
 * Created by zengqiang on 2017/9/4.
 */

public class PersonBean extends BaseObservable implements Serializable {
    @ParamNames("alt")
    private String alt;

    // 导演或演员
    @ParamNames("type")
    private String type;

    @ParamNames("avatars")
    private ImagesBean avatars;
    @ParamNames("name")
    private String name;
    @ParamNames("id")
    private String id;

    @Bindable
    public String getAlt() {
        return alt;
    }

    @Bindable
    public ImagesBean getAvatars() {
        return avatars;
    }

    @Bindable
    public String getName() {
        return name;
    }

    @Bindable
    public String getId() {
        return id;
    }

    public void setAlt(String alt) {
        this.alt = alt;
        notifyPropertyChanged(BR.alt);
    }

    public void setAvatars(ImagesBean avatars) {
        this.avatars = avatars;
        notifyPropertyChanged(BR.avatars);
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        notifyPropertyChanged(BR.type);
    }
}
