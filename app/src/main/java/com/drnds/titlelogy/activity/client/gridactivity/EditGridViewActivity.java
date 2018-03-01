package com.drnds.titlelogy.activity.client.gridactivity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.adapter.EditGridAdapter;
import com.drnds.titlelogy.util.Logger;

public class EditGridViewActivity extends AppCompatActivity {
    public TabLayout orderqueue;
    public ViewPager viewPager;
    private Toolbar toolbar;
    EditGridAdapter editGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_grid_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar_editgrid);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Order");
        Logger.getInstance().Log("gridadapter");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
         editGridAdapter = new  EditGridAdapter(getSupportFragmentManager());
        orderqueue= (TabLayout) findViewById(R.id.tabs_editgrid);
        viewPager = (ViewPager) findViewById(R.id.viewpager_editgrid);

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
