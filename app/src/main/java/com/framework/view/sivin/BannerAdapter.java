package com.framework.view.sivin;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * bannerAdapter
 * Created by sivin on 2016/5/1.
 */
public abstract class BannerAdapter<T> {
    private List<T> mDataList;

    List<T> getDataList() {
        return mDataList;
    }

    protected BannerAdapter(List<T> dataList) {
        mDataList = dataList;
    }

    void setImageViewSource(ImageView imageView, int position) {
        bindImage(imageView, mDataList.get(position));
    }

    void selectTips(TextView tv, int position) {
        if (mDataList != null && mDataList.size() > 0)
            bindTips(tv, mDataList.get(position));
    }

    public void addImage(T t) {
        mDataList.add(t);
    }

    public void addImages(List<T> list) {
        mDataList.addAll(list);
    }

    public void setImages(List<T> list) {
        mDataList.clear();
        mDataList.addAll(list);
    }

    public List<T> getmDataList() {
        return mDataList;
    }

    protected abstract void bindTips(TextView tv, T t);

    public abstract void bindImage(ImageView imageView, T t);


}
