package com.example.zengqiang.mycloud.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.http.ParamNames;
import com.example.zengqiang.mycloud.BR;
import com.example.zengqiang.mycloud.bean.moivechild.ImagesBean;
import com.example.zengqiang.mycloud.bean.moivechild.PersonBean;
import com.example.zengqiang.mycloud.bean.moivechild.RatingBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zengqiang on 2017/9/4.
 */

public class SubjectsBean extends BaseObservable implements Serializable {
    @ParamNames("rating")
    private RatingBean rating;
    @ParamNames("title")
    private String title;
    @ParamNames("collect_count")
    private int collect_count;
    @ParamNames("original_title")
    private String original_title;
    @ParamNames("subtype")
    private String subtype;
    @ParamNames("year")
    private String year;
    @ParamNames("images")
    private ImagesBean images;
    @ParamNames("alt")
    private String alt;
    @ParamNames("id")
    private String id;
    @ParamNames("genres")
    private List<String> genres;
    @ParamNames("casts")
    private List<PersonBean> casts;
    @ParamNames("directors")
    private List<PersonBean> directors;


    @Bindable
    public RatingBean getRating() {
        return rating;
    }

    @Bindable
    public String getTitle() {
        return this.title;
    }

    @Bindable
    public int getCollect_count() {
        return collect_count;
    }

    @Bindable
    public String getOriginal_title() {
        return original_title;
    }

    @Bindable
    public String getSubtype() {
        return subtype;
    }

    @Bindable
    public String getYear() {
        return year;
    }

    @Bindable
    public ImagesBean getImages() {
        return images;
    }

    @Bindable
    public String getAlt() {
        return alt;
    }

    @Bindable
    public String getId() {
        return id;
    }

    @Bindable
    public List<String> getGenres() {
        return genres;
    }

    @Bindable
    public List<PersonBean> getCasts() {
        return casts;
    }

    @Bindable
    public List<PersonBean> getDirectors() {
        return directors;
    }


    public void setRating(RatingBean rating) {
        this.rating = rating;
        notifyPropertyChanged(BR.rating);
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
        notifyPropertyChanged(BR.collect_count);
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
        notifyPropertyChanged(BR.original_title);
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
        notifyPropertyChanged(BR.subtype);
    }

    public void setYear(String year) {
        this.year = year;
        notifyPropertyChanged(BR.year);
    }

    public void setImages(ImagesBean images) {
        this.images = images;
        notifyPropertyChanged(BR.images);
    }

    public void setAlt(String alt) {
        this.alt = alt;
        notifyPropertyChanged(BR.alt);
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
        notifyPropertyChanged(BR.genres);
    }

    public void setCasts(List<PersonBean> casts) {
        this.casts = casts;
        notifyPropertyChanged(BR.casts);
    }

    public void setDirectors(List<PersonBean> directors) {
        this.directors = directors;
        notifyPropertyChanged(BR.directors);
    }

    @Override
    public String toString() {
        return "SubjectsBean{" +
                "directors=" + directors +
                ", casts=" + casts +
                ", genres=" + genres +
                ", id='" + id + '\'' +
                ", alt='" + alt + '\'' +
                ", images=" + images +
                ", year='" + year + '\'' +
                ", subtype='" + subtype + '\'' +
                ", original_title='" + original_title + '\'' +
                ", collect_count=" + collect_count +
                ", title='" + title + '\'' +
                ", rating=" + rating +
                '}';
    }
}
