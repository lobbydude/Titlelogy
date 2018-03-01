package com.drnds.titlelogy.fragments.vendor.orderqueuefragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.orderqueueactivity.OrderQueueActivity;
import com.drnds.titlelogy.adapter.RecyclerOrderQueueAdapter;
import com.drnds.titlelogy.adapter.vendor.VendorRecyclerOrderQueueAdapter;
import com.drnds.titlelogy.model.OrderQueue;
import com.drnds.titlelogy.model.VendorOrderQueue;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by abishek on 9/15/2017.
 */


public class VendorOrderQueueFragment extends Fragment {

    private ArrayList<VendorOrderQueue> vendororderQueueList = new ArrayList<>();
    private RecyclerView recyclerView;
    private VendorRecyclerOrderQueueAdapter mAdapter;
    SharedPreferences sp;


    SwipeRefreshLayout mSwipeRefreshLayout;
    public static final String VENDORORDERPREFS_NAME = "OrderQueueFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.vendor_fragment_orderqueue, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.vendor_recycle_orderqueuef);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);
        mAdapter = new VendorRecyclerOrderQueueAdapter(vendororderQueueList);
//        recyclerView.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();





                // Fetching data from server
                prepareProcessingOrder();


        return view;
    }



    private void  prepareProcessingOrder(){


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.VENDORORDERQUEUE_URL + getVendorId(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object

                    JSONArray jsonArray = response.getJSONArray("ScoreBoard");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Logger.getInstance().Log("object is....: " + jsonObject);
                        String Order_Id = jsonObject.getString("Order_Id");
                        String subname = jsonObject.getString("Sub_Client_Name");
                        String orderno = jsonObject.getString("Order_Number");
                        VendorOrderQueue vendororderQueue = new VendorOrderQueue();
                        vendororderQueue.setSubclient(subname);
                        vendororderQueue.setOderno(orderno);

                        Logger.getInstance().Log("selected order id is : " + subname);

                        vendororderQueueList.add(vendororderQueue);

                        recyclerView.setAdapter(mAdapter);


                        mAdapter.notifyDataSetChanged();

                        // 0 for private mode

                        // 0 for private mode
                        SharedPreferences pref = getActivity().getSharedPreferences(VENDORORDERPREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("Order_Id", subname);
                        Logger.getInstance().Log("selected order id is : " + subname);
                        editor.commit();
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

    public String getVendorId() {

        return sp.getString("Vendor_Id", "");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setQueryHint("Search Orders");
        search(searchView);
        super.onCreateOptionsMenu(menu,inflater);
    }
    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return true;

            }
        });
    }
}





