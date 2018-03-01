package com.drnds.titlelogy.adapter.vendor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.createuseractivity.EditCreateUserActivity;
import com.drnds.titlelogy.activity.vendor.clientactivity.EditClientActivityVendor;
import com.drnds.titlelogy.adapter.CreateUserAdapter;
import com.drnds.titlelogy.model.CreateUser;
import com.drnds.titlelogy.model.VendorClient;
import com.drnds.titlelogy.model.VendorSubclient;
import com.drnds.titlelogy.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by abishek on 9/6/2017.
 */

public class VendorClientAdapter extends RecyclerView.Adapter< VendorClientAdapter.MyViewHolder>{

    private List<VendorClient> vendorClientList;
    private Activity activity;
    private Context ctx;


    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView companyname,state,county,clientcode ;
        private ImageView imgedit;
        private List<VendorClient> vendorClientList=new ArrayList<VendorClient>();
        Context ctx;

        public MyViewHolder(View view, final Context ctx, final List<VendorClient> vendorClientList) {
            super(view);
            this.vendorClientList=vendorClientList;
            this.ctx=ctx;

            companyname = (TextView) view.findViewById(R.id.companyname_client);
            state= (TextView) view.findViewById(R.id.statename_client);
            county= (TextView) view.findViewById(R.id.countyname_client);
            clientcode= (TextView) view.findViewById(R.id.clientcode_client);
            imgedit= (ImageView) view.findViewById(R.id.client_vedor_edit);
            imgedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    VendorClient vendorClient=vendorClientList.get(position);
                    Context context = v.getContext();
                    Intent intent=new Intent(context, EditClientActivityVendor.class);
                    intent.putExtra("Client_Code",vendorClient.getClientcode());
                    intent.putExtra("Client_Id",vendorClient.getClientId());


                    context.startActivity(intent);
                }
            });


        }


    }

    public VendorClientAdapter(List<VendorClient>vendorClientList) {
        this. vendorClientList=   vendorClientList;
        this.ctx=ctx;

    }

    @Override
    public VendorClientAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_view_clients_row, parent, false);

        return new VendorClientAdapter.MyViewHolder(itemView,ctx,vendorClientList);
    }

    @Override
    public void onBindViewHolder(VendorClientAdapter.MyViewHolder holder, int position) {
        VendorClient createClient = vendorClientList.get(position);
        holder.companyname.setText(  createClient .getCompanyname());
        holder.state.setText(  createClient .getState());
        holder.county.setText(  createClient .getCounty());
        holder.clientcode.setText(  createClient .getClientcode());


    }

    @Override
    public int getItemCount() {
        return  vendorClientList.size();
    }


}
