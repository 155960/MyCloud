package com.example.zengqiang.mycloud.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zengqiang.mycloud.R;
import com.example.zengqiang.mycloud.app.MyApplication;
import com.example.zengqiang.mycloud.base.baseAdapter.BaseRecyclerAdapter;
import com.example.zengqiang.mycloud.base.baseAdapter.BaseRecyclerViewHolder;
import com.example.zengqiang.mycloud.bean.GankIoBean;
import com.example.zengqiang.mycloud.bean.GankIoDataBean;
import com.example.zengqiang.mycloud.databinding.AndroidItemBinding;
import com.example.zengqiang.mycloud.utils.ImgLoadUtils;

/**
 * Created by zengqiang on 2017/9/19.
 */

public class AndroidAdapter extends BaseRecyclerAdapter<GankIoDataBean.ResultBean> {

    private boolean isAll;

    public void setAll(boolean isAll){
        this.isAll=isAll;
    }
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.android_item);
    }
    private class ViewHolder extends BaseRecyclerViewHolder<GankIoDataBean.ResultBean,AndroidItemBinding>{

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
        }

        @Override
        public void onBindViewHolder(GankIoDataBean.ResultBean object, int position) {
            if(isAll&&("福利").equals(object.getType())){
                binding.androidItemLarge.setVisibility(View.VISIBLE);
                binding.androidSmall.setVisibility(View.GONE);
                binding.androidContent.setVisibility(View.GONE);
                ImgLoadUtils.displayEspImage(object.getUrl(),binding.androidSmall,1);
            }else {
                binding.androidContent.setVisibility(View.VISIBLE);
                binding.androidItemLarge.setVisibility(View.GONE);
                binding.androidSmall.setVisibility(View.VISIBLE);
            }
            if(isAll){
                binding.androidType.setVisibility(View.VISIBLE);
                binding.androidType.setText(" · "+object.getType());
            }
            else{
                binding.androidType.setVisibility(View.GONE);
            }
            binding.setBean(object);

            if(object.getImages()!=null&&object.getImages().size()>0
                    &&! TextUtils.isEmpty(object.getImages().get(0))){
                binding.androidSmall.setVisibility(View.VISIBLE);
                Toast.makeText(MyApplication.getInstance(),"url"+object.getImages().get(0),
                        Toast.LENGTH_SHORT).show();
                ImgLoadUtils.displayGif(object.getImages().get(0),binding.androidSmall);
            }else{
                binding.androidSmall.setVisibility(View.GONE);
            }

            binding.androidCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MyApplication.getInstance(),"点击",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
