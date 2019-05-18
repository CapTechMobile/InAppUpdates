package com.example.inappupdates;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView mVersionNumber;
    private RecyclerView mEmployeeList;
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
//        ArrayList<Employee> employeeList = Employee.createEmployeeList(2);
        ArrayList<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee("Alisher Pazylbekov", R.drawable.profilepicture1));
        employeeList.add(new Employee("JP Garduno", R.drawable.profilepicture2));

        EmployeeAdapter adapter = new EmployeeAdapter(employeeList);

        mEmployeeList.setAdapter(adapter);
        mEmployeeList.setLayoutManager(new LinearLayoutManager( this));
    }
}
