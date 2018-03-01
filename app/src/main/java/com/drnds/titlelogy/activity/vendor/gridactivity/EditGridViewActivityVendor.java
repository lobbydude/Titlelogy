package com.drnds.titlelogy.activity.vendor.gridactivity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.adapter.EditGridAdapter;
import com.drnds.titlelogy.adapter.vendor.VendorEditGridAdapter;
import com.drnds.titlelogy.util.Logger;

/**
 * Created by abishek on 9/14/2017.
 */

public class EditGridViewActivityVendor extends AppCompatActivity {
    public TabLayout orderqueue;
    public ViewPager viewPager;
    private Toolbar toolbar;
    VendorEditGridAdapter editGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_edit_grid_view);
        toolbar = (Toolbar) findViewById(R.id.vendor_toolbar_editgrid);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Order");
        Logger.getInstance().Log("gridadapter");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        editGridAdapter = new  VendorEditGridAdapter(getSupportFragmentManager());
        orderqueue= (TabLayout) findViewById(R.id.vendor_tabs_editgrid);
        viewPager = (ViewPager) findViewById(R.id.vendor_viewpager_editgrid);

        viewPager.setAdapter(editGridAdapter);

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

