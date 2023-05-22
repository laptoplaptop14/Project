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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AddEmployee extends AppCompatActivity {
    String Userdid;
    FirebaseDatabase data;
    DatabaseReference ref;
    ImageView image;
    EditText e1, e2, e3, e4, e5, e6, e7, e8;
    Button b1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        data=FirebaseDatabase.getInstance();
        ref=data.getReference().child("Employee Tracking");

        image=(ImageView)findViewById(R.id.image);
        e1=(EditText)findViewById(R.id.edit1);
        e2=(EditText)findViewById(R.id.edit2);
        e3=(EditText)findViewById(R.id.edit3);
        e4=(EditText)findViewById(R.id.edit4);
        e5=(EditText)findViewById(R.id.edit5);
        e6=(EditText)findViewById(R.id.edit6);
        e7=(EditText)findViewById(R.id.edit7);
        e8=(EditText)findViewById(R.id.edit8);
        b1=(Button)findViewById(R.id.btn);

        Bundle bundle=getIntent().getExtras();
        Userdid=bundle.getString("Userid").toString();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = String.valueOf(System.currentTimeMillis());
                String name = String.valueOf(e1.getText().toString().trim());
                String email = String.valueOf(e2.getText().toString().trim());
                String password = String.valueOf(e3.getText().toString().trim());
                String id_proof_no = String.valueOf(e4.getText().toString().trim());
                String mobil_eno = String.valueOf(e5.getText().toString().trim());
                String address = String.valueOf(e6.getText().toString().trim());
                String designation = String.valueOf(e7.getText().toString().trim());
                String salary = String.valueOf(e8.getText().toString().trim());

                if(name.isEmpty() || email.isEmpty() || password.isEmpty() || id_proof_no.isEmpty() || mobil_eno.isEmpty() || address.isEmpty() || designation.isEmpty() || salary.isEmpty())
                {
                    Toast.makeText(AddEmployee.this, "Fill the Detail", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AddEmployeeData(id,name,email,password,id_proof_no,mobil_eno,address,designation,salary);
                }

            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void AddEmployeeData(String id, String name, String email, String password, String id_proof_no, String mobil_eno, String address, String designation, String salary)
    {

        Map db=new HashMap();

        db.put("Userid",Userdid); //Admin UserId
        db.put("id",id);
        db.put("name",name);
        db.put("email",email);
        db.put("password",password);
        db.put("idproofno",id_proof_no);
        db.put("mobilemo",mobil_eno);
        db.put("address",address);
        db.put("designation",designation);
        db.put("salary",salary);

        ref.child("Employee").child(Userdid).child(name).setValue(db);

        Toast.makeText(this, "Add Employee Successful", Toast.LENGTH_SHORT).show();

        e1.setText("");
        e2.setText("");
        e3.setText("");
        e4.setText("");
        e5.setText("");
        e6.setText("");
        e7.setText("");
        e8.setText("");

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}