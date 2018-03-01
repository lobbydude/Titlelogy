package com.drnds.titlelogy.activity.client.orderqueueactivity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.adapter.EditOrderQueueAdapter;
import com.drnds.titlelogy.fragments.client.orderqueuefragment.EditOrderInfoFragment;
import com.drnds.titlelogy.util.Logger;

public class EditOrderActivity extends AppCompatActivity {
    public TabLayout orderqueue;
    public ViewPager viewPager;
    EditOrderQueueAdapter editOrderQueueAdapter;
    String Order_Id,Sub_Client_Name,Order_Priority,state,county, producttype,Order_Assign_Type,Order_Task,status,barrowername;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);

        toolbar = (Toolbar) findViewById(R.id.toolbar_editorder);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Order");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //set Fragmentclass Arguments
        Intent intent = getIntent();
        Order_Id = intent.getStringExtra("Order_Id");
        state = intent.getStringExtra("State");
        county = intent.getStringExtra("County");
        producttype= intent.getStringExtra("Product_Type");
        Sub_Client_Name = intent.getStringExtra("Sub_Client_Name");
        Order_Priority = intent.getStringExtra("Order_Priority");
        Order_Task = intent.getStringExtra("Order_Status");
        Order_Assign_Type = intent.getStringExtra("Order_Assign_Type");
        status = intent.getStringExtra("Progress_Status");
        barrowername = intent.getStringExtra("Barrower_Name");

        Logger.getInstance().Log("in update countytype"+Order_Assign_Type);

        editOrderQueueAdapter = new  EditOrderQueueAdapter(getSupportFragmentManager(),
                Order_Id,Sub_Client_Name,Order_Priority,state,county, producttype,Order_Task,Order_Assign_Type,status);

        orderqueue= (TabLayout) findViewById(R.id.tabs_editorder);
        viewPager = (ViewPager) findViewById(R.id.viewpager_editorder);

        viewPager.setAdapter(editOrderQueueAdapter);

        orderqueue.post(new Runnable() {
            @Override
            public void run() {
                orderqueue.setupWithViewPager(viewPager);
            }
        });

//        getSupportFragmentManager().beginTransaction().replace(R.id.frag,obj).commit();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}



