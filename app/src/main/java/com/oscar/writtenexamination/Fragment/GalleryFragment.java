package com.oscar.writtenexamination.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.oscar.writtenexamination.R;
import com.oscar.writtenexamination.Widget.GalleryView;

import java.io.IOException;
import java.util.Objects;

/**
 * 图片选择Fragment
 */
public class GalleryFragment extends BottomSheetDialogFragment
        implements GalleryView.SelectedChangeListener{

    private GalleryView mGallery;
    private OnSelectedListener mListener;

    public GalleryFragment(){

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 返回一个复写的
        return new BottomSheetDialog(Objects.requireNonNull(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =   inflater.inflate(R.layout.fragment_gallery, container, true);
        mGallery = root.findViewById(R.id.fgGalleryView);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGallery.setup(getLoaderManager(), this);
    }

    @Override
    public void onSelectedCountChanged(int count) {
        // 如果选中的一张图片
        if (count > 0) {
            // 隐藏自己
            dismiss();
            if (mListener != null) {
                // 得到所有的选中的图片的路径
                String[] paths = mGallery.getSelectedPath();
                // 返回第一张
                try {
                    mListener.onSelectedImage(paths[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 取消和唤起者之间的应用，加快内存回收
                mListener = null;
            }
        }
    }

    /**
     * 设置事件监听，并返回自己
     *
     * @param listener OnSelectedListener
     * @return GalleryFragment
     */
    public GalleryFragment setListener(OnSelectedListener listener) {
        mListener = listener;
        return this;
    }

    /**
     * 选中图片的监听器
     */
    public interface OnSelectedListener {
        void onSelectedImage(String path) throws IOException;
    }


}
