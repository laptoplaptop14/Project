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

public class AdminLogin extends AppCompatActivity {
    String Uid;
    FirebaseDatabase data;
    DatabaseReference ref;
    ImageView image;
    TextView t1, t2;
    EditText e1, e2;
    Button b1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        data=FirebaseDatabase.getInstance();
        ref=data.getReference().child("Employee Tracking");

        image = (ImageView) findViewById(R.id.image);
        t1 = (TextView) findViewById(R.id.text1);
        t2 = (TextView) findViewById(R.id.text2);
        e1 = (EditText) findViewById(R.id.edit1);
        e2 = (EditText) findViewById(R.id.edit2);
        b1 = (Button) findViewById(R.id.btn);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=String.valueOf(e1.getText().toString().trim());
                String password=String.valueOf(e2.getText().toString().trim());

                if(email.isEmpty())
                {
                    Toast.makeText(AdminLogin.this, "Enter Email", Toast.LENGTH_SHORT).show();
                }
                else if(password.isEmpty())
                {
                    Toast.makeText(AdminLogin.this, "Enter Password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    LoginWithEmailAndPassword(email,password);
                }
            }
        });

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminLogin.this,AdminForgetPassword.class);
                startActivity(intent);
            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminLogin.this,AdminRegister.class);
                startActivity(intent);
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void LoginWithEmailAndPassword(String email, String password)
    {
        Query em=ref.child("Admin").orderByChild("email").equalTo(email);
        em.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    CheckPassword(password);
                }
                else
                {
                    Toast.makeText(AdminLogin.this, "Account Not Created", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminLogin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CheckPassword(String password)
    {
        Query pa=ref.child("Admin").orderByChild("password").equalTo(password);
        pa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        Uid=dataSnapshot.child("id").getValue().toString();
                    }
                    Toast.makeText(AdminLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    e1.setText("");
                    e2.setText("");

                    Intent intent=new Intent(AdminLogin.this,AdminDashboard.class);
                    intent.putExtra("Userid",Uid);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(AdminLogin.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminLogin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
}