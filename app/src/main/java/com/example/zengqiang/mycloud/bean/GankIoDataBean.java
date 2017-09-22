package com.example.zengqiang.mycloud.bean;

import com.example.http.ParamNames;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zengqiang on 2017/9/18.
 */

public class GankIoDataBean implements Serializable {
    @ParamNames("error")
    private boolean error;
    @ParamNames("results")
    private List<ResultBean> results;

    public static class ResultBean implements Serializable {

        @ParamNames("_id")
        private String _id;
        @ParamNames("createdAt")
        private String createdAt;
        @ParamNames("desc")
        private String desc;
        @ParamNames("publishedAt")
        private String publishedAt;
        @ParamNames("source")
        private String source;
        @ParamNames("type")
        private String type;
        @ParamNames("url")
        private String url;
        @ParamNames("used")
        private boolean used;
        @ParamNames("who")
        private String who;
        @ParamNames("images")
        private List<String> images;

        public String get_id() {
            return _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public String getSource() {
            return source;
        }

        public String getType() {
            return type;
        }

        public String getUrl() {
            return url;
        }

        public boolean isUsed() {
            return used;
        }

        public String getWho() {
            return who;
        }

        @Override
        public String toString() {
            return "ResultsBean{" +
                    "who='" + who + '\'' +
                    ", used=" + used +
                    ", url='" + url + '\'' +
                    ", type='" + type + '\'' +
                    ", source='" + source + '\'' +
                    ", publishedAt='" + publishedAt + '\'' +
                    ", desc='" + desc + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", _id='" + _id + '\'' +
                    '}';
        }

        public List<String> getImages() {
            return images;
        }
    }

    public boolean isError() {
        return error;
    }

    public List<ResultBean> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "GankIoDataBean{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
