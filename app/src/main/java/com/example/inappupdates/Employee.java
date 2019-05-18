package com.example.inappupdates;

import java.util.ArrayList;

public class Employee {

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
