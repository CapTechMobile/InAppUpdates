package com.captech.inappupdates;

import java.util.ArrayList;

/*
 * Copyright 2018 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
