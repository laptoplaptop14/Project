package com.example.employeetracking;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeetracking.model.View_Employee_Detail;

import java.util.ArrayList;

public class MyAdpater extends RecyclerView.Adapter<MyAdpater.MyViewHolder>
{

    Context context;
    public ArrayList<View_Employee_Detail> view_employee_detailArrayList;
    onclick onclick;

    public MyAdpater(Context context, ArrayList<View_Employee_Detail> view_employee_detailArrayList,onclick onclick) {
        this.context = context;
        this.view_employee_detailArrayList = view_employee_detailArrayList;
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public MyAdpater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.single_row_design,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdpater.MyViewHolder holder, int position) {

        View_Employee_Detail view_employee_detail=view_employee_detailArrayList.get(position);

        holder.name.setText(view_employee_detail.name);
        holder.designation.setText(view_employee_detail.designation);
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick.onDelete(view_employee_detail.getId());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,EmpCheckInActivity.class);
                i.putExtra("empId",view_employee_detail.id);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return view_employee_detailArrayList.size();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,designation;
        ImageView imgDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.viewname);
            designation=itemView.findViewById(R.id.viewdesignation);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }

    public interface onclick{
        public void onDelete(String id);
    }


}
