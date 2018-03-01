package com.drnds.titlelogy.activity.vendor.orderqueueactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.adapter.EditOrderQueueAdapter;
import com.drnds.titlelogy.adapter.vendor.VendorEditOrderQueueAdapter;
import com.drnds.titlelogy.util.Logger;

/**
 * Created by abishek on 9/15/2017.
 */

public class EditOrderActivityVendor extends AppCompatActivity {
    public TabLayout orderqueue;
    public ViewPager viewPager;
    VendorEditOrderQueueAdapter vendoreditOrderQueueAdapter;
    String Order_Id,Sub_Client_Name,Order_Priority,state,county, producttype,Order_Assign_Type,Order_Task,status,barrowername;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_edit_order);

        toolbar = (Toolbar) findViewById(R.id.vendor_toolbar_editorder);
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

        vendoreditOrderQueueAdapter = new  VendorEditOrderQueueAdapter(getSupportFragmentManager(),
                Order_Id,Sub_Client_Name,Order_Priority,state,county, producttype,Order_Task,Order_Assign_Type,status);

        orderqueue= (TabLayout) findViewById(R.id.vendor_tabs_editorder);
        viewPager = (ViewPager) findViewById(R.id.vendor_viewpager_editorder);

        viewPager.setAdapter(vendoreditOrderQueueAdapter);

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




