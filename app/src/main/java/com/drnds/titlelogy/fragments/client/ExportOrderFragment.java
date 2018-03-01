package com.drnds.titlelogy.fragments.client;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.createuseractivity.CreateUserActivity;
import com.drnds.titlelogy.adapter.RecyclerOrderQueueAdapter;
import com.drnds.titlelogy.model.OrderQueue;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.sdsmdg.tastytoast.TastyToast;

/**
 * Created by Ajithkumar on 5/17/2017.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


import es.dmoral.toasty.Toasty;

import static android.R.attr.centerX;
import static android.R.attr.content;
import static android.R.attr.data;
import static android.app.Activity.RESULT_OK;
import static android.os.Environment.*;
import static com.android.volley.VolleyLog.TAG;

public class ExportOrderFragment extends Fragment {
    Button write, read;
    Intent intent ;
    static String TAG = "ExelLog";
    private String filename = "SampleFile.txt";
    private static final String FILE_NAME = "/tmp/MyFirstExcel.xlsx";
    SharedPreferences sp;
    JSONObject jsonObject;
    private ProgressDialog pDialog;
    String Data="Hello!!";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_export, null);

        sp = getActivity().getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);
        pDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        write = (Button) view.findViewById(R.id.write);
        read = (Button) view.findViewById(R.id.read);

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 7);

            }
        });


        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternetConnection();
                saveExcelFile(getActivity(), "myExcel.xls");
            }
        });

        prepareProcessingOrder();

        return view;
    }

    public String getClientId() {

        return sp.getString("Client_Id", "");
    }

    private void prepareProcessingOrder() {
        showDialog();
        pDialog.setMessage("Exporting ...");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.ORDERQUEUE_URL + getClientId(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    jsonObject = new JSONObject(response.toString());
                    hideDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialog();
            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private boolean saveExcelFile(Context context, String fileName) {

        // check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            String state = getExternalStorageState();
            if (MEDIA_MOUNTED.equals(state)) {
                return true;
            }
            return false;

        }

        boolean success = false;

        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
//        CellStyle cs = wb.createCellStyle();
//        cs.setFillForegroundColor(HSSFColor.LIME.index);
//        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("myOrder");

        // Generate column headings
        Row row = sheet1.createRow(0);

        c = row.createCell(0);
        c.setCellValue("Client Name");
//        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("Order Id");
//        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("Order_Number");
//        c.setCellStyle(cs);

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("View_Order_Info");
            for (int i = 0; i < jsonArray.length(); i++) {
                boolean error = jsonObject.getBoolean("error");
                if (!error) {
//                    Toasty.success(getActivity(), "   Exporting pleasewait...", Toast.LENGTH_SHORT).show();
                    hideDialog();
//                   showAlert();
//                    TastyToast.makeText( getActivity(),"Exporting pleasewait...",TastyToast.LENGTH_SHORT,TastyToast.DEFAULT);
//                    StyleableToast.makeText(getActivity(), "Exporting pleasewait...", Toast.LENGTH_LONG, R.style.StyledToast).show();

                    SuperActivityToast.create(getActivity(), new Style(), Style.TYPE_PROGRESS_BAR)
                            .setProgressBarColor(Color.YELLOW)
                            .setText("Exporting Please Wait....")
                            .setDuration(Style.DURATION_LONG)
                            .setFrame(Style.FRAME_STANDARD)
                            .setWidth(800)
                            .setColor(Color.rgb(32,93,169))
                            .setHeight(220).show();


//                            .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_BLUE))

//                            .setAnimations(Style.ANIMATIONS_FLY).show();
                } else {
                    Toasty.error(getActivity(), "  Not Exported...", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
                row = sheet1.createRow(i + 1);

                c = row.createCell(0);
                c.setCellValue(jsonArray.getJSONObject(i).getString("Sub_Client_Name"));
//
                c = row.createCell(1);
                c.setCellValue(jsonArray.getJSONObject(i).getString("Order_Id"));

                c = row.createCell(2);
                c.setCellValue(jsonArray.getJSONObject(i).getString("Order_Number"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 500));

        // Create a path where we will place our List of objects on external storage
        File file = new File(context.getFilesDir(), fileName);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
//            Log.w("FileUtils", "Writing file" + file);
        } catch (IOException e) {
//            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }
        return success;
    }

    private static void readExcelFile(Context context, String filename) {

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(TAG, "Storage not available or read only");
            return;
        }

        try {
            // Creating Input Stream
            File file = new File(context.getExternalFilesDir(null), filename);
            FileInputStream myInput = new FileInputStream(file);

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            /** We now need something to iterate through the cells.**/
            Iterator rowIter = mySheet.rowIterator();

            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator cellIter = myRow.cellIterator();
                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    Log.d(TAG, "Cell Value: " + myCell.toString());
                    Toast.makeText(context, "cell Value: " + myCell.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return;
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = getExternalStorageState();
        if (MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = getExternalStorageState();
        if (MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch(requestCode){

            case 7:

                if(resultCode==RESULT_OK){

                    String PathHolder = data.getData().getPath();

                    Toast.makeText(getActivity(), PathHolder , Toast.LENGTH_LONG).show();

                }
                break;

        }
    }



    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showAlert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle);
        builder.setTitle("Export");
        builder.setIcon(R.drawable.export_new);
        builder.setMessage("exporting please wait....");
        builder.setCancelable(false);
        final AlertDialog closedialog= builder.create();
        closedialog.show();

        final Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            public void run() {
                closedialog.dismiss();
                timer2.cancel(); //this will cancel the timer of the system
            }
        }, 36500); // the timer will count 5 seconds....

    }

    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                =(ConnectivityManager)getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED  ) {
            TastyToast.makeText( getActivity(),"Check Internet Connection",TastyToast.LENGTH_SHORT,TastyToast.INFO);
//            Snackbar snackbar = Snackbar.make(snackbarCoordinatorLayout, "Check Internet Connection..!", Snackbar.LENGTH_LONG);
//            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
//            snackbar.show();
            return false;
        }
        return false;
    }
}

