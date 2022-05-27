package com.oscar.writtenexamination.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oscar.writtenexamination.R;

import java.util.List;

public class VersionAdapter extends BaseAdapter {

    private Context context;
    private List<String> contentList;

    public VersionAdapter(Context context, List<String> list) {
        super();
        this.context = context;
        this.contentList = list;
    }

    @Override
    public int getCount() {
        return  contentList.size();
    }

    @Override
    public Object getItem(int i) {
        return contentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dialog_cell_version, null);
        }
        String content = contentList.get(i);
        TextView tvContent = view.findViewById(R.id.dcvTv);
        if (!TextUtils.isEmpty(content)){
            tvContent.setText((i+1) + ". " + content);
        }
        return view;
    }
}
