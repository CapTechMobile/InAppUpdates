package com.captech.inappupdates;

import java.util.ArrayList;

public class Employee {

    public static String EMPLOYEE_NAME = "employee_name";
    public static String EMPLOYEE_PICTURE_ID = "employee_picture_id";


    private String mName;
    private int mPictureId;

    public Employee(String name) {
        mName = name;
    }

    public Employee(String name, int resId) {
        mName = name;
        mPictureId = resId;
    }

    public String getName() {
        return mName;
    }

    public int getPictureId() {
        return mPictureId;
    }

    public static ArrayList<Employee> createEmployeeList(int numEmployees) {
        ArrayList<Employee> employees = new ArrayList<>();

        for (int i = 0; i < numEmployees; i++) {
            employees.add(new Employee("Person " + (i + 1)));
        }

        return employees;
    }
}
