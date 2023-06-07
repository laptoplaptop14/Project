package com.example.employeetracking;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.GetLocationDetail;
import com.example.easywaylocation.Listener;
import com.example.easywaylocation.LocationData;
import com.example.employeetracking.model.EmpCheckIn;
import com.example.employeetracking.model.Employee;
import com.example.employeetracking.model.EmployeeLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EmployeeDashboard extends AppCompatActivity implements Listener, OnMapReadyCallback, LocationData.AddressCallBack {

    EasyWayLocation easyWayLocation;
    Button b1,btnLogout;

    GetLocationDetail getLocationDetail;
    LatLng latLng;
    GoogleMap map;
    FirebaseDatabase data;
    DatabaseReference ref;
    boolean isFirstTime = true;

    LocationData locationData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);
        getSupportActionBar().hide();
        data=FirebaseDatabase.getInstance();
        ref=data.getReference().child("Employee Tracking");
        b1 = (Button) findViewById(R.id.checkin);
        btnLogout = (Button) findViewById(R.id.btnLogout);


        PermissionX.init(this)
                .permissions(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION)
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                        getCurrentLocation();
                    }
                });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationData != null){
                    addCheckInData();
                }else{
                    Toast.makeText(EmployeeDashboard.this, "Your Location not working...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppPref.getInstance(getApplicationContext()).clearData();
                Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(EmployeeDashboard.this,SelectActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addCheckInData() {
        Employee emp = AppPref.getInstance(getApplicationContext()).getEmployeeData();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        EmpCheckIn empCheckIn = new EmpCheckIn(emp.name,emp.id,emp.Userid,locationData.getFull_address(),currentDate);
        ref.child("empCheckIn").child(emp.id).setValue(empCheckIn)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EmployeeDashboard.this, "You have successfully check in...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void getCurrentLocation(){

        getLocationDetail = new GetLocationDetail(this,this);
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.mapFragment,mapFragment).commit();
        mapFragment.getMapAsync(this);

        LocationRequest locationRequest =  LocationRequest.create()
                .setInterval(100)
                .setFastestInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(100);

        easyWayLocation = new EasyWayLocation(getApplicationContext(),locationRequest,false,false,this);
      }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void locationOn() {
        }

    @Override
    public void currentLocation(Location location) {
        latLng = new LatLng(location.getLatitude(),location.getLongitude());

        if (map != null)
        {
            if (isFirstTime)
            {
                isFirstTime = false;
                map.addMarker(new MarkerOptions().position(latLng).title("My Location").icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.tracklocation)));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f);
                map.animateCamera(cameraUpdate);
            }
        }
        getLocationDetail.getAddress(location.getLatitude(),location.getLongitude(),"AIzaSyBP3KhqaIgn51j-qkiRXIzeHP8SXlNOAs4");
        sendLocation(location);
    }

    private void sendLocation(Location location) {
        Employee emp = AppPref.getInstance(getApplicationContext()).getEmployeeData();
        EmployeeLocation employee = new EmployeeLocation(emp.id,emp.Userid,emp.name, location.getLatitude(), location.getLongitude());

        ref.child("empLocation").child(emp.Userid).child(emp.id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    EmployeeLocation existingEmployee = dataSnapshot.getValue(EmployeeLocation.class);
                    existingEmployee.latitude = location.getLatitude();
                    existingEmployee.longitude = location.getLongitude();
                    ref.child("empLocation").child(existingEmployee.adminId).child(existingEmployee.employeeId).setValue(existingEmployee)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Data successfully updated
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Failed to update data
                                }
                            });
                }else{
                    ref.child("empLocation").child(emp.Userid).child(emp.id).setValue(employee)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void locationCancelled() {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        if (latLng != null)
        {
            if (isFirstTime) {
                isFirstTime = false;
                map.addMarker(new MarkerOptions().position(latLng).title("My Location").icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.tracklocation)));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10f);
                map.animateCamera(cameraUpdate);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==EasyWayLocation.LOCATION_SETTING_REQUEST_CODE ){
            easyWayLocation.onActivityResult(resultCode);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        easyWayLocation.startLocation();
    }



    @Override
    protected void onPause() {
        super.onPause();
        easyWayLocation.endUpdates();
    }

    @Override
    public void locationData(LocationData locationData) {
        this.locationData = locationData;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}