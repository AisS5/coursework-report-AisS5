package com.example.comp2001_aistenphillips;

public class Employee {         //Create a class that outlines what an Employee is
                                //All necessary fields for an Employee object


    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private String department;
    private Float salary;
    private String joiningdate;
    private int leaves;

                                            //Have set and get methods. Helps when we want to GET and POST to the API
    public int getId() {
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment(){
        return department;
    }
    public void setDepartment(String department){
        this.department = department;
    }
    public double getSalary() {

        return salary;
    }
    public void setSalary(Float salary) {

        this.salary = salary;
    }

    public String getJoiningDate() {
        return joiningdate;
    }
    public void setJoiningDate(String joiningdate) {
        this.joiningdate = joiningdate;
    }

    public int getLeaves(){
        return leaves;
    }
    public void setLeaves(int leaves){
        this.leaves = leaves;
    }

}
