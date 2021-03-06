package com.example.shoppingdrive.Models;

import com.example.shoppingdrive.Models.Product;

import java.util.ArrayList;

public class UserMerchant {
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String pwd;
    private String address;
    private String role;
    private String companyName;
    private String companyLogo;
    private ArrayList<Product> myArticles;

    public UserMerchant(){
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public ArrayList<Product> getMyArticles() {
        return myArticles;
    }

    public void setMyArticles(ArrayList<Product> articles){
        this.myArticles = articles;
    }
}
