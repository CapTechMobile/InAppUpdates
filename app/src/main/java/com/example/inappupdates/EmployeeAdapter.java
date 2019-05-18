package com.example.inappupdates;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter {

    private List<Employee> mEmployees;

    public EmployeeAdapter(List<Employee> employees) {
        mEmployees = employees;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public ImageView pictureImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.employee_name);
            pictureImageView = itemView.findViewById(R.id.employee_picture);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View employeeView = inflater.inflate(R.layout.employee_row, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(employeeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Employee employee = mEmployees.get(i);

        ViewHolder employeeViewHolder = (EmployeeAdapter.ViewHolder)viewHolder;
        // Set item views based on your views and data model
        TextView textView = employeeViewHolder.nameTextView;
        textView.setText(employee.getName());
        ImageView picture = employeeViewHolder.pictureImageView;
        //TODO set picture
    }

    @Override
    public int getItemCount() {
        return mEmployees.size();
    }
}
