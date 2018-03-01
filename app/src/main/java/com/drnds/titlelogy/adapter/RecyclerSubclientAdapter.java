package com.drnds.titlelogy.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.subclientactivity.EditSubclientActivity;
import com.drnds.titlelogy.model.Subclient;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.CustomRequest;
import com.drnds.titlelogy.util.Logger;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.dmoral.toasty.Toasty;

import static com.drnds.titlelogy.util.AppController.TAG;

/**
 * Created by Ajithkumar on 6/20/2017.
 */

public class RecyclerSubclientAdapter extends RecyclerView.Adapter< RecyclerSubclientAdapter.MyViewHolder>  {
    private List<Subclient> subclientList;
    private Activity activity;
    private Context ctx;

    private ProgressDialog pDialog;
    RecyclerSubclientAdapter madapter;
    String subid;

    //    for animation
    private int lastPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView custname,email ;
        private ImageView imgedit,imgdelete;
        private List<Subclient> subclientList=new ArrayList<Subclient>();
        Context ctx;

        public MyViewHolder(View view, final Context ctx, final List<Subclient>  subclientList) {
            super(view);
            this.subclientList=subclientList;
            this.ctx=ctx;

            custname = (TextView) view.findViewById(R.id.subcustname);
            email= (TextView) view.findViewById(R.id.subemail);
            imgedit= (ImageView) view.findViewById(R.id.imageView_subedit);
            imgdelete= (ImageView) view.findViewById(R.id.imageView_subdelete);
            imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder deleteDialog = new AlertDialog.Builder(ctx,R.style.MyAlertDialogStyle);
                    deleteDialog.setMessage("Are you sure you want to delete?");
                    deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int position=getAdapterPosition();
                            Subclient subclient=subclientList.get(position);
                            subid=subclient.getSubId();
                            deleteItem(getAdapterPosition());
                        }
                    })
                            .setNegativeButton("No", null)
                            .show();

                }
            });
            imgedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    Subclient subclient=subclientList.get(position);
                    Context context = v.getContext();
                    Intent intent=new Intent(context, EditSubclientActivity.class);
                    intent.putExtra("Sub_Client_Name",subclient.getCustname());
                    intent.putExtra("Email",subclient.getEmail());
                    intent.putExtra("City",subclient.getCity());
                    intent.putExtra("Address",subclient.getAddresss());
                    intent.putExtra("Zip_Code",subclient.getZip());
                    intent.putExtra("Alternative_Email",subclient.getAltemail());
                    intent.putExtra("Sub_Client_Id",subclient.getSubId());
                    intent.putExtra("State",subclient.getState());
                    intent.putExtra("County",subclient.getCounty());
                    context.startActivity(intent);
                }
            });

        }


    }
    public RecyclerSubclientAdapter(List<Subclient> subclientList, Context context) {
        this. subclientList=   subclientList;
        pDialog = new ProgressDialog(context,R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        this.ctx=context;
    }

    @Override
    public RecyclerSubclientAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subclientitem_row, parent, false);

        return new RecyclerSubclientAdapter.MyViewHolder(itemView,ctx,subclientList);
    }

    @Override
    public void onBindViewHolder(RecyclerSubclientAdapter.MyViewHolder holder, int position) {
        Subclient subclient = subclientList.get(position);
        holder.custname.setText(  subclient .getCustname());
        holder.email.setText(  subclient .getEmail());


//        for animation
        setAnimation(holder.itemView, position);
    }

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
        return  subclientList.size();
    }

    public void deleteItem(final int position){

        Logger.getInstance().Log("in update client id");

        showDialog();
        pDialog.setMessage("Updating ...");



        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.DLTSUBCLIENT_URL+subid,null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {

            try {
                boolean  error = response.getBoolean("error");
                if (!error){
                    Toasty.success( ctx,"Deleted Successfully...", Toast.LENGTH_SHORT, true).show();
                    subclientList.remove(position);
                    notifyItemRemoved(position);
                    hideDialog();
                }else{
                    Toasty.error( ctx,"Not Deleted ...", Toast.LENGTH_SHORT, true).show();
                    hideDialog();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            hideDialog();
            // Check for error node in json
            Log.d(TAG, response.toString());


        }



    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {


        }
    }) ;
    AppController.getInstance().addToRequestQueue(customRequest);
}

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}