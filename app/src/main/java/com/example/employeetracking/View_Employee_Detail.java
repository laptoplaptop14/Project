package com.example.employeetracking;

public class View_Employee_Detail {

    String name,designation;

    public View_Employee_Detail() {
    }

    public View_Employee_Detail(String name, String designation) {
        this.name = name;
        this.designation = designation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

}
