package com.example.employeetracking;

import android.content.Context;
import android.content.SharedPreferences;


import com.example.employeetracking.model.Admin;
import com.example.employeetracking.model.Employee;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class AppPref {

    private static AppPref sInstance;
    private static SharedPreferences sPref;
    private static SharedPreferences.Editor sEditor;

    private static String employeeKey = "employee";

    private static String adminKey = "admin";
    private AppPref(Context context) {
        sPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        sEditor = sPref.edit();
    }

    public static AppPref getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AppPref(context);
        }
        return sInstance;
    }

    public void setEmployeeData(Employee employee){
        String s =new Gson().toJson(employee);
        sEditor.putString(employeeKey,s).commit();
    }

    public void setAdminData(Admin admin){
        String s =new Gson().toJson(admin);
        sEditor.putString(adminKey,s).commit();
    }

    public Employee getEmployeeData(){
        String s = sPref.getString(employeeKey,"");
        if (s.isEmpty()){
            return null;
        }else{
            return new Gson().fromJson(sPref.getString(employeeKey,""),Employee.class);
        }
    }

    public Admin getAdminData() {
        String s = sPref.getString(adminKey,"");
        if (s.isEmpty()){
            return null;
        }else{
            return new Gson().fromJson(sPref.getString(adminKey,""),Admin.class);
        }
    }
    public void clearData() {
        sEditor.clear().apply();
    }
}