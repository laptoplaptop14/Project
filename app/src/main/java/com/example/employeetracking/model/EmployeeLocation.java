package com.example.employeetracking.model;

public class EmployeeLocation {
    public String employeeId;
    public String adminId;

    public String employeeName;
    public double latitude;
    public double longitude;
    public EmployeeLocation(String employeeId,String adminId,String employeeName, double latitude, double longitude) {
        this.employeeId = employeeId;
        this.adminId = adminId;
        this.employeeName = employeeName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
