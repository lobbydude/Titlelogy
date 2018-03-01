package com.drnds.titlelogy.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class UploadActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText inputDescript;
    Button upload, submit;
    Spinner spinner;
    TextInputLayout descriptionlayout;
    private ArrayList<String> document;
    private ArrayList<String> documentIds;
    private ProgressDialog pDialog;
    SharedPreferences sp;
    String Description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        toolbar = (Toolbar) findViewById(R.id.toolbar_upload);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Upload Document");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        spinner=(Spinner)findViewById(R.id.document_spinner);
        sp = getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);
        pDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        document = new ArrayList<String>();
        documentIds = new ArrayList<>();
        inputDescript=(EditText)findViewById(R.id.input_discription);
        getDocument();
        checkInternetConnection();
        submit=(Button)findViewById(R.id.button_ordersubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Description = inputDescript.getText().toString().trim();

                if (!validateDescription()) {
                    return;
                }

                if (   checkInternetConnection()){
                    return;
                }

                else{
                    getDocument();
                }}
        });

    }
    private void getDocument(){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.DOCUMENT_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonArray = response.getJSONArray("Client_master");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        document.add(details.getString("Document_Type"));
                        documentIds.add(details.getString("Document_Type_Id"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinner.setAdapter(new ArrayAdapter<String>(UploadActivity.this, android.R.layout.simple_spinner_dropdown_item, document));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
    private boolean validateDescription() {
        if (inputDescript.getText().toString().trim().isEmpty()) {
            descriptionlayout.setError(getString(R.string.err_msg_description));
            requestFocus(inputDescript);
            return false;
        } else {
            descriptionlayout.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                =(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

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
            TastyToast.makeText( this,"Check Internet Connection",TastyToast.LENGTH_SHORT, TastyToast.INFO);
            return false;
        }
        return false;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}


