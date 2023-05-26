package com.example.employeetracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminViewProfile extends AppCompatActivity {

    String Userdid;
    FirebaseDatabase data;
    DatabaseReference ref;
    ImageView img;
    TextView name, email, mono;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_profile);

        data = FirebaseDatabase.getInstance();
        ref = data.getReference().child("Employee Tracking");

        img = (ImageView) findViewById(R.id.image);
        name = (TextView) findViewById(R.id.text1);
        email = (TextView) findViewById(R.id.text2);
        mono = (TextView) findViewById(R.id.text3);


        Intent intent=getIntent();
        Userdid=intent.getStringExtra("Userid");

        ref.child("Admin").child(Userdid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        name.setText(String.valueOf(snapshot.child("name").getValue().toString()));
                        email.setText(String.valueOf(snapshot.child("email").getValue().toString()));
                        mono.setText(String.valueOf(snapshot.child("mobileno").getValue().toString()));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}