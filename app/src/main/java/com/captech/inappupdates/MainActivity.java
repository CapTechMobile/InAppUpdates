package com.captech.inappupdates;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.captech.inappupdates.settings.MoreFragment;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnSuccessListener<AppUpdateInfo> {

    private TextView mVersionNumber;
    private RecyclerView mEmployeeList;
    private AppUpdateManager appUpdateManager;
    private boolean mNeedsFlexibleUpdate;
    public static final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mVersionNumber = findViewById(R.id.mainVersionNumber);
        mEmployeeList = findViewById(R.id.employeeList);

        setEmployeeAdapter();
        setVersionText();

        mNeedsFlexibleUpdate = false;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        appUpdateManager = AppUpdateManagerFactory.create(context);
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onResume() {
        super.onResume();
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            transitionToSettingsFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setVersionText() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (Exception e) {

        }
        mVersionNumber.setText(packageInfo.versionName);
    }

    private void setEmployeeAdapter() {
        ArrayList<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee("Alisher Pazylbekov", R.drawable.profilepicture1));
        employeeList.add(new Employee("JP Garduno", R.drawable.profilepicture2));

        EmployeeAdapter adapter = new EmployeeAdapter(this, employeeList);

        mEmployeeList.setAdapter(adapter);
        mEmployeeList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onSuccess(AppUpdateInfo appUpdateInfo) {
        if (appUpdateInfo.updateAvailability()
                == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
            // If an in-app update is already running, resume the update.
            startUpdate(appUpdateInfo, AppUpdateType.IMMEDIATE);
        } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
            // If the update is downloaded but not installed,
            // notify the user to complete the update.
            popupSnackbarForCompleteUpdate();
        } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
            if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startUpdate(appUpdateInfo, AppUpdateType.IMMEDIATE);
            } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                mNeedsFlexibleUpdate = true;
                showFlexibleUpdateNotification();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.i(MainActivity.class.getSimpleName(), "Update flow completed! Result code: " + resultCode);
            } else {
                Log.e(MainActivity.class.getSimpleName(), "Update flow failed! Result code: " + resultCode);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void startUpdate(AppUpdateInfo appUpdateInfo, int appUpdateType) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo,
                    appUpdateType,
                    this,
                    REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    /* Displays the snackbar notification and call to action. */
    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.main_activity),
                        "An update has just been downloaded.",
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RESTART", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    private void showFlexibleUpdateNotification() {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.main_activity),
                        "An update is available and accessible in Settings.",
                        Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void transitionToSettingsFragment() {
        Fragment settingsFragment = new MoreFragment();
        Bundle data = new Bundle();
        data.putBoolean(MoreFragment.FLEXIBLE_UPDATE, mNeedsFlexibleUpdate);
        settingsFragment.setArguments(data);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, settingsFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }
}
