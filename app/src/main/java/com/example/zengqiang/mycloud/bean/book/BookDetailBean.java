package com.example.zengqiang.mycloud.bean.book;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.http.ParamNames;
import com.example.zengqiang.mycloud.BR;
import com.example.zengqiang.mycloud.bean.moivechild.ImagesBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zengqiang on 2017/9/16.
 */

public class BookDetailBean extends BaseObservable implements Serializable {
    @ParamNames("rating")
    private BookBean.RatingBean rating;
    @ParamNames("subtitle")
    private String subtitle;
    @ParamNames("pubdate")
    private String pubdate;
    @ParamNames("origin_title")
    private String origin_title;
    @ParamNames("image")
    private String image;
    @ParamNames("binding")
    private String binding;
    @ParamNames("catalog")
    private String catalog;
    @ParamNames("pages")
    private String pages;
    @ParamNames("images")
    private ImagesBean images;
    @ParamNames("alt")
    private String alt;
    @ParamNames("id")
    private String id;
    @ParamNames("publisher")
    private String publisher;
    @ParamNames("isbn10")
    private String isbn10;
    @ParamNames("isbn13")
    private String isbn13;
    @ParamNames("title")
    private String title;
    @ParamNames("url")
    private String url;
    @ParamNames("alt_title")
    private String alt_title;
    @ParamNames("author_intro")
    private String author_intro;
    @ParamNames("summary")
    private String summary;
    @ParamNames("price")
    private String price;
    @ParamNames("author")
    private List<String> author;
    @ParamNames("tags")
    private List<BookBean.TagsBean> tags;
    @ParamNames("translator")
    private List<String> translator;

    @Bindable
    public BookBean.RatingBean getRating() {
        return rating;
    }

    public void setRating(BookBean.RatingBean rating) {
        this.rating = rating;
        notifyPropertyChanged(BR.rating);
    }

    @Bindable
    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        notifyPropertyChanged(BR.subtitle);
    }

    @Bindable
    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
        notifyPropertyChanged(BR.pubdate);
    }

    @Bindable
    public String getOrigin_title() {
        return origin_title;
    }

    public void setOrigin_title(String origin_title) {
        this.origin_title = origin_title;
        notifyPropertyChanged(BR.origin_title);
    }

    @Bindable
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        notifyPropertyChanged(BR.image);
    }

    @Bindable
    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
        notifyPropertyChanged(BR.binding);
    }

    @Bindable
    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
        notifyPropertyChanged(BR.catalog);
    }

    @Bindable
    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
        notifyPropertyChanged(BR.pages);
    }

    @Bindable
    public ImagesBean getImages() {
        return images;
    }

    public void setImages(ImagesBean images) {
        this.images = images;
        notifyPropertyChanged(BR.images);
    }

    @Bindable
    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
        notifyPropertyChanged(BR.alt);
    }

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
        notifyPropertyChanged(BR.publisher);
    }

    @Bindable
    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
        notifyPropertyChanged(BR.isbn10);
    }

    @Bindable
    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
        notifyPropertyChanged(BR.isbn13);
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
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        notifyPropertyChanged(BR.url);
    }

    @Bindable
    public String getAlt_title() {
        return alt_title;
    }

    public void setAlt_title(String alt_title) {
        this.alt_title = alt_title;
        notifyPropertyChanged(BR.alt_title);
    }

    @Bindable
    public String getAuthor_intro() {
        return author_intro;
    }

    public void setAuthor_intro(String author_intro) {
        this.author_intro = author_intro;
        notifyPropertyChanged(BR.author_intro);
    }

    @Bindable
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
        notifyPropertyChanged(BR.summary);
    }

    @Bindable
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
        notifyPropertyChanged(BR.price);
    }

    @Bindable
    public List<String> getAuthor() {
        return author;
    }

    public void setAuthor(List<String> author) {
        this.author = author;
        notifyPropertyChanged(BR.author);
    }

    @Bindable
    public List<BookBean.TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<BookBean.TagsBean> tags) {
        this.tags = tags;
        notifyPropertyChanged(BR.tags);
    }

    @Bindable
    public List<String> getTranslator() {
        return translator;
    }

    public void setTranslator(List<String> translator) {
        this.translator = translator;
        notifyPropertyChanged(BR.translator);
    }

}

