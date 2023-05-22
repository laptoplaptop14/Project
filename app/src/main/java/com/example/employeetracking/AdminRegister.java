package com.example.employeetracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Map;

public class AdminRegister extends AppCompatActivity {

    FirebaseDatabase data;
    DatabaseReference ref;
    ImageView image;
    TextView t1;
    EditText e1, e2, e3, e4;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);

        data=FirebaseDatabase.getInstance();
        ref=data.getReference().child("Employee Tracking");

        image = (ImageView) findViewById(R.id.image);
        t1 = (TextView) findViewById(R.id.text1);
        e1 = (EditText) findViewById(R.id.edit1);
        e2 = (EditText) findViewById(R.id.edit2);
        e3 = (EditText) findViewById(R.id.edit3);
        e4 = (EditText) findViewById(R.id.edit4);
        b1 = (Button) findViewById(R.id.btn);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id=String.valueOf(System.currentTimeMillis());
                String name=String.valueOf(e1.getText().toString().trim());
                String email=String.valueOf(e2.getText().toString().trim());
                String password=String.valueOf(e3.getText().toString().trim());
                String mobile_no=String.valueOf(e4.getText().toString().trim());

                if(name.isEmpty() || email.isEmpty() || password.isEmpty() || mobile_no.isEmpty())
                {
                    Toast.makeText(AdminRegister.this, "Fill the Detail", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Registration(id,name,email,password,mobile_no);
                }
            }
        });

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminRegister.this,AdminLogin.class);
                startActivity(intent);
            }
        });
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void Registration(String id, String name, String email, String password, String mobile_no)
    {
        Query em= ref.child("Admin").orderByChild("email").equalTo(email);
        em.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Toast.makeText(AdminRegister.this, "Account Already Created", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    RegistrationSucessful(id,name,email,password,mobile_no);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminRegister.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void RegistrationSucessful(String id, String name, String email, String password, String mobile_no)
    {
        Map db=new HashMap();

        db.put("id",id);
        db.put("name",name);
        db.put("email",email);
        db.put("password",password);
        db.put("mobileno",mobile_no);

        ref.child("Admin").child(id).setValue(db);

        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();

        e1.setText("");
        e2.setText("");
        e3.setText("");
        e4.setText("");

        Intent intent=new Intent(AdminRegister.this,AdminLogin.class);
        startActivity(intent);
        finish();

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
}