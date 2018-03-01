package com.drnds.titlelogy.fragments.client.scoreboardfragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.gridactivity.GridviewActivity;
import com.drnds.titlelogy.adapter.ProcessingTabGridAdapter;
import com.drnds.titlelogy.model.ProcessTab;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by ajithkumar on 6/26/2017.
 */

public class ProcessingTabFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ArrayList<ProcessTab> processTablist = new ArrayList<>();
    private ArrayList<String> griddata;
    GridView gv;
    private ProcessingTabGridAdapter mAdapter;
    SharedPreferences sp;
    private String  urlJsonObj = "https://titlelogy.com/Final_Api/api/View_Orders/";
    String Pass_Order_Task_Id,Pass_Order_Status_Id;
    String Pass_Order_Filter;
    public static final String MY_PREFS_NAME= "Griview";
    SharedPreferences pref;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialog pDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_processingtab, container, false);
        gv= (GridView) view.findViewById(R.id.gridview_proceesingtab);
        mAdapter = new ProcessingTabGridAdapter(getActivity(),processTablist);
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);
        pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
       pref = getActivity().getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE);
        griddata = new ArrayList<String>();

        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.processswipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);

        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);

                // Fetching data from server
            }
        });


        prepareProcessingOrder();
        checkInternetConnection();

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProcessTab item = (ProcessTab) parent.getItemAtPosition(position);
                showDialog();
                pDialog.setMessage("Loading ...");
                switch (position) {
                    case 0:
                        Pass_Order_Task_Id = "0";
                                Pass_Order_Status_Id = "13";
                                Pass_Order_Filter = "NEW_ORDERS";
                        fireEvent();
                        break;
                    case 1:
                        Pass_Order_Task_Id = "2";
                        Pass_Order_Status_Id = "0";
                        Pass_Order_Filter = "GET_PROCESSING_ORDERS";
                        fireEvent();
                        break;

                    case 2:
                        Pass_Order_Task_Id = "3";
                        Pass_Order_Status_Id = "0";
                        Pass_Order_Filter = "GET_PROCESSING_ORDERS";
                        fireEvent();
                        break;
                    case 3:
                        Pass_Order_Task_Id = "4";
                        Pass_Order_Status_Id = "0";
                        Pass_Order_Filter = "GET_PROCESSING_ORDERS";
                        fireEvent();
                        break;
                    case 4:
                        Pass_Order_Task_Id = "7";
                        Pass_Order_Status_Id = "0";
                        Pass_Order_Filter = "GET_PROCESSING_ORDERS";
                        fireEvent();
                        break;
                    case 5:
                        Pass_Order_Task_Id = "18";
                        Pass_Order_Status_Id = "0";
                        Pass_Order_Filter = "GET_PROCESSING_ORDERS";
                        fireEvent();
                        break;
                    case 6:
                        Pass_Order_Task_Id = "12";
                        Pass_Order_Status_Id = "0";
                        Pass_Order_Filter = "GET_PROCESSING_ORDERS";
                        fireEvent();
                        break;
                    case 7:
                        Pass_Order_Task_Id = "0";
                        Pass_Order_Status_Id = "0";
                        Pass_Order_Filter = "COMPLETED_ORDERS";
                        fireEvent();
                        break;
                }


            }
        });
        return view;
    }
    public String getClientId() {

        return sp.getString("Client_Id", "");
    }
    public void  prepareProcessingOrder() {
        mSwipeRefreshLayout.setRefreshing(true);
        showDialog();
        pDialog.setMessage("Loading ...");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.SCOREBOARD_URL+getClientId() , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                hideDialog();
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object

                    JSONArray jsonArray=response.getJSONArray("ScoreBoard");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);

                        String NEW_ORDERS = jsonObject.getString("NEW_ORDERS");

                        String SEARCH_ORDES = jsonObject.getString("SEARCH_ORDES");
                        String SEARCH_QC_ORDERS =jsonObject.getString("SEARCH_QC_ORDERS");
                        String COMPLETED_ORDERS =jsonObject.getString("COMPLETED_ORDERS");

                        ProcessTab process=new ProcessTab(jsonObject.getString("NEW_ORDERS"),"New Orders");
                        processTablist.add(process);
                        process=new ProcessTab(jsonObject.getString("SEARCH_ORDES"),"Title Search");
                        processTablist.add(process);
                        process=new ProcessTab(jsonObject.getString("SEARCH_QC_ORDERS"),"Title Search qc");
                        processTablist.add(process);
                        process=new ProcessTab(jsonObject.getString("TYPING_ORDERS"),"Property Typing");
                        processTablist.add(process);
                        process=new ProcessTab(jsonObject.getString("TYPING_QC_ORDERS"),"Property Typing qc");
                        processTablist.add(process);
                        process=new ProcessTab(jsonObject.getString("TAX_ORDERS"),"Tax Certificate");
                        processTablist.add(process);
                        process=new ProcessTab(jsonObject.getString("FINAL_REVIEW_ORDERS"),"Final Review");
                        processTablist.add(process);
                        process=new ProcessTab(jsonObject.getString("COMPLETED_ORDERS"),"Completed");
                        processTablist.add(process);

                        Logger.getInstance().Log("COMPLETED_ORDERS is : " + COMPLETED_ORDERS);



                        gv.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                        // 0 for private mode




                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSwipeRefreshLayout.setRefreshing(false);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();

            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
    public void fireEvent(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                urlJsonObj, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
//                 hideDialog();
                stopDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.d(TAG, response.toString());
                    boolean  error = jObj.getBoolean("error");

                    Logger.getInstance().Log("in error response"+response);
                    // Check for error node in json
                    if (!error) {
                        JSONArray jsonArray=jObj.getJSONArray("View_Order_Info");
//                        hideDialog();
                        stopDialog();


                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String Sub_Client_Name = jsonObject.getString("Sub_Client_Name");
                            String Order_Number = jsonObject.getString("Order_Number");
                            String Status = jsonObject.getString("Status");
                            String Product_Type = jsonObject.getString("Product_Type");
                            String State = jsonObject.getString("State");
                            String County = jsonObject.getString("County");
                            String Property_Address = jsonObject.getString("Property_Address");
                            String Order_Id = jsonObject.getString("Order_Id");
                            String Order_Status = jsonObject.getString("Order_Status");




                            SharedPreferences.Editor edt = pref.edit();
                            edt.putString("Sub_Client_Name", Sub_Client_Name);
                            edt.putString("Order_Number", Order_Number);
                            edt.putString("Status", Status);
                            edt.putString("Product_Type", Product_Type);
                            edt.putString("State", State);
                            edt.putString("County", County);
                            edt.putString("Property_Address", Property_Address);
                            edt.putString("Order_Id", Order_Id);
                            edt.commit();
                        }
                        Intent intent = new Intent(getActivity(),GridviewActivity.class);

                        intent.putExtra("JSON", response.toString());
                        startActivity(intent);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideDialog();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Client_Id",getClientId());
                params.put("Order_Task_id",Pass_Order_Task_Id);
                params.put("Order_Status_Id", Pass_Order_Status_Id );
                params.put("Order_Filter",Pass_Order_Filter);


                return params;
            }
        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                =(ConnectivityManager)getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED  ) {
            TastyToast.makeText( getActivity(),"Check Internet Connection",TastyToast.LENGTH_SHORT,TastyToast.INFO);
//            Snackbar snackbar = Snackbar.make(snackbarCoordinatorLayout, "Check Internet Connection..!", Snackbar.LENGTH_LONG);
//            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
//            snackbar.show();
            return false;
        }
        return false;
    }

    private void stopDialog(){

        new Handler().postDelayed(new Runnable() {
            public void run() {
                pDialog.dismiss();
            }
        }, 500);}


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    public void onRefresh() {

        // Fetching data from server
        prepareProcessingOrder();
        processTablist.clear();
        mAdapter.notifyDataSetChanged();
    }

    }


