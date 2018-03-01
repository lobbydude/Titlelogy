package com.drnds.titlelogy.fragments.client;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.adapter.OrderQueueAdapter;

/**
 * Created by Ajithkumar on 5/17/2017.
 */

public class AddOrderFragment extends Fragment {
    public TabLayout orderqueue;
    public ViewPager viewPager;
    OrderQueueAdapter orderQueueAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_addorder, container, false);
        OrderQueueAdapter orderQueueAdapter = new  OrderQueueAdapter( getChildFragmentManager());
        orderqueue= (TabLayout)view.findViewById(R.id.tabs_addorder);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_addorder);

        viewPager.setAdapter(orderQueueAdapter);

        orderqueue.post(new Runnable() {
            @Override
            public void run() {
                orderqueue.setupWithViewPager(viewPager);
            }
        });


        return view;
    }
}