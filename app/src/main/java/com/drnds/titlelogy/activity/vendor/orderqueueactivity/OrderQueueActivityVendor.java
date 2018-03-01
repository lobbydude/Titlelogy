package com.drnds.titlelogy.activity.vendor.orderqueueactivity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.adapter.OrderQueueAdapter;
import com.drnds.titlelogy.adapter.vendor.VendorOrderQueueAdapter;

/**
 * Created by abishek on 9/15/2017.
 */

public class OrderQueueActivityVendor extends AppCompatActivity {
    public TabLayout orderqueue;
    public ViewPager viewPager;
    VendorOrderQueueAdapter orderQueueAdapter;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_order_queue);
        OrderQueueAdapter orderQueueAdapter = new  OrderQueueAdapter(getSupportFragmentManager());
        toolbar = (Toolbar) findViewById(R.id.vendor_toolbar_createorder);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add New Order");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        orderqueue= (TabLayout) findViewById(R.id.vendor_tabs_order);
        viewPager = (ViewPager) findViewById(R.id.vendor_viewpager_order);

        viewPager.setAdapter(orderQueueAdapter);

        orderqueue.post(new Runnable() {
            @Override
            public void run() {
                orderqueue.setupWithViewPager(viewPager);
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}

