package com.drnds.titlelogy.fragments.vendor.createsubclientfragvendor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.subclientactivity.SubClientActivity;
import com.drnds.titlelogy.adapter.VendorViewAdapter;
import com.drnds.titlelogy.adapter.vendor.VendorSubclientAdapter;
import com.drnds.titlelogy.model.Vendor;
import com.drnds.titlelogy.model.VendorSubclient;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by abishek on 9/5/2017.
 */

public class CreateSubclientFragmentVendor extends Fragment {
    private List<VendorSubclient> vendorSubclientList = new ArrayList<>();
    private RecyclerView recyclerView;
    private VendorSubclientAdapter mAdapter;
    SharedPreferences sp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.vendor_fragment_subclient, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.vendorrecyle_subclient);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);
        viewVendor();
        return view;
    }
    public String getVendorId() {

        return sp.getString("Vendor_Id", "");
    }
    public void viewVendor(){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.VENDORSUBCLIENT_URL + getVendorId(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.e("responce : ", "" + response.toString());
                    JSONArray jsonArray = response.getJSONArray("Users");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);

                        VendorSubclient vendor = new VendorSubclient();
                        vendor.setClientname(details.getString("Sub_Client_Name"));
                        vendor.setSubprocessname(details.getString("Sub_Client_Name"));
                        vendor.setSubprocesscode(details.getString("Sub_Process_Code"));
                        vendor.setState(details.getString("State"));
                        vendor.setCounty(details.getString("County"));
                        vendor.setInvoicename(details.getString("Invoice_Contact_Name"));
                        vendor.setAddress(details.getString("Address"));
                        vendor.setSubId(details.getString("Sub_Client_Id"));


                        vendorSubclientList.add(vendor);
                        mAdapter = new VendorSubclientAdapter(vendorSubclientList);
                        recyclerView.setAdapter(mAdapter);

                        mAdapter.notifyDataSetChanged();





                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


}
