package com.example.zengqiang.mycloud.utils;

import com.example.zengqiang.mycloud.bean.moivechild.PersonBean;

import java.util.List;

/**
 * Created by zengqiang on 2017/9/4.
 */

public class StringFormatUtils {

    public static String formatName(List<PersonBean> list){
        if(list!=null&&list.size()>0){
            StringBuilder builder=new StringBuilder("");
            for(int i=0;i<list.size();i++){
                if(i<list.size()-1){
                    builder.append(list.get(i).getName()+"/");
                }
                else{
                    builder.append(list.get(i).getName());
                }
            }
            return builder.toString();
        }
        return "佚名";
    }

    public static String formatN(List<String> genres) {
        if (genres != null && genres.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < genres.size(); i++) {
                if (i < genres.size() - 1) {
                    stringBuilder.append(genres.get(i)).append(" / ");
                } else {
                    stringBuilder.append(genres.get(i));
                }
            }
            return stringBuilder.toString();

        } else {
            return "不知名类型";
        }
    }




    public static String formatType(List<String> list){
        if(list!=null&&list.size()>0){
            StringBuilder builder=new StringBuilder("");
            for(int i=0;i<list.size();i++){
                if(i<list.size()-1){
                    builder.append(list.get(i)).append("/");
                }
                else{
                    builder.append(list.get(i));
                }
            }
            return builder.toString();
        }
        return  "不知名类型";
    }
}
