package com.oscar.writtenexamination.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.oscar.writtenexamination.Enums.TopNavigationTab;
import com.oscar.writtenexamination.R;


public class TopNavigationAdapter extends RecyclerView.Adapter {

    private static final int REGULAR_ITEM_VIEW_TYPE = 0;
    private static final int SPECIAL_ITEM_VIEW_TYPE = 1;

    private static final int SPECIAL_ITEM_POSITION = 6;
    private static final int NO_SELECTION = -1;

    private int selectedPosition = NO_SELECTION;
    private int regularItemWidth;
    private int specialItemWidth;

    private TopNavigationListener listener;
    private Context mContext;


    public TopNavigationAdapter(Context context, TopNavigationListener listener, int selectedPosition) {
        this.mContext = context;
        this.listener = listener;
        this.selectedPosition = selectedPosition;
    }

    @Override
    public int getItemViewType(int position) {
        return position == SPECIAL_ITEM_POSITION ? SPECIAL_ITEM_VIEW_TYPE : REGULAR_ITEM_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (regularItemWidth == 0) {
            calculateCellWidth(parent);
        }

        int width = getItemWidth(viewType);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == REGULAR_ITEM_VIEW_TYPE) {
            View view = inflater.inflate(R.layout.item_top_tab_navigation_regular, parent, false);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
            lp.width = width;
//            lp.topMargin = UIUtils.getDimension(R.dimen.bottom_navigation_view_height) -
//                    UIUtils.getDimension(R.dimen.bottom_navigation_clickable_area);
            view.setLayoutParams(lp);
            return new RegularTabViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_top_tab_navigation_special, parent, false);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
            lp.width = width;
//            lp.topMargin = UIUtils.getDimension(R.dimen.bottom_navigation_view_height) -
//                    UIUtils.getDimension(R.dimen.bottom_navigation_clickable_area);
            view.setLayoutParams(lp);
            return new SpecialTabViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int itemViewType = getItemViewType(position);
        TopNavigationTab current = (TopNavigationTab.values()[position]);

        if (itemViewType == REGULAR_ITEM_VIEW_TYPE) {
            RegularTabViewHolder holder = (RegularTabViewHolder) viewHolder;

            boolean isSelected = position == selectedPosition;
            int resId = isSelected ? current.getSelectedResId() : current.getUnselectedResId();
            holder.iconIV.setImageResource(resId);
            holder.titleTV.setText(current.getTitleResId());
            holder.titleTV.setTextColor(isSelected ? current.getSelectedColorId() : current.getUnselectedColorId());
        } else {
            SpecialTabViewHolder holder = (SpecialTabViewHolder) viewHolder;
            holder.button.setImageResource(current.getSelectedResId());
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    private void calculateCellWidth(ViewGroup parent) {
        int parentWidth = parent.getWidth();
        int itemCount = getItemCount();
        int specialItemCount = TopNavigationTab.getSpecialItemCount();
        int regularItemCount = TopNavigationTab.values().length - specialItemCount;
        float specialItemWidthRatio = TopNavigationTab.SPECIAL_ITEM_WIDTH_RATIO;

        float cellWidth = parentWidth / itemCount;
        float specialItemsTotalWidth = cellWidth * specialItemCount * specialItemWidthRatio;
        float regularItemsTotalWidth = (parentWidth - specialItemsTotalWidth);

        regularItemWidth = (int) (regularItemsTotalWidth / regularItemCount);
        specialItemWidth = (int) (specialItemsTotalWidth / specialItemCount);
    }

    private int getItemWidth(int viewType) {
        boolean isRegularItem = viewType == REGULAR_ITEM_VIEW_TYPE;
        return isRegularItem ? regularItemWidth : specialItemWidth;
    }

    public void setSelectedItemPosition(int selectedPosition) {
        if (selectedPosition >= 0) {
            this.selectedPosition = selectedPosition;
            notifyDataSetChanged();
        }
    }

    public void onItemSelected(int selectedPosition) {
        if (listener != null) {
            if (selectedPosition == SPECIAL_ITEM_POSITION) {
                listener.onSpecialTabSelected();
                return;
            }
        }

        boolean isConsumed = listener != null && listener.onTabSelected(selectedPosition > SPECIAL_ITEM_POSITION ?
                selectedPosition - 1 : selectedPosition);
        if (!isConsumed) {
            return;
        }

        int aux = this.selectedPosition;
        this.selectedPosition = selectedPosition;

        if (aux == this.selectedPosition) {
            return;
        }

        notifyItemChanged(aux);
        notifyItemChanged(this.selectedPosition);
    }

    class RegularTabViewHolder extends RecyclerView.ViewHolder {

        ImageView iconIV;
        TextView titleTV;

        RegularTabViewHolder(View itemView) {
            super(itemView);
            iconIV = itemView.findViewById(R.id.ittnrView);
            titleTV = itemView.findViewById(R.id.ittnrTv);
            itemView.setOnClickListener(v -> onItemSelected(getAdapterPosition()));
        }
    }

    class SpecialTabViewHolder extends RecyclerView.ViewHolder {

        ImageButton button;

        SpecialTabViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.ibnst_special_btn);
            itemView.setOnClickListener(v -> onItemSelected(getAdapterPosition()));
        }
    }


    public interface TopNavigationListener {
        boolean onTabSelected(int pos);

        void onSpecialTabSelected();
    }
}
