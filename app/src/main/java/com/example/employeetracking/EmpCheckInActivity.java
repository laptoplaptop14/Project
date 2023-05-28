package com.example.employeetracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.employeetracking.model.EmpCheckIn;
import com.example.employeetracking.model.View_Employee_Detail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmpCheckInActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    HistoryAdapter historyAdapter;
    FirebaseDatabase data;
    DatabaseReference ref;
    ArrayList<EmpCheckIn> empCheckIns = new ArrayList<>();

    String empId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_check_in);
        empId = getIntent().getStringExtra("empId");
        recyclerView = findViewById(R.id.rcv);
        historyAdapter = new HistoryAdapter(empCheckIns);
        recyclerView.setAdapter(historyAdapter);
        data= FirebaseDatabase.getInstance();
        ref=data.getReference().child("Employee Tracking");

        getEmpData();
    }

    private void getEmpData() {
        ref.child("empCheckIn").orderByChild("employeeId").equalTo(empId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot db : snapshot.getChildren())
                {
                    EmpCheckIn view_employee_detail=db.getValue(EmpCheckIn.class);
                    empCheckIns.add(view_employee_detail);
                }
                historyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText( EmpCheckInActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{

        ArrayList<EmpCheckIn> empCheckIns;
        public HistoryAdapter(ArrayList<EmpCheckIn> empCheckIns) {
            this.empCheckIns = empCheckIns;
        }

        @NonNull
        @Override
        public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);
            return new HistoryAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
            EmpCheckIn empCheckIn = empCheckIns.get(position);
            holder.tvAddress.setText(empCheckIn.address);
            holder.tvDate.setText(empCheckIn.time);
        }

        @Override
        public int getItemCount() {
            return empCheckIns.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvDate,tvAddress;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvDate = itemView.findViewById(R.id.tvTime);
                tvAddress = itemView.findViewById(R.id.tvAddress);
            }
        }
    }
}