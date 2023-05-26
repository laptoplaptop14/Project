package com.example.employeetracking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.location.Location;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.Listener;
import com.example.employeetracking.model.Employee;
import com.example.employeetracking.model.EmployeeLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

import java.util.List;

public class EmployeeDashboard extends AppCompatActivity implements Listener, OnMapReadyCallback {

    EasyWayLocation easyWayLocation;
    Button b1, b2;
    SupportMapFragment smf;
    FusedLocationProviderClient cli;
    LatLng latLng;
    GoogleMap map;
    FirebaseDatabase data;
    DatabaseReference ref;
    boolean isFirstTime = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);
        getSupportActionBar().hide();
        data=FirebaseDatabase.getInstance();
        ref=data.getReference().child("Employee Tracking");
        b1 = (Button) findViewById(R.id.checkin);
        b2 = (Button) findViewById(R.id.checkout);
        smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        Dexter.withContext(getApplicationContext())
                        .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)
                                .withListener(new MultiplePermissionsListener() {
                                    @Override
                                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                        getCurrentLocation();
                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                                    }
                                }).check();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b1.setVisibility(View.INVISIBLE);
                b2.setVisibility(View.VISIBLE);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b2.setVisibility(View.INVISIBLE);
                b1.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getCurrentLocation(){
        LocationRequest locationRequest =  LocationRequest.create()
                .setInterval(100)
                .setFastestInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(100);

        easyWayLocation = new EasyWayLocation(getApplicationContext(),locationRequest,false,false,this);
        smf.getMapAsync(this);
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
                map.addMarker(new MarkerOptions().position(latLng).title("My Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_pedal_bike_24)));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f);
                map.animateCamera(cameraUpdate);
            }
        }
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
                map.addMarker(new MarkerOptions().position(latLng).title("My Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_pedal_bike_24)));
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
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}