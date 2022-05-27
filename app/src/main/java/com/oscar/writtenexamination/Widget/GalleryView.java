package com.oscar.writtenexamination.Widget;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oscar.writtenexamination.Adapter.RecyclerAdapter;
import com.oscar.writtenexamination.R;
import com.oscar.writtenexamination.Utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class GalleryView extends RecyclerView {

    private static final int LOADER_ID = 0x0100;
    private static final int MAX_IMAGE_COUNT = 3;//最大选中的图片数量
    private static final int MIN_IMAGE_FILE_SIZE = 10 * 1024;//最小的图片大小
    private LoaderCallback mLoaderCallback = new LoaderCallback();
    private Adapter mAdapter = new Adapter();
    //ArrayList遍历会比较快，但处于不断的添加删除更新的情况下会有性能消耗，而Linked不会有消耗
    private List<Image> mSelectedImages = new LinkedList<>();
    private SelectedChangeListener mListener;

    public GalleryView(@NonNull Context context) {
        super(context);
        init();
    }

    public GalleryView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleryView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        //设置布局管理器，1行4列
        setLayoutManager(new GridLayoutManager(getContext(),4));
        //设置适配器
        setAdapter(mAdapter);
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Image>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Image image) throws FileNotFoundException {
                // Cell点击操作，如果说点击是允许的，那么更新对应的Cell状态
                //然后更新界面，同理；如果说不能允许点击（已经达到最大的选中数量），则不刷新界面
                if (onItemSelectClick(image)){
                    holder.updateData(image);
                }
            }
        });
    }

    /**
     * 初始化Loader方法
     * @param loaderManager Loader管理器
     * @return 返回ID，可用于销毁Loader
     */
    public int setup(LoaderManager loaderManager, SelectedChangeListener listener){
        mListener = listener;
        loaderManager.initLoader(LOADER_ID,null,mLoaderCallback);
        return LOADER_ID;
    }

    /**
     * Cell点击的具体逻辑
     * @param image 图片
     * @return True，代表进了行数据更改，需要刷新；反之不刷新
     */
    private boolean onItemSelectClick(Image image) throws FileNotFoundException {
        //是否需要进行刷新
        boolean notifyRefresh;
        if (mSelectedImages.contains(image)){
            //如果之前存在则移除
            mSelectedImages.remove(image);
            //同时更改状态
            image.isSelect = false;
            notifyRefresh = true;
        }else {
            if (mSelectedImages.size() >= MAX_IMAGE_COUNT){
                notifyRefresh =false;
                ToastUtils.showShortToast(getContext(),"最多選擇" + MAX_IMAGE_COUNT + "張");
            }else {
                mSelectedImages.add(image);
                image.isSelect = true;
                notifyRefresh = true;
            }
        }
        //如果数据有更改，那么我们需要通知外面的监听者，数据选中改变
        if (notifyRefresh){
            notifySelectChanged();
        }
        return true;
    }

    /**
     * 得到选中的图片的全部地址
     * @return 返回路径数组
     */
    public String[] getSelectedPath(){
        String[] paths = new String[mSelectedImages.size()];
        int index = 0;
        for (Image image : mSelectedImages){
            //先取index，然后自增
            paths[index++] = image.path;
        }
        return paths;
    }

    /**
     * 可以进行清空选中的图片
     */
    private void clear() throws FileNotFoundException {
        for (Image image : mSelectedImages){
            //一定要先重置状态
            image.isSelect = false;
        }
        mSelectedImages.clear();
        //通知更新
        mAdapter.notifyDataSetChanged();
        //通知选中数量改变
        notifySelectChanged();
    }

    /**
     * 通知选中状态改变
     */
    private void notifySelectChanged() throws FileNotFoundException {
        //得到监听者并判断是否有监听者，然后回调数量变化
        SelectedChangeListener listener = mListener;
        if (listener != null){
            listener.onSelectedCountChanged(mSelectedImages.size());
        }
    }

    /**
     * 通知Adapter数据更改的方法
     * @param images 新的数据
     */
    private void updateSource(List<Image> images){
        mAdapter.replace(images);
    }

    /**
     * 用于实际的数据加载Loader Callback
     */
    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>{
        private final String[] IMAGE_PROJECTION = new String[]{
                MediaStore.Images.Media._ID,    //id
                MediaStore.Images.Media.DATA,   //图片路径
                MediaStore.Images.Media.DATE_ADDED     //图片的创建时间
        };


        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {
            //创建一个Loader
            if (id == LOADER_ID){
                //如果是本身的ID则可进行初始化
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        null,
                        null,
                        IMAGE_PROJECTION[2] + " DESC");//倒序查询
            }
            return null;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            //当Loader加载完成时
            List<Image> images = new ArrayList<>();
            //判断是否有数据
            if (data != null){
                int count = data.getCount();
                if (count > 0){
                    //移动游标至开始
                    data.moveToFirst();
                    // 得到对应的列的Index坐标
                    int indexId = data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]);
                    int indexPath = data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
                    int indexDate = data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]);

                    do {
                        //循环读取，直到没有下一条数据
                        int id = data.getInt(indexId);
                        String path = data.getString(indexPath);
                        long dateTime = data.getLong(indexDate);

                        File file = new File(path);
                        if (!file.exists() || file.length() < MIN_IMAGE_FILE_SIZE){
                            //如果没有图片或图片大小太小，则跳过
                            continue;
                        }

                        //添加一条新的数据
                        Image image = new Image();
                        image.id = id;
                        image.path = path;
                        image.date = dateTime;
                        images.add(image);
                    }while (data.moveToNext());
                }
            }
            updateSource(images);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            // 当Loader销毁或重置,进行界面清空操作
            updateSource(null);
        }
    }

    /**
     * 内部的数据结构
     */
    private static class Image{
        int id;     //数据的ID
        String path;    //图片的路径
        long date;      //图片的创建日期
        boolean isSelect;       //是否选中

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Image image = (Image) o;
            return Objects.equals(path, image.path);
        }

        @Override
        public int hashCode() {
            return Objects.hash(path);
        }
    }

    private class Adapter extends RecyclerAdapter<Image>{

        /**
         * @param position 坐标
         */
        @Override
        protected int getItemViewType(int position, Image image) {
            return R.layout.item_photo_choose;
        }

        @Override
        protected ViewHolder<Image> onCreateViewHolder(View root, int viewType) {
            return new GalleryView.ViewHolder(root);
        }
    }

    /**
     * cell对应的holder
     */
    private class ViewHolder extends RecyclerAdapter.ViewHolder<Image>{
        private ImageView mPic;
        private View mShade;
        private CheckBox mSelected;

        ViewHolder(View itemView) {
            super(itemView);

            mPic = itemView.findViewById(R.id.ipcImg);
            mShade = itemView.findViewById(R.id.ipcShade);
            mSelected = itemView.findViewById(R.id.ipcCheckbox);
        }

        @Override
        protected void onBind(Image image) {
            Glide.with(getContext())
                    .load(image.path) // 加载路径
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用缓存，直接从原图加载
                    .centerCrop() // 居中剪切
                    .placeholder(R.color.colorE0) // 默认颜色
                    .into(mPic);

            mShade.setVisibility(image.isSelect ? VISIBLE : INVISIBLE);
            mSelected.setChecked(image.isSelect);
            mSelected.setVisibility(VISIBLE);
        }

        @Override
        protected void onBind(Image image, List<Object> payloads) {

        }
    }

    /**
     * 对外的监听器
     */
    public interface SelectedChangeListener{
        void onSelectedCountChanged(int count) throws FileNotFoundException;
    }
}
