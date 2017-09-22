package com.example.zengqiang.mycloud.adapter;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zengqiang.mycloud.R;
import com.example.zengqiang.mycloud.app.MyApplication;
import com.example.zengqiang.mycloud.base.baseAdapter.BaseRecyclerAdapter;
import com.example.zengqiang.mycloud.base.baseAdapter.BaseRecyclerViewHolder;
import com.example.zengqiang.mycloud.bean.AndroidBean;
import com.example.zengqiang.mycloud.databinding.ItemEverydayFooterBinding;
import com.example.zengqiang.mycloud.databinding.ItemEverydayOneBinding;
import com.example.zengqiang.mycloud.databinding.ItemEverydayThreeBinding;
import com.example.zengqiang.mycloud.databinding.ItemEverydayTitleBinding;
import com.example.zengqiang.mycloud.databinding.ItemEverydayTwoBinding;
import com.example.zengqiang.mycloud.http.rx.RxBus;
import com.example.zengqiang.mycloud.http.rx.RxConstants;
import com.example.zengqiang.mycloud.utils.CommonUtils;
import com.example.zengqiang.mycloud.utils.ImgLoadUtils;
import com.example.zengqiang.mycloud.utils.PerfectListener;

import java.util.List;

/**
 * Created by zengqiang on 2017/8/30.
 */

