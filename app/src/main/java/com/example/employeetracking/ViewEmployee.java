package com.example.employeetracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.employeetracking.model.View_Employee_Detail;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewEmployee extends AppCompatActivity implements MyAdpater.onclick {

    String Uid;
    FirebaseDatabase data;
    DatabaseReference ref;
    RecyclerView recyclerView;
    ArrayList<View_Employee_Detail> view_employee_detailArrayList;
    MyAdpater myAdpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employee);

        data=FirebaseDatabase.getInstance();
        ref=data.getReference().child("Employee Tracking");

        recyclerView = (RecyclerView)findViewById(R.id.rcv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        view_employee_detailArrayList = new ArrayList<View_Employee_Detail>();
        myAdpater = new MyAdpater(ViewEmployee.this,view_employee_detailArrayList,this);
        recyclerView.setAdapter(myAdpater);

        Bundle bundle=getIntent().getExtras();
        Uid=bundle.getString("Userid").toString();

        EventChangeListener();

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void EventChangeListener()
    {
        ref.child("Employee").orderByChild("Userid").equalTo(Uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                view_employee_detailArrayList.clear();
                for(DataSnapshot db : snapshot.getChildren())
                {
                    View_Employee_Detail view_employee_detail=db.getValue(View_Employee_Detail.class);
                    view_employee_detailArrayList.add(view_employee_detail);

                }
                myAdpater.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText( ViewEmployee.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDelete(String id) {


// Query the database to find the employee record
        Query query = ref.child("Employee").orderByChild("id").equalTo(id);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Remove the employee record
                    snapshot.getRef().removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // The employee record was successfully deleted
                                    // Handle the success case
                                    EventChangeListener();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // An error occurred while deleting the employee record
                                    // Handle the failure case
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error appropriately
            }
        });

    }
}