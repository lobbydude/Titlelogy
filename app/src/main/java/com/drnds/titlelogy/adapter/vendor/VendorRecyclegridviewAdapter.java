package com.drnds.titlelogy.adapter.vendor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.drnds.titlelogy.activity.client.gridactivity.EditGridViewActivity;
import com.drnds.titlelogy.adapter.RecyclergridviewAdapter;
import com.drnds.titlelogy.model.GridItem;
import com.drnds.titlelogy.model.VendorGridItem;
import com.drnds.titlelogy.util.Logger;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by abishek on 9/14/2017.
 */

public class VendorRecyclegridviewAdapter extends RecyclerView.Adapter< VendorRecyclegridviewAdapter.MyViewHolder>implements Filterable {
    private ArrayList<VendorGridItem> vendorgridItemList;
    private ArrayList<VendorGridItem> mFilteredList;

    private Context context;

    SharedPreferences sharedpreferences;
    public static final String VENDORGRID = "vendorgridadpter";

    //    for animation
    private int lastPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView subclient,oderno,propertyaddress,producttype,state,county,status, barrowername;

        public MyViewHolder(View view) {
            super(view);

            subclient = (TextView) view.findViewById(R.id.vendor_grid_subclient);
            oderno = (TextView) view.findViewById(R.id.vendor_grid_orderno);
            producttype = (TextView) view.findViewById(R.id.vendor_gridview_producttype);
            state = (TextView) view.findViewById(R.id.vendor_gridview_state);
            county = (TextView) view.findViewById(R.id.vendor_gridview_county);
            propertyaddress = (TextView) view.findViewById(R.id.vendor_gridview_address);
            status = (TextView) view.findViewById(R.id.vendor_grid_status);
            barrowername = (TextView) view.findViewById(R.id.vendor_gridview_borrowername);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    Context context = v.getContext();
                    VendorGridItem vendorgridItem=vendorgridItemList.get(position);
                    Intent intent=new Intent(context, EditGridViewActivity.class);

                    sharedpreferences = context.getSharedPreferences(VENDORGRID, Context.MODE_PRIVATE);
                    //Creating editor to store values to shared preferences
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    //Adding values to editor

                    editor.putString("Order_Id",vendorgridItem.getOrderId());
                    editor.putString("Sub_Client_Name", vendorgridItem.getSubclient());
                    editor.putString("Product_Type", vendorgridItem.getProducttype());
                    editor.putString("State", vendorgridItem.getState());
                    editor.putString("County", vendorgridItem.getCounty());
                    editor.putString("Order_Priority", vendorgridItem.getOrderpriority());
                    editor.putString("Order_Assign_Type", vendorgridItem.getCountytype());
                    editor.putString("Progress_Status", vendorgridItem.getStatus());
                    editor.putString("Barrower_Name", vendorgridItem.getBarrowername());


                    editor.putString("Order_Status", vendorgridItem.getOrdertask());
                    Logger.getInstance().Log("orderid7777   : " + vendorgridItem.getOrderId());
                    editor.commit();
                    context.startActivity(intent);
                }
            });
        }
    }
    public VendorRecyclegridviewAdapter(ArrayList<VendorGridItem>vendorgridItemList) {
        this. vendorgridItemList=   vendorgridItemList;
        this. mFilteredList=   vendorgridItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_grid_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        VendorGridItem vendorgridItem = vendorgridItemList.get(position);
        holder.subclient.setText (mFilteredList.get(position).getSubclient());

        holder.oderno.setText( mFilteredList.get(position).getOderno());
        holder.producttype.setText( mFilteredList.get(position).getProducttype());
        holder.propertyaddress.setText( mFilteredList.get(position).getPropertyaddress());

        holder.state .setText( mFilteredList.get(position).getState());
        holder.county.setText( mFilteredList.get(position).getCounty());
        holder.status.setText( mFilteredList.get(position).getStatus());
        holder.barrowername.setText( mFilteredList.get(position).getBarrowername());


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
        return  mFilteredList.size();
    }
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = vendorgridItemList;
                } else {

                    ArrayList<VendorGridItem> filteredList = new ArrayList<>();

                    for (VendorGridItem vendorgridItem : vendorgridItemList) {

                        if (vendorgridItem.getSubclient().toLowerCase().contains(charString) ||
                                vendorgridItem.getOderno().toLowerCase().contains(charString) ||
                                vendorgridItem.getProducttype().toLowerCase().contains(charString)||
                                vendorgridItem.getPropertyaddress().toLowerCase().contains(charString)||
                                vendorgridItem.getState().toLowerCase().contains(charString)||
                                vendorgridItem.getCounty().toLowerCase().contains(charString)||
                                vendorgridItem.getCounty().toLowerCase().contains(charString)||
                                vendorgridItem.getBarrowername().toLowerCase().contains(charString))

                        {

                            filteredList.add(vendorgridItem);
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
                mFilteredList = (ArrayList<VendorGridItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}

