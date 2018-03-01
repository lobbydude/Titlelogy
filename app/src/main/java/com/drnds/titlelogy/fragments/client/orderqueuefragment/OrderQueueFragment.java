package com.drnds.titlelogy.fragments.client.orderqueuefragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.drnds.titlelogy.model.OrderQueue;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Ajithkumar on 5/17/2017.
 */

public class OrderQueueFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ArrayList<OrderQueue> orderQueueList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerOrderQueueAdapter mAdapter;
    SharedPreferences sp;
    private String  urlorder= "https://titlelogy.com/Final_Api/api/Order_Queue/";
    private FloatingActionButton fab;
    SwipeRefreshLayout mSwipeRefreshLayout;
    public static final String ORDERPREFS_NAME = "OrderQueueFragment";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_orderqueue, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_orderqueue);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);
        mAdapter = new RecyclerOrderQueueAdapter(orderQueueList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        fab = (FloatingActionButton) view.findViewById(R.id.fab_orderqueue);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), OrderQueueActivity.class);
                startActivity(intent);
            }
        });

        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.ordersswipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);

        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);

                // Fetching data from server
                prepareProcessingOrder();

            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    public void onRefresh() {

        // Fetching data from server
        prepareProcessingOrder();
        orderQueueList.clear();
        mAdapter.notifyDataSetChanged();
    }
//
    private void  prepareProcessingOrder(){
        mSwipeRefreshLayout.setRefreshing(true);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,Config.ORDERQUEUE_URL + getClientId(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object

                    JSONArray jsonArray = response.getJSONArray("View_Order_Info");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);


                        String Order_Id = jsonObject.getString("Order_Id");

                        OrderQueue orderQueue = new OrderQueue();
                        orderQueue.setSubclient(jsonObject.getString("Sub_Client_Name"));
                        orderQueue.setOderno(jsonObject.getString("Order_Number"));
                        orderQueue.setStatus(jsonObject.getString("Progress_Status"));
                        orderQueue.setProducttype(jsonObject.getString("Product_Type"));
                        orderQueue.setState(jsonObject.getString("State"));
                        orderQueue.setCounty(jsonObject.getString("County"));
                        orderQueue.setPropertyaddress(jsonObject.getString("Property_Address"));
                        orderQueue.setOrderPriority(jsonObject.getString("Order_Priority"));
                        orderQueue.setOrder_Id(jsonObject.getString("Order_Id"));
                        orderQueue.setOrdertask(jsonObject.getString("Order_Status"));
                        orderQueue.setCountytype(jsonObject.getString("Order_Assign_Type"));
                        orderQueue.setBarrowername(jsonObject.getString("Barrower_Name"));


                        orderQueueList.add(orderQueue);
                        mAdapter = new RecyclerOrderQueueAdapter(orderQueueList);
                        recyclerView.setAdapter(mAdapter);


                        mAdapter.notifyDataSetChanged();

                        // 0 for private mode

                        // 0 for private mode
                        SharedPreferences pref = getActivity().getSharedPreferences(ORDERPREFS_NAME,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("Order_Id", Order_Id);
                        Logger.getInstance().Log("selected order id is : " + Order_Id);
                        editor.commit();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSwipeRefreshLayout.setRefreshing(false);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
    public String getClientId() {

        return sp.getString("Client_Id", "");
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
                mAdapter.getFilter().filter(newText);

                return true;

            }
        });
    }
}


