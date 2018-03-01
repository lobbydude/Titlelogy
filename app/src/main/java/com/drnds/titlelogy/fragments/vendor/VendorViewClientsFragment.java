package com.drnds.titlelogy.fragments.vendor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drnds.titlelogy.R;

/**
 * Created by Abishek.S on 7/13/2017.
 */

public class VendorViewClientsFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.vendor_view_clients_row, null);
        return view;
    }

}

