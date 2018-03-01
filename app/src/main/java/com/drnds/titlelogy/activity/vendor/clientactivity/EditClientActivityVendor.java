package com.drnds.titlelogy.activity.vendor.clientactivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.subclientactivity.EditSubclientActivity;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.CustomRequest;
import com.drnds.titlelogy.util.Logger;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * Created by abishek on 9/7/2017.
 */

public class EditClientActivityVendor extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText  clientcode;
    private TextInputLayout inputclientcode;
    private ProgressDialog pDialog;
    private Button submit;
    SharedPreferences pref;
    private String clientcodeu;
    private static String TAG = EditClientActivityVendor.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_edit_client);
        pref = getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);
        pref = getApplicationContext().getSharedPreferences(
                "Client", 0);
        toolbar = (Toolbar) findViewById(R.id.toolbar_editsubclient);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Client");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        pDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        clientcode = (EditText) findViewById(R.id.input_clientcode);
        inputclientcode = (TextInputLayout) findViewById(R.id.input_layout_clientcode);
        submit = (Button) findViewById(R.id.button_updatesubclientvendor);

        //        getting vallus
        Intent intent = getIntent();


        clientcodeu = intent.getStringExtra("Client_Code");
//       set values
        clientcode.setText(clientcodeu);
        checkInternetConnection();




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                {
                    submitClient();
                }}
        });
    }

    public String getUserId() {
        Intent intent = getIntent();
        return intent.getStringExtra("Client_Id");
    }

    public void submitClient() {
        Logger.getInstance().Log("in update vendor id");
        showDialog();
        final String Client_Code = clientcode.getText().toString().trim();

        pDialog.setMessage("Updating ...");

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.EDITVENDORCLIENT_URL + getUserId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    boolean  error = response.getBoolean("error");
                    if (!error){
                        Toasty.success(EditClientActivityVendor.this, "Subclient Updated Successfully", Toast.LENGTH_SHORT, true).show();
                        hideDialog();
                    }else{
                        Toasty.error(EditClientActivityVendor.this, "Update Not Successful", Toast.LENGTH_SHORT, true).show();
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

                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Client_Id", getUserId());
                params.put("Client_Code", Client_Code);





                return params;
            }


        };
        customRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(customRequest);
    }


    public String getVendorId() {

        return pref.getString("Vendor_Id", "");

    }



    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

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
            TastyToast.makeText( EditClientActivityVendor.this,"Check Internet Connection",TastyToast.LENGTH_SHORT,TastyToast.INFO);
            return false;
        }
        return false;
    }
}



