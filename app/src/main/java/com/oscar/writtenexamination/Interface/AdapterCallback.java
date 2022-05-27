package com.oscar.writtenexamination.Interface;

import com.oscar.writtenexamination.Adapter.RecyclerAdapter;

public interface AdapterCallback<Data> {
    void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);
}
