package com.example.employeetracking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EmployeeForgetPassword extends AppCompatActivity {

    ImageView image;
    EditText e1;
    Button b1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_forget_password);

        image=(ImageView)findViewById(R.id.image);
        e1=(EditText)findViewById(R.id.edit1);
        b1=(Button)findViewById(R.id.btn);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=String.valueOf(e1.getText().toString().trim());

                if(email.isEmpty())
                {
                    Toast.makeText(EmployeeForgetPassword.this, "Enter Email", Toast.LENGTH_SHORT).show();
                }
                else
                {

                }
            }
        });
    }
}