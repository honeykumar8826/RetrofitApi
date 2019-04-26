package com.retrofitApi.modal;

public class UpdateRecord {
    private String name;
    private String salary;
    private String age;
    private String recId;

    public UpdateRecord(String name, String salary, String age, String recId) {
        this.name = name;
        this.salary = salary;
        this.age = age;
        this.recId = recId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }
}
