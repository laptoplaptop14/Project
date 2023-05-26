package com.example.employeetracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.window.SplashScreen;

public class Splash_Screen extends AppCompatActivity {

    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splass_screen);

        getSupportActionBar().hide();

        image=(ImageView)findViewById(R.id.image);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (AppPref.getInstance(getApplicationContext()).getAdminData() != null){
                    Intent intent=new Intent(Splash_Screen.this,AdminDashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else if (AppPref.getInstance(getApplicationContext()).getEmployeeData() != null){
                    Intent intent=new Intent(Splash_Screen.this,EmployeeDashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent=new Intent(Splash_Screen.this,GetStart.class);
                    startActivity(intent);
                    finish();
                }

            }
        },4000);
    }
}