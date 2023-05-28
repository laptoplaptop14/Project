package com.example.employeetracking.model;

public class EmpCheckIn {
    public String employeeName;
    public String employeeId;
    public String adminId;
    public String address;
    public String time;

    public EmpCheckIn() {
    }

    public EmpCheckIn(String employeeName, String employeeId, String adminId, String address, String time) {
        this.employeeName = employeeName;
        this.employeeId = employeeId;
        this.adminId = adminId;
        this.address = address;
        this.time = time;
    }
}
