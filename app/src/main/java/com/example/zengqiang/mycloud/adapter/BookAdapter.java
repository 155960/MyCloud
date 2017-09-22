package com.example.zengqiang.mycloud.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zengqiang.mycloud.R;
import com.example.zengqiang.mycloud.app.MyApplication;
import com.example.zengqiang.mycloud.bean.book.BookBean;
import com.example.zengqiang.mycloud.databinding.BookFooterItemBinding;
import com.example.zengqiang.mycloud.databinding.BookHeaderItemBinding;
import com.example.zengqiang.mycloud.databinding.BookItemBinding;
import com.example.zengqiang.mycloud.ui.book.BookDetailActivity;
import com.example.zengqiang.mycloud.utils.PerfectListener;

import java.util.List;

/**
 * Created by zengqiang on 2017/9/13.
 */

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int LOAD_MORE = 0;
    public static final int LOAD_PULL_TO = 1;
    public static final int LOAD_NONE = 2;
    public static final int LOAD_END = 3;

    private Activity activity;
    private int state=1;
    private static int FOOTER_BOOK = -2;
    private static int HEADER_BOOK = -3;
    private static int CONTENT_BOOK = -4;

    private List<BookBean> list;

    public BookAdapter(List<BookBean> list,Activity activity){
        this.list=list;
        this.activity=activity;
    }

    public void setState(int a){
        this.state=a;
        notifyDataSetChanged();
    }

    public int getState(){
        return state;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==HEADER_BOOK){
            BookHeaderItemBinding binding=DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.book_header_item,null,false);
            return new HeaderHolder(binding.getRoot());
        }
        else if(viewType==FOOTER_BOOK){
            BookFooterItemBinding binding=DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.book_footer_item,null,false);
            return new FooterHolder(binding.getRoot());
        }else{
            BookItemBinding binding=DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.book_item,null,false);
            return new ContentHolder(binding.getRoot());
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(isHeader(position)){

        }
        else if (holder instanceof FooterHolder){
            ((FooterHolder)holder).bindItem();
        }
        else {
            if(list!=null&&list.size()>0)
            ((ContentHolder)holder).bindItem(list.get(position-1));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(isHeader(position)){
            return HEADER_BOOK;
        }
        else if(isFooter(position)){
            return FOOTER_BOOK;
        }
        else return CONTENT_BOOK;
    }

    private boolean isHeader(int position){
        if(position>=0&&position<1)
            return true;
        return false;
    }

    private boolean isFooter(int position){
        if(position<getItemCount()&&position>=getItemCount()-1){
            return true;
        }
        return false;
    }


    @Override
    public int getItemCount() {
        return list.size()+2;
    }

    private class FooterHolder extends RecyclerView.ViewHolder{
        public BookFooterItemBinding binding;

        public FooterHolder(View itemView) {
            super(itemView);
            binding= DataBindingUtil.getBinding(itemView);
        }

        public void bindItem(){
            switch (state){
                case LOAD_MORE:
                    binding.bookFooterProgress.setVisibility(View.VISIBLE);
                    binding.bookFooterText.setText("正在加载...");
                    itemView.setVisibility(View.VISIBLE);
                    break;
                case LOAD_PULL_TO:
                    binding.bookFooterProgress.setVisibility(View.GONE);
                    binding.bookFooterText.setText("上拉加载更多");
                    itemView.setVisibility(View.VISIBLE);
                    break;
                case LOAD_NONE:
                    binding.bookFooterProgress.setVisibility(View.GONE);
                    binding.bookFooterText.setText("没有更多了");
                    itemView.setVisibility(View.VISIBLE);
                    break;
                case LOAD_END:
                    itemView.setVisibility(View.GONE);
                    break;

            }
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder{
        public BookHeaderItemBinding binding;
        public HeaderHolder(View itemView) {
            super(itemView);
            binding=DataBindingUtil.getBinding(itemView);
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams layoutParams=holder.itemView.getLayoutParams();
        if(layoutParams!=null&&layoutParams instanceof StaggeredGridLayoutManager.LayoutParams
                &&(isHeader(holder.getLayoutPosition())||isFooter(holder.getLayoutPosition()))){
            ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager manager=recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager){
            ((GridLayoutManager) manager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return isFooter(position)||isHeader(position)?((GridLayoutManager) manager).getSpanCount():1;
                }
            });
        }
    }

    private class ContentHolder extends RecyclerView.ViewHolder{
        public BookItemBinding binding;
        public ContentHolder(View itemView) {
            super(itemView);
            binding=DataBindingUtil.getBinding(itemView);
        }

        public void bindItem(final BookBean bookBean){
            binding.setBean(bookBean);
            binding.executePendingBindings();
            binding.bookTop.setOnClickListener(new PerfectListener() {
                @Override
                public void onDoubleClick(View v) {
                    BookDetailActivity.startActivity(activity,bookBean,binding.bookAdapterImg);
                }
            });
        }
    }

    public void addAll(List<BookBean> list){
        this.list.addAll(list);
    }
}
