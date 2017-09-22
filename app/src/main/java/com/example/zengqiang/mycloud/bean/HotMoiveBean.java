package com.example.zengqiang.mycloud.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.http.ParamNames;
import com.example.zengqiang.mycloud.BR;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zengqiang on 2017/9/4.
 */

public class HotMoiveBean extends BaseObservable implements Serializable {
    @ParamNames("count")
    private int count;

    @ParamNames("start")
    private int start;

    @ParamNames("total")
    private int total;

    @ParamNames("title")
    private String title;

    @ParamNames("subjects")
    private List<SubjectsBean> subjects;

    @Bindable
    public int getCount() {
        return count;
    }


    public void setCount(int count) {
        this.count = count;
        notifyPropertyChanged(BR.count);
    }

    @Bindable
    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
        notifyPropertyChanged(BR.start);
    }
    @Bindable
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
        notifyPropertyChanged(BR.total);
    }
    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public List<SubjectsBean> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectsBean> subjects) {
        this.subjects = subjects;
        notifyPropertyChanged(BR.subjects);
    }
}
