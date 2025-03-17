package com.pojo;

import java.time.LocalDateTime;

public class Employee {
    private Long id;
    private String userName;
    private String password;
    private String name;
    private String phone;
    private int gender; //0 is female, 1 is male
    private LocalDateTime createTime;

    public Employee(Long id, String userName, String password, String name, String phone, int gender, LocalDateTime createTime) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.createTime = createTime;
    }

    public Employee(Long id, String name, String phone, int gender) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
    }

    public Employee() {
    }

    public String getStringGender() {
        return gender == 1 ? "Male" : "Female";
    }

    public static int getStringInt (String gender){
        return gender.equals("Male") ? 1 : 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", gender=" + gender +
                ", createTime=" + createTime +
                '}';
    }
}
