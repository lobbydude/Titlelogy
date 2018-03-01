package com.drnds.titlelogy.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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
import com.drnds.titlelogy.fragments.client.orderqueuefragment.EditOrderInfoFragment;
import com.drnds.titlelogy.fragments.client.orderqueuefragment.OrderQueueFragment;
import com.drnds.titlelogy.model.OrderQueue;
import com.drnds.titlelogy.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ajithkumar on 6/28/2017.
 */
public class RecyclerOrderQueueAdapter extends RecyclerView.Adapter<RecyclerOrderQueueAdapter.MyViewHolder>implements Filterable {

    private ArrayList<OrderQueue> orderQueueList;
    private ArrayList<OrderQueue> mFilteredList;
    private Context ctx;

    //    for animation
    private int lastPosition = -1;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView subclient,oderno,propertyaddress,producttype,state,county,status,barrowername;
        private List<OrderQueue> orderQueueList=new ArrayList<OrderQueue>();

        public MyViewHolder(View view,Context ctx,List<OrderQueue> orderQueueList) {
            super(view);

            this.orderQueueList=orderQueueList;
            subclient = (TextView) view.findViewById(R.id.text_subclient);
            oderno = (TextView) view.findViewById(R.id.text_orderno);
            producttype = (TextView) view.findViewById(R.id.producttype);
            state = (TextView) view.findViewById(R.id.statename);
            county = (TextView) view.findViewById(R.id.countyname);
            propertyaddress = (TextView) view.findViewById(R.id.property_address);
            status = (TextView) view.findViewById(R.id.status);
            barrowername = (TextView) view.findViewById(R.id.borrowername);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();


            OrderQueue orderQueue=this.orderQueueList.get(position);
            Context context = v.getContext();
            Intent intent=new Intent(context, EditOrderActivity.class);
            intent.putExtra("Order_Id",orderQueue.getOrder_Id());
            intent.putExtra("Sub_Client_Name",orderQueue.getSubclient());
            intent.putExtra("State",orderQueue.getState());
            intent.putExtra("County",orderQueue.getCounty());
            intent.putExtra("Order_Priority",orderQueue.getOrderPriority());
            intent.putExtra("Product_Type",orderQueue.getProducttype());
            intent.putExtra("Order_Status",orderQueue.getOrdertask());
            intent.putExtra("Order_Assign_Type",orderQueue.getCountytype());
            intent.putExtra("Progress_Status",orderQueue.getStatus());
            intent.putExtra("Progress_Status",orderQueue.getStatus());
            intent.putExtra("Barrower_Name",orderQueue.getStatus());

//            intent.putExtra("Order_Id",orderQueue.getOrder_Id());

            context.startActivity(intent);
        }
    }


    public RecyclerOrderQueueAdapter(ArrayList<OrderQueue>orderQueueList) {
        this. orderQueueList=   orderQueueList;
        this. mFilteredList=   orderQueueList;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orderqueue_row, parent, false);

        return new MyViewHolder(itemView,ctx,orderQueueList);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        OrderQueue orderQueue = orderQueueList.get(position);
        holder.subclient.setText( mFilteredList.get(position).getSubclient());
        holder.oderno.setText(  mFilteredList.get(position).getOderno());
        holder.producttype.setText( mFilteredList.get(position).getProducttype());
        holder.propertyaddress.setText( mFilteredList.get(position).getPropertyaddress());
        holder.barrowername .setText( mFilteredList.get(position).getBarrowername());
        holder.state .setText( mFilteredList.get(position).getState());
        holder.county.setText( mFilteredList.get(position).getCounty());
        holder.status.setText(  mFilteredList.get(position).getStatus());
        holder.barrowername.setText(  mFilteredList.get(position).getBarrowername());

        //        for animation
        setAnimation(holder.itemView, position);
    }

    //    for animation
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.ABSOLUTE, 0.3f, Animation.ABSOLUTE, 0.3f);
            anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = orderQueueList;
                } else {

                    ArrayList<OrderQueue> filteredList = new ArrayList<>();

                    for (OrderQueue orderQueue : orderQueueList) {

                        if (orderQueue.getSubclient().toLowerCase().contains(charString) ||
                                orderQueue.getOderno().toLowerCase().contains(charString) ||
                                orderQueue.getProducttype().toLowerCase().contains(charString)||
                                orderQueue.getPropertyaddress().toLowerCase().contains(charString)||
                                orderQueue.getState().toLowerCase().contains(charString)||
                                orderQueue.getCounty().toLowerCase().contains(charString)||
                                orderQueue.getBarrowername().toLowerCase().contains(charString)||
                                orderQueue.getStatus().toLowerCase().contains(charString))
                        {

                            filteredList.add(orderQueue);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<OrderQueue>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}