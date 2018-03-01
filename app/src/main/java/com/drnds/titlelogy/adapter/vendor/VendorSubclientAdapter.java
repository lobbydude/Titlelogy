package com.drnds.titlelogy.adapter.vendor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.subclientactivity.EditSubclientActivity;
import com.drnds.titlelogy.activity.vendor.clientactivity.EditClientActivityVendor;
import com.drnds.titlelogy.activity.vendor.subclientactivity.EditSubclientActivityVendor;
import com.drnds.titlelogy.adapter.RecyclerSubclientAdapter;
import com.drnds.titlelogy.adapter.VendorViewAdapter;
import com.drnds.titlelogy.model.Subclient;
import com.drnds.titlelogy.model.Vendor;
import com.drnds.titlelogy.model.VendorClient;
import com.drnds.titlelogy.model.VendorSubclient;
import com.drnds.titlelogy.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by abishek on 9/5/2017.
 */

public class VendorSubclientAdapter extends RecyclerView.Adapter< VendorSubclientAdapter.MyViewHolder>  {
    private List<VendorSubclient> vendorSubclientList;
    private Activity activity;
    private Context ctx;
    private ImageView imgedit;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView clientname,subprocessname,subcode,state,county,adress,invoicename;
        private List<VendorSubclient> vendorSubclientList=new ArrayList<VendorSubclient>();
        Context ctx;
        public MyViewHolder(View view, Context ctx, final List<VendorSubclient> vendorSubclientList) {
            super(view);
            this.vendorSubclientList=vendorSubclientList;
            this.ctx=ctx;

            clientname = (TextView) view.findViewById(R.id.clientnamevendor);
            subprocessname= (TextView) view.findViewById(R.id.subprocess_name_vendor);
            subcode= (TextView) view.findViewById(R.id.subprocess_code_vendor);
            state= (TextView) view.findViewById(R.id.state_vendor);
            county= (TextView) view.findViewById(R.id.county_vendor);
            adress= (TextView) view.findViewById(R.id.address_vendor);
            invoicename= (TextView) view.findViewById(R.id.invoice_contact_name_vendor);

            imgedit= (ImageView) view.findViewById(R.id.imageView_subedit_vendor);
            imgedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    VendorSubclient vendorSubclient=vendorSubclientList.get(position);
                    Context context = v.getContext();
                    Intent intent=new Intent(context, EditSubclientActivityVendor.class);
                    intent.putExtra("Sub_Client_Name",vendorSubclient.getClientname());
                    intent.putExtra("Invoice_Contact_Name",vendorSubclient.getInvoicename());
                    intent.putExtra("Address",vendorSubclient.getAddress());
                    intent.putExtra("Sub_Client_Id",vendorSubclient.getSubId());
                    context.startActivity(intent);
                }
            });


        }

    }
    public VendorSubclientAdapter(List<VendorSubclient>vendorSubclientList) {
        this. vendorSubclientList=   vendorSubclientList;
        this.ctx=ctx;
    }

    @Override
    public VendorSubclientAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_view_subclients_row, parent, false);

        return new VendorSubclientAdapter.MyViewHolder(itemView,ctx,vendorSubclientList);
    }

    @Override
    public void onBindViewHolder(VendorSubclientAdapter.MyViewHolder holder, int position) {
        VendorSubclient vendor = vendorSubclientList.get(position);
        holder.clientname.setText(  vendor .getClientname());
        holder.subprocessname.setText(  vendor .getSubprocessname());
        holder.subcode.setText(  vendor .getSubprocesscode());
        holder.state.setText(  vendor .getState());
        holder.county.setText(  vendor .getCounty());
        holder.adress.setText(  vendor .getAddress());
        holder.invoicename.setText(  vendor .getInvoicename());


    }
    @Override
    public int getItemCount() {

        return  vendorSubclientList.size();
    }
}

