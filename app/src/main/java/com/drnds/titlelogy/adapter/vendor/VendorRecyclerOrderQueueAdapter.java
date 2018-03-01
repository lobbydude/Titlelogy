package com.drnds.titlelogy.adapter.vendor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.orderqueueactivity.EditOrderActivity;
import com.drnds.titlelogy.activity.vendor.orderqueueactivity.EditOrderActivityVendor;
import com.drnds.titlelogy.adapter.RecyclerOrderQueueAdapter;
import com.drnds.titlelogy.adapter.VendorViewAdapter;
import com.drnds.titlelogy.model.OrderQueue;
import com.drnds.titlelogy.model.Vendor;
import com.drnds.titlelogy.model.VendorOrderQueue;
import com.drnds.titlelogy.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by abishek on 9/15/2017.
 */

public class VendorRecyclerOrderQueueAdapter extends RecyclerView.Adapter<VendorRecyclerOrderQueueAdapter.MyViewHolder> {

    private List<VendorOrderQueue> vendorList;
    private Activity activity;
    private Context ctx;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView subclient,orderno;
        private List<VendorOrderQueue> vendorList=new ArrayList<VendorOrderQueue>();
        Context ctx;
        public MyViewHolder(View view,Context ctx,List<VendorOrderQueue> vendorList) {
            super(view);
            this.vendorList=vendorList;
            this.ctx=ctx;

            subclient = (TextView) view.findViewById(R.id.vendor_text_subclient);
            orderno = (TextView) view.findViewById(R.id.vendor_text_orderno);



        }

    }
    public VendorRecyclerOrderQueueAdapter(List<VendorOrderQueue>vendorList) {
        this. vendorList=   vendorList;
        this.ctx=ctx;
    }

    @Override
    public VendorRecyclerOrderQueueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_orderqueue_row, parent, false);

        return new VendorRecyclerOrderQueueAdapter.MyViewHolder(itemView,ctx,vendorList);
    }

    @Override
    public void onBindViewHolder(VendorRecyclerOrderQueueAdapter.MyViewHolder holder, int position) {
        VendorOrderQueue vendor = vendorList.get(position);
        holder.subclient.setText(  vendor .getSubclient());
        holder.orderno.setText(  vendor .getOderno());





    }
    @Override
    public int getItemCount() {

        return  vendorList.size();
    }

}

