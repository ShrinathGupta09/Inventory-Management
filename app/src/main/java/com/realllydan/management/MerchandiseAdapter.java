package com.realllydan.management;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

public class MerchandiseAdapter extends RecyclerView.Adapter<MerchandiseAdapter.ViewHolder> {

    private static final String TAG = "MerchandiseAdapter";

    private ArrayList<Merchandise> mMerchandise = new ArrayList<>();
    private Context mContext;
    private OnMerchClickListener onMerchClickListener;

    public MerchandiseAdapter( Context mContext, ArrayList<Merchandise> mMerchandise, OnMerchClickListener onMerchClickListener) {
        this.mMerchandise = mMerchandise;
        this.mContext = mContext;
        this.onMerchClickListener = onMerchClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_merch_item, viewGroup, false);
        return new ViewHolder(view, onMerchClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called");

        viewHolder.tvMerchName.setText(mMerchandise.get(i).getMerch_name());
        viewHolder.tvMerchCost.setText(mMerchandise.get(i).getMerch_cost());
        viewHolder.tvMerchQuantity.setText(mMerchandise.get(i).getMerch_quantity());
        Glide.with(mContext).load(mMerchandise.get(i).getMerch_image_url()).into(viewHolder.ivMerchImage);
    }

    @Override
    public int getItemCount() {
        return mMerchandise.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ConstraintLayout mParentLayout;
        TextView tvMerchName, tvMerchCost, tvMerchQuantity;
        ImageView ivMerchImage;
        OnMerchClickListener onMerchClickListener;

        public ViewHolder(@NonNull View itemView, OnMerchClickListener onMerchClickListener) {
            super(itemView);
            mParentLayout = itemView.findViewById(R.id.mParentLayout);
            ivMerchImage = itemView.findViewById(R.id.ivMerchImage);
            tvMerchName = itemView.findViewById(R.id.tvMerchName);
            tvMerchCost = itemView.findViewById(R.id.tvMerchCost);
            tvMerchQuantity = itemView.findViewById(R.id.tvMerchQuantity);
            this.onMerchClickListener = onMerchClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMerchClickListener.onMerchClick(getAdapterPosition());
        }
    }

    public interface OnMerchClickListener {
        void onMerchClick(int position);
    }
}
