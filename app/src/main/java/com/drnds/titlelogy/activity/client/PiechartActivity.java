package com.drnds.titlelogy.activity.client;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.fragments.client.MyValueFormatter;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PiechartActivity extends AppCompatActivity {
    private RelativeLayout mainLayout;
    private PieChart mChart;
    SharedPreferences sp;
    private FrameLayout chartContainer;
    //we are going to display the pie chart for smartphone market shares
    private Toolbar toolbar;
    private String[] xData ={"HOLD", "CLARIFICATION","CANCELLED","TAX_ORDERS", "FINAL_REVIEW_ORDERS","TYPING_QC_ORDERS", "TYPING_ORDERS","SEARCH_QC_ORDERS","SEARCH_ORDES", "NEW_ORDERS"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart);
        toolbar = (Toolbar) findViewById(R.id.toolbar_piechart);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Pie Chart");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        chartContainer =(FrameLayout) findViewById(R.id.chartContainer);
        sp =getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);
        mChart = new PieChart(this);

        chartContainer.addView(mChart);
        //mainLayout.setBackgroundColor(Color.LTGRAY);
        //  mainLayout.setBackgroundColor(Color.parseColor("#55656C"));
        mainLayout.setBackgroundColor(Color.WHITE);


        mChart.setDrawHoleEnabled(true);
        // mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(50);
        mChart.setCenterText("Orders");
        mChart.setCenterTextColor(Color.WHITE);
        mChart.setHoleColor(Color.DKGRAY);
        mChart.setTransparentCircleRadius(10);
        mChart.setBackgroundColor(Color.WHITE);

        // enable rotation of the chart by touch
        mChart.setRotation(0);
        mChart.setRotationEnabled(true);

        //set a chart value selected listener
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                //display message when value selected
                if (e == null)
                    return;

                Toast.makeText(PiechartActivity.this, xData[e.getXIndex()]+" = "+e.getVal()+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        // add Data
        addData();

        //customize legends
//        Legend l = mChart.getLegend();
//        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
//
//        l.setTextColor(Color.BLUE);
//        l.setYEntrySpace(40f);
//        l.setFormSize(10f);
//        l.setForm(Legend.LegendForm.CIRCLE);

    }

    private void addData(){
        StringRequest stringRequest=new StringRequest(Request.Method.GET, Config.DASHBOARD_URL+getClientId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Logger.getInstance().Log("in response");


                try {
                    JSONObject jObj = new JSONObject(response);



                    {


                        JSONArray jsonArray=jObj.getJSONArray("ScoreBoard");

                        int Arraylength=jsonArray.length();


                        Logger.getInstance().Log("RLen"+Arraylength);


                        ArrayList<Entry> yvalues = new ArrayList<Entry>();
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int Value = jsonObject.getInt("Value");
                            String  No_Of_Orders = jsonObject.getString("No_Of_Orders");
                            yvalues.add(new Entry(Value, i));
                            Logger.getInstance().Log("Yvalue"+Value);
                            PieDataSet dataSet = new PieDataSet(yvalues, "orders");


//                            Integer Value = jsonObject.getInt("Value");
//                            String  No_Of_Orders = jsonObject.getString("No_Of_Orders");


//                            ArrayList<String> xVals = new ArrayList<String>();
//
//                            xVals.add(No_Of_Orders);

                            Logger.getInstance().Log("Yvalue"+No_Of_Orders);

                            PieData data = new PieData(xData, dataSet);


//                            ArrayList<Entry> yVals1 = new ArrayList<Entry>();
//
//                            for (int j = 0; j <Value ; j++) {
//                                yVals1.add(new Entry(j,(Value)));
//
//
//                            }
//
//
//
//
//
//                            PieDataSet dataSet = new PieDataSet(yVals1, "orders");
                            dataSet.setSelectionShift(5);
                            dataSet.setDrawValues(true);
                            dataSet.setSliceSpace(0f);
//        dataSet.setSliceSpace(2);

//                            ArrayList<String> xVals = new ArrayList<String>();
//                            for (int k=0; k< xData.length; k++) {
//                                xVals.add(xData[k]);
//                            }
//
//                            PieData data = new PieData(xData, dataSet);

                            data.setValueFormatter(new MyValueFormatter());
                            data.setValueTextSize(11f);
                            data.setValueTextColor(Color.BLACK);
//        data.setValueFormatter(new PercentFormatter());

                            mChart.setData(data);
                            mChart.setDrawSliceText(false);
                            //undo all higlights
                            mChart.highlightValues(null);

                            // update pie chart
                            mChart.invalidate();


//                            ArrayList<Integer> colors = new ArrayList<Integer>();
//
//                            for (int c: ColorTemplate.VORDIPLOM_COLORS)
//                                colors.add(c);
//
//                            for (int c: ColorTemplate.JOYFUL_COLORS)
//                                colors.add(c);
//
//                            for (int c: ColorTemplate.COLORFUL_COLORS)
//                                colors.add(c);
//                            for (int c: ColorTemplate.LIBERTY_COLORS)
//                                colors.add(c);
//                            for (int c: ColorTemplate.PASTEL_COLORS)
//                                colors.add(c);
//
//                            colors.add(ColorTemplate.getHoloBlue());
                            dataSet.setColors(com.drnds.titlelogy.util.ColorTemplate.MATERIAL_COLORS);

                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();

            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);

    }
    public String getClientId() {
//         num3=sp.getString("Client_Id","");
//        Log.e("Client_Id of num3", num3);
        return sp.getString("Client_Id", "");


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
