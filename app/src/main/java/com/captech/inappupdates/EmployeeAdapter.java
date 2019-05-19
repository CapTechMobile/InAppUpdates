package com.captech.inappupdates;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.captech.inappupdates.employee.EmployeeFragment;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter {

    private List<Employee> mEmployees;
    private Context mContext;

    public EmployeeAdapter(Context context, List<Employee> employees) {
        mContext = context;
        mEmployees = employees;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView nameTextView;
        public ImageView pictureImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.employee_name);
            pictureImageView = itemView.findViewById(R.id.employee_picture);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            Employee employee = mEmployees.get(position);
            Fragment employeeFragment = new EmployeeFragment();
            Bundle data = new Bundle();
            data.putString(Employee.EMPLOYEE_NAME, employee.getName());
            data.putInt(Employee.EMPLOYEE_PICTURE_ID, employee.getPictureId());
            employeeFragment.setArguments(data);

            FragmentTransaction ft = ((AppCompatActivity)mContext).getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, employeeFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
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
        ImageView imageView = employeeViewHolder.pictureImageView;
        imageView.setImageResource(employee.getPictureId());
    }

    @Override
    public int getItemCount() {
        return mEmployees.size();
    }
}
