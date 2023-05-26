package com.example.employeetracking.model;

public class View_Employee_Detail {

    public String name,designation,id;

    public View_Employee_Detail() {
    }

    public View_Employee_Detail(String name, String designation) {
        this.name = name;
        this.designation = designation;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
