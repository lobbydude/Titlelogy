package com.drnds.titlelogy.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.createuseractivity.EditCreateUserActivity;
import com.drnds.titlelogy.model.CreateUser;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.CustomRequest;
import com.drnds.titlelogy.util.Logger;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import es.dmoral.toasty.Toasty;

import static com.drnds.titlelogy.util.AppController.TAG;

/**
 * Created by Ajithkumar on 6/21/2017.
 */

public class CreateUserAdapter extends RecyclerView.Adapter< CreateUserAdapter.MyViewHolder> {
    private List<CreateUser> createUserList;
    private Activity activity;
    private Context ctx;
    private String clientuserId;
    private ProgressDialog pDialog;

    //    for animation
    private int lastPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView fname,lname,email,altemail ;
        private ImageView imgedit,imgdelete;
        private List<CreateUser> createUserList=new ArrayList<CreateUser>();
        Context ctx;
        public MyViewHolder(View view, final Context ctx, final List<CreateUser> createUserList) {
            super(view);
            this.createUserList=createUserList;
            this.ctx=ctx;

            fname = (TextView) view.findViewById(R.id.userfname);
            lname= (TextView) view.findViewById(R.id.userlname);
            email= (TextView) view.findViewById(R.id.useremail);
            altemail= (TextView) view.findViewById(R.id.useraltemail);
            imgedit= (ImageView) view.findViewById(R.id.imageView_creedit);
            imgedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    CreateUser createUser=createUserList.get(position);
                    Context context = v.getContext();
                    Intent intent=new Intent(context, EditCreateUserActivity.class);
                    intent.putExtra("Client_User_Id",createUser.getClientUid());
                    intent.putExtra("First_Name",createUser.getFname());
                    intent.putExtra("Email",createUser.getEmail());
                    intent.putExtra("Last_Name",createUser.getLname());
                    intent.putExtra("Alternative_Email",createUser.getAltemail());

                    Logger.getInstance().Log("clientuserId " + createUser.getClientUid());
                    context.startActivity(intent);
                }
            });
            imgdelete= (ImageView) view.findViewById(R.id.imageView_credelete);
            imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder deleteDialog = new AlertDialog.Builder(ctx,R.style.MyAlertDialogStyle);
                    deleteDialog.setMessage("Are you sure you want to delete?");
                    deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int position=getAdapterPosition();
                            CreateUser createUser=createUserList.get(position);
                            clientuserId=createUser.getClientUid();
                            deleteUser(getAdapterPosition());
                            Logger.getInstance().Log("clientuserId " + clientuserId);
                        }
                    })
                            .setNegativeButton("No", null)
                            .show();
                }
            });

        }


    }

    public CreateUserAdapter(List<CreateUser>createUserList,Context context) {
        this. createUserList=   createUserList;
        pDialog = new ProgressDialog(context,R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        this.ctx=context;

    }

    @Override
    public CreateUserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.createuser_row, parent, false);

        return new CreateUserAdapter.MyViewHolder(itemView,ctx,createUserList);
    }

    @Override
    public void onBindViewHolder(CreateUserAdapter.MyViewHolder holder, int position) {
        CreateUser createUser = createUserList.get(position);
        holder.fname.setText(  createUser .getFname());
        holder.lname.setText(  createUser .getLname());
        holder.email.setText(  createUser .getEmail());
        holder.altemail.setText(  createUser .getAltemail());


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
        return  createUserList.size();
    }
    public void deleteUser(final int position){
        showDialog();
        pDialog.setMessage("Updating ...");
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.DELETECLIENTUSER_URL+clientuserId,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean error = response.getBoolean("error");
                    if (!error) {
                        Toasty.success(ctx, "Deleted Successfully...",  Toast.LENGTH_SHORT, true).show();

                        createUserList.remove(position);
                        notifyItemRemoved(position);
                        hideDialog();
                    } else {
                        Toasty.error(ctx, "Not Deleted ...",  Toast.LENGTH_SHORT, true).show();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialog();
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


