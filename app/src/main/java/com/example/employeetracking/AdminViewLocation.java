package com.example.employeetracking;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.Listener;
import com.example.employeetracking.model.Admin;
import com.example.employeetracking.model.Employee;
import com.example.employeetracking.model.EmployeeLocation;
import com.example.employeetracking.model.View_Employee_Detail;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import java.util.ArrayList;
import java.util.List;

public class AdminViewLocation extends AppCompatActivity implements Listener, OnMapReadyCallback{


    EasyWayLocation easyWayLocation;
    LatLng latLng;
    GoogleMap map;
    FirebaseDatabase data;
    DatabaseReference ref;
    boolean isFirstTime = true;

    ArrayList<EmployeeLocation> employeeLocations = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_location);

        getSupportActionBar().hide();
        data=FirebaseDatabase.getInstance();
        ref=data.getReference().child("Employee Tracking");

        PermissionX.init(this)
                .permissions(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION)
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                     getCurrentLocation();
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
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.mapFragment,mapFragment).commit();
        mapFragment.getMapAsync(this);

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
                map.addMarker(new MarkerOptions().position(latLng).title("My Location").icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.person)));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f);
                map.animateCamera(cameraUpdate);
                setEmployeeData();
            }
        }

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
    public void locationCancelled() {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        if (latLng != null)
        {
            if (isFirstTime) {
                isFirstTime = false;
                map.addMarker(new MarkerOptions().position(latLng).title("My Location").icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.person)));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10f);
                map.animateCamera(cameraUpdate);
                setEmployeeData();
            }
        }
    }

    private void setEmployeeData() {

        Admin admin = AppPref.getInstance(getApplicationContext()).getAdminData();
        ref.child("empLocation").child(admin.id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot db : snapshot.getChildren())
                {
                    EmployeeLocation employeeLocation =db.getValue(EmployeeLocation.class);
                    employeeLocations.add(employeeLocation);
                }

                if (employeeLocations.size() > 0){
                    for (int i = 0;i<employeeLocations.size();i++){
                        LatLng latLng1 = new LatLng(employeeLocations.get(i).latitude,employeeLocations.get(i).longitude);
                        map.addMarker(new MarkerOptions().position(latLng1).title(employeeLocations.get(i).employeeName).icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.baseline_pedal_bike_24)));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
}