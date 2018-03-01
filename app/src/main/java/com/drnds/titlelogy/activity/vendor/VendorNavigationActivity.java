package com.drnds.titlelogy.activity.vendor;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.fragments.client.scoreboardfragment.ScoreBoardFragment;
import com.drnds.titlelogy.fragments.vendor.VendorViewClientsFragment;
import com.drnds.titlelogy.fragments.vendor.DashboardFragmentVendor;
import com.drnds.titlelogy.fragments.vendor.ViewVendorFragment;
import com.drnds.titlelogy.fragments.vendor.createclientfragvendor.CreateClientFragmentVendor;
import com.drnds.titlelogy.fragments.vendor.createsubclientfragvendor.CreateSubclientFragmentVendor;
import com.drnds.titlelogy.fragments.vendor.orderqueuefragment.VendorOrderQueueFragment;
import com.drnds.titlelogy.fragments.vendor.scoreboardfragmentvendor.ScoreBoardFragmentVendor;


public class VendorNavigationActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;

    String fragmentCalled = "";

    private Toolbar toolbar;


    // urls to load navigation header background image
    // and profile image

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_SCOREBOARD = "scoreboard";
    private static final String TAG_ORDERQUEUE = "orderqueue";
    private static final String TAG_DASHBOARD = "dashboard";
    private static final String TAG_SUBCLIENT = "subclient";
    private static final String TAG_USER = "user";
    private static final String TAG_Vendor = "vendor";

    public static String CURRENT_TAG = TAG_SCOREBOARD;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;
    private Boolean exit = false;
    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_navigation);

        toolbar = (Toolbar) findViewById(R.id.toolbar_navigation_vendor);

        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_vendor);
        navigationView = (NavigationView) findViewById(R.id.nav_view_vendor);
        sp = getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.navigationtext_vendor);
        String Email = sp.getString("Email","");
        String Vendor_User_Id = sp.getString("Vendor_User_Id","");
        nav_user.setText(Email);

        // Navigation view header


        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_vendortitles);

        // load nav menu header data



        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_SCOREBOARD;
            loadHomeFragment();
        }
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */


    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button

            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.frame_vendor, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button


        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // process
                ScoreBoardFragmentVendor scoreBoardFragment = new ScoreBoardFragmentVendor();
                return scoreBoardFragment;
            case 1:

                DashboardFragmentVendor dashboardFragment = new DashboardFragmentVendor();
                return dashboardFragment;
            case 2:
                // order fragment
                VendorOrderQueueFragment orderQueueFragment = new VendorOrderQueueFragment();
                return  orderQueueFragment ;

            case 3:
                // settings fragment
                CreateSubclientFragmentVendor createSubclientFragment = new CreateSubclientFragmentVendor();
                return createSubclientFragment;
            case 4:
                // settings fragment
                CreateClientFragmentVendor createClientFragment= new CreateClientFragmentVendor();
                return createClientFragment;
            case 5:
                // settings fragment
                ViewVendorFragment viewVendorFragment = new ViewVendorFragment();
                return viewVendorFragment;



            default:
                return new ScoreBoardFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_scoreboard_vendor:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_SCOREBOARD;
                        break;

                    case R.id.nav_dashboard_vendor:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_DASHBOARD;
                        break;
                    case R.id.nav_orderqueue_vendor:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_ORDERQUEUE;
                        break;
                    case R.id.nav_subclient_vendor:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_SUBCLIENT;
                        break;
                    case R.id.nav_user_vendor:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_USER;
                        break;
                    case R.id.nav_vendor_vendor:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_Vendor;
                        break;

                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_SCOREBOARD;
                loadHomeFragment();
                return;
            }

        }
        AlertDialog dlg = new AlertDialog.Builder(VendorNavigationActivity.this,R.style.MyAlertDialogStyle).create();
        dlg.setCancelable(false);
        dlg.setMessage("Are you sure want to close?");
        dlg.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dlg.setButton(DialogInterface.BUTTON_POSITIVE, "CLOSE APP", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                startActivity(intent);
                finish();


            }
        });

        dlg.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected

        getMenuInflater().inflate(R.menu.logout_vendor, menu);

        return true;
        // when fragment is notifications, load the menu created for notifications


    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Fragment fragment = null;
        String title = "";

        switch (item.getItemId()){

            case R.id.action_logout_vendor:
                logout();
                return true;

            case R.id.nav_myaccount_vendor:
                Intent intent=new Intent(VendorNavigationActivity.this,MyaccountActivityVendor.class);
                startActivity(intent);

                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    // show or hide the fab
    private void logout() {

        Intent intent = new Intent(VendorNavigationActivity.this, VendorLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}




