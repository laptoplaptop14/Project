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

import com.example.employeetracking.model.Employee;
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
                    LoginWithEmployee(email,password);
                }
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    private void LoginWithEmployee(String email, String password)
    {
        Query em=ref.child("Employee Tracking").child("Employee").orderByChild("email").equalTo(email);
        em.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean emailExists = false;
                Employee employeeData = null;
                int i = 0;
                for (DataSnapshot employeeSnapshot : snapshot.getChildren()) {
                    Employee employee = employeeSnapshot.getValue(Employee.class);
                    i++;
                    // Check if the provided password matches the employee's password
                    if (employee.password.equals(password)) {
                        // Email and password combination exists in Firebase Realtime Database
                        emailExists = true;
                        employeeData = employee;
                        break;
                    }
                }


                if (emailExists){
                    AppPref.getInstance(getApplicationContext()).setEmployeeData(employeeData);
                    Toast.makeText(EmployeeLogin.this, "You have successfully login", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(EmployeeLogin.this,EmployeeDashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    if (i > 0){
                        Toast.makeText(EmployeeLogin.this, "Enter Valid Password", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(EmployeeLogin.this, "Account not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}