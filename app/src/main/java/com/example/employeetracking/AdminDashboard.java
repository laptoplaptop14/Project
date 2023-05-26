package com.example.employeetracking;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminDashboard extends AppCompatActivity {
    String Uid;
    String AdminName;
    FirebaseDatabase data;
    DatabaseReference ref;
    ImageView image;
    TextView name,company;
    TextView text,count;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        getWindow().setStatusBarColor(ContextCompat.getColor(AdminDashboard.this,R.color.purple_500));

        data= FirebaseDatabase.getInstance();
        ref=data.getReference().child("Employee Tracking");

        image=(ImageView)findViewById(R.id.imageView);
        name=(TextView)findViewById(R.id.text1);
       // company=(TextView)findViewById(R.id.text2);

        text=(TextView)findViewById(R.id.cardtext);
        count=(TextView)findViewById(R.id.countemp);

        Uid=AppPref.getInstance(getApplicationContext()).getAdminData().id;

        AdminProfileName();
        CountEmployee();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void AdminProfileName()
    {

        ref.child("Admin").child(Uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        name.setText(String.valueOf(snapshot.child("name").getValue().toString()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void CountEmployee()
    {
        ref.child("Employee").orderByChild("Userid").equalTo(Uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String countemp=String.valueOf(snapshot.getChildrenCount());
                count.setText(countemp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminDashboard.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////


    ///MenuBar///
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int imt_id=item.getItemId();
        if (imt_id==R.id.addemp){
            Intent intent=new Intent(AdminDashboard.this,AddEmployee.class);
            intent.putExtra("Userid",Uid);
            startActivity(intent);
        }
        else if (imt_id==R.id.viewEmployee){
            Intent intent=new Intent(AdminDashboard.this,ViewEmployee.class);
            intent.putExtra("Userid",Uid);
            startActivity(intent);
        }
        else if (imt_id==R.id.viewLocation){
            Intent intent=new Intent(AdminDashboard.this,AdminViewLocation.class);
            startActivity(intent);
        }
        else if (imt_id==R.id.viewprofile){
            Intent intent=new Intent(AdminDashboard.this,AdminViewProfile.class);
            intent.putExtra("Userid",Uid);
            startActivity(intent);
        }
        else if (imt_id==R.id.logout){
            AppPref.getInstance(getApplicationContext()).clearData();
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(AdminDashboard.this,AdminLogin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        return true;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}