public class EverydayAdapter extends BaseRecyclerAdapter<List<AndroidBean>> {
    private static final int TYPE_TITLE=1;
    private static final int TYPE_ONE=2;
    private static final int TYPE_TWO=3;
    private static final int TYPE_THREE=4;

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_TITLE:
                Log.e("xys","title holder");
                return new TitleHolder(parent,R.layout.item_everyday_title);
            case TYPE_ONE:
                return new OneHolder(parent,R.layout.item_everyday_one);
            case TYPE_TWO:
                return new TwoHolder(parent,R.layout.item_everyday_two);
           default:
                return new ThreeHolder(parent,R.layout.item_everyday_three);

        }
    }

    @Override
    public int getItemViewType(int position){
        Log.e("xys","chagndu"+getData().get(position).size());
        //Toast.makeText(MyApplication.getInstance(),"长度"+getData().get(position).size(),Toast.LENGTH_SHORT).show();
        if(getData().get(position).size()>0)
        if(!TextUtils.isEmpty(getData().get(position).get(0).getType_title())){
            return TYPE_TITLE;
        }
        else if(getData().get(position).size()==1){
            return TYPE_ONE;
        }
        else if(getData().get(position).size()==2){
            return TYPE_TWO;
        }
        else if(getData().get(position).size()==3){
            return TYPE_THREE;
        }
        return super.getItemViewType(position);
    }

    private class OneHolder extends BaseRecyclerViewHolder<List<AndroidBean>,ItemEverydayOneBinding>{

        public OneHolder(ViewGroup itemView, int layoutId) {
            super(itemView, layoutId);
        }

        @Override
        public void onBindViewHolder(List<AndroidBean> object, int position) {
            if(object.size()<=0)return;
            if("福利".equals(object.get(0).getType())){
                binding.oneText.setVisibility(View.GONE);
                binding.oneImgPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(binding.oneImgPhoto.getContext())
                        .load(object.get(0).getUrl())
                        .placeholder(R.drawable.img_one_bi_one)
                        .error(R.drawable.img_one_bi_one)
                        .crossFade(1500)
                        .into(binding.oneImgPhoto);
            }
            else{
                binding.oneText.setVisibility(View.VISIBLE);
                setDesc(object,0,binding.oneText);
                setDisplayRandom(1,0,binding.oneImgPhoto,object);
            }
            setOnClock(binding.llOnePhoto,object.get(0));
        }
    }

    private class TwoHolder extends BaseRecyclerViewHolder<List<AndroidBean>,ItemEverydayTwoBinding>{

        public TwoHolder(ViewGroup itemView, int layoutId) {
            super(itemView, layoutId);
        }

        @Override
        public void onBindViewHolder(List<AndroidBean> object, int position) {
            if(object.size()<=0)return;
            setDisplayRandom(2,0,binding.ivTwoOneOne,object);
            setDisplayRandom(2,1,binding.ivTwoOneTwo,object);
            setDesc(object,0,binding.tvTwoOneOneTitle);
            setDesc(object,1,binding.tvTwoOneTwoTitle);
            setOnClock(binding.llTwoOneOne,object.get(0));
            setOnClock(binding.llTwoOneTwo,object.get(1));
        }
    }

    private class ThreeHolder extends BaseRecyclerViewHolder<List<AndroidBean>,ItemEverydayThreeBinding>{

        public ThreeHolder(ViewGroup itemView, int layoutId) {
            super(itemView, layoutId);
        }

        @Override
        public void onBindViewHolder(List<AndroidBean> object, int position) {
            if(object.size()<=0)return;
            setDisplayRandom(3, 0, binding.ivThreeOneOne, object);
            setDisplayRandom(3, 1, binding.ivThreeOneTwo, object);
            setDisplayRandom(3, 2, binding.ivThreeOneThree, object);
            setOnClock(binding.llThreeOneOne, object.get(0));
            setOnClock(binding.llThreeOneTwo, object.get(1));
            setOnClock(binding.llThreeOneThree, object.get(2));
            setDesc(object, 0, binding.tvThreeOneOneTitle);
            setDesc(object, 1, binding.tvThreeOneTwoTitle);
            setDesc(object, 2, binding.tvThreeOneThreeTitle);
        }
    }

    private class TitleHolder extends BaseRecyclerViewHolder<List<AndroidBean>,ItemEverydayTitleBinding>{

        public TitleHolder(ViewGroup itemView, int layoutId) {
            super(itemView, layoutId);
        }

        @Override
        public void onBindViewHolder(List<AndroidBean> object, int position) {
            Log.e("xys","VIEWhOLDER");
            int index=0;
            String title=object.get(0).getType_title();
            binding.tvTitleType.setText(title);
            if("Android".equals(title)){

                binding.ivTitleType.setImageDrawable(CommonUtils.getDrawable(R.drawable.home_title_android));
                index=0;
            }
            else if("福利".equals(title)){
                binding.ivTitleType.setImageDrawable(CommonUtils.getDrawable(R.drawable.home_title_meizi));
                index=1;
            }
            else if("IOS".equals(title)){
                binding.ivTitleType.setImageDrawable(CommonUtils.getDrawable(R.drawable.home_title_ios));
                index=2;
            }
            else if("休息视频".equals(title)){
                binding.ivTitleType.setImageDrawable(CommonUtils.getDrawable(R.drawable.home_title_movie));
                index=2;
            }
            else if("拓展资源".equals(title)){
                binding.ivTitleType.setImageDrawable(CommonUtils.getDrawable(R.drawable.home_title_source));
                index=2;
            }
            else if("瞎推荐".equals(title)){
                binding.ivTitleType.setImageDrawable(CommonUtils.getDrawable(R.drawable.home_title_xia));
                index=2;
            }
            else if("前端".equals(title)){
                binding.ivTitleType.setImageDrawable(CommonUtils.getDrawable(R.drawable.home_title_qian));
                index=2;
            }
            else if("App".equals(title)){
                binding.ivTitleType.setImageDrawable(CommonUtils.getDrawable(R.drawable.home_title_app));
                index=2;
            }
            if(position!=0){
                binding.viewLine.setVisibility(View.VISIBLE);
            }
            else{
                binding.viewLine.setVisibility(View.GONE);
            }
            final int last=index;
            binding.llTitleMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RxBus.getInstance().post(RxConstants.JUMP_TYPE,last);
                }
            });
        }
    }

    private void setOnClock(final LinearLayout linearLayout,AndroidBean bean){
        linearLayout.setOnClickListener(new PerfectListener() {
            @Override
            public void onDoubleClick(View v) {

            }
        });
    }

    private void setDisplayRandom(int imgNumber, int position, ImageView imageView, List<AndroidBean> object){
        ImgLoadUtils.setDisplayRandom(imgNumber,object.get(position).getImage_url(),imageView);
    }

    private void setDesc(List<AndroidBean> object, int position, TextView textView){
        textView.setText(object.get(position).getDesc());
    }
}
