package com.captech.inappupdates.employee;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.captech.inappupdates.Employee;
import com.captech.inappupdates.R;

public class EmployeeFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.employee_main, container, false);
        Bundle arguments = getArguments();
        String employeeName = arguments.getString(Employee.EMPLOYEE_NAME);
        int pictureId = arguments.getInt(Employee.EMPLOYEE_PICTURE_ID);

        TextView employeeNameTextView = view.findViewById(R.id.employee_name);
        ImageView profileImageView = view.findViewById(R.id.profile_picture);

        employeeNameTextView.setText(employeeName);
        profileImageView.setImageResource(pictureId);

        return view;
    }
}
