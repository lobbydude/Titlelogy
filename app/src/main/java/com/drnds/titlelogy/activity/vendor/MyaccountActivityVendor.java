package com.drnds.titlelogy.activity.vendor;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.adapter.MyAccountAdapter;
import com.drnds.titlelogy.adapter.vendor.MyAccountAdapterVendor;

public class MyaccountActivityVendor extends AppCompatActivity {
    public static TabLayout myaccount;
    public static ViewPager viewPager;
    FragmentPagerAdapter adapterViewPager;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_myaccount);
        toolbar = (Toolbar) findViewById(R.id.toolbar_myaccount_vendor);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Account");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        myaccount = (TabLayout) findViewById(R.id.tabs_myaccount_vendor);
        viewPager = (ViewPager) findViewById(R.id.viewpager_myaccount_vendor);

        adapterViewPager = new MyAccountAdapterVendor(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        myaccount.post(new Runnable() {
            @Override
            public void run() {
                myaccount.setupWithViewPager(viewPager);
            }
        });
    }
}
