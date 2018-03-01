package com.drnds.titlelogy.adapter.vendor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.model.ProcessTab;
import com.drnds.titlelogy.model.VendorProcessTab;

import java.util.ArrayList;

/**
 * Created by abishek on 9/12/2017.
 */

public class VendorProcessingTabGridAdapter extends BaseAdapter {
    Context c;
    ArrayList<VendorProcessTab> vendorprocessTablist;
    public VendorProcessingTabGridAdapter(Context c, ArrayList<VendorProcessTab> vendorprocessTablist) {
        this.c = c;
        this. vendorprocessTablist =  vendorprocessTablist;
    }
    @Override
    public int getCount() {
        return  vendorprocessTablist.size();
    }
    @Override
    public Object getItem(int i) {
        return  vendorprocessTablist.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            view= LayoutInflater.from(c).inflate(R.layout.vendor_tabprocessing_row,viewGroup,false);
        }
        final VendorProcessTab s= (VendorProcessTab) this.getItem(i);

        TextView orderNo= (TextView) view.findViewById(R.id.vendororderno_prtab);
        TextView title= (TextView) view.findViewById(R.id.vendortitle_prtab);
        //BIND
        orderNo.setText(s.getOrderno());
        title.setText(s.getTitle());

        return view;
    }
}
