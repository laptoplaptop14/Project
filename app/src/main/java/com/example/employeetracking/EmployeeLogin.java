package com.example.employeetracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EmployeeLogin extends AppCompatActivity {

    FirebaseDatabase data;
    DatabaseReference ref;
    ImageView image;
    TextView t1;
    EditText e1,e2;
    Button b1;
    String Userdid;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_login);

        data= FirebaseDatabase.getInstance();
        ref=data.getReference();

        image=(ImageView)findViewById(R.id.image);
        t1=(TextView)findViewById(R.id.text1);
        e1=(EditText)findViewById(R.id.edit1);
        e2=(EditText)findViewById(R.id.edit2);
        b1=(Button)findViewById(R.id.btn);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=String.valueOf(e1.getText().toString().trim());
                String password=String.valueOf(e2.getText().toString().trim());

                if(email.isEmpty())
                {
                    Toast.makeText(EmployeeLogin.this, "Enter Email And Password", Toast.LENGTH_SHORT).show();
                }
                else if(password.isEmpty())
                {
                    Toast.makeText(EmployeeLogin.this, "Enter Password", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    Intent intent=new Intent(EmployeeLogin.this,EmployeeDashboard.class);
                    startActivity(intent);
                    finish();
                    //LoginWithEmployee(email,password);
                }
            }
        });

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EmployeeLogin.this,EmployeeForgetPassword.class);
                startActivity(intent);
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    private void LoginWithEmployee(String email, String password)
    {
        Query em=ref.child("Employee Tracking").child("Employee").child("Userid").equalTo(email);
        em.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void CheckPassword(String password)
    {
        Query pa=ref.child("Employee").child("id").child("name").orderByChild("password").equalTo(password);
        pa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Toast.makeText(EmployeeLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    e1.setText("");
                    e2.setText("");

                    Intent intent=new Intent(EmployeeLogin.this,EmployeeDashboard.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(EmployeeLogin.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmployeeLogin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
}