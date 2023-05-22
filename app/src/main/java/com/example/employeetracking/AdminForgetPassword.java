package com.example.employeetracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminForgetPassword extends AppCompatActivity {

    FirebaseDatabase data;
    DatabaseReference ref;
    ImageView image;
    EditText e1,e2,e3;
    Button b1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_forget_password);

        data=FirebaseDatabase.getInstance();
        ref=data.getReference();

        image=(ImageView)findViewById(R.id.image);
        e1=(EditText)findViewById(R.id.edit1);
        e2=(EditText)findViewById(R.id.edit2);
        e3=(EditText)findViewById(R.id.edit3);
        b1=(Button)findViewById(R.id.btn);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String OldPassword=String.valueOf(e1.getText().toString().trim());
                String NewPassword=String.valueOf(e2.getText().toString().trim());
                String ConPassword=String.valueOf(e3.getText().toString().trim());

                if(OldPassword.isEmpty())
                {
                    Toast.makeText(AdminForgetPassword.this, "Fill the Detail", Toast.LENGTH_SHORT).show();
                }
                else if(!(NewPassword.equals(ConPassword)))
                {
                    Toast.makeText(AdminForgetPassword.this, "Check New And Confirm Password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ChangePassword(NewPassword);
                }
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    private void ChangePassword(String newPassword)
    {
        ref.child("Employee Tracking").child("Admin").child("id")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        e1.setText(snapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    ///////////////////////////////////////////////////////////////////////////////////////////
}