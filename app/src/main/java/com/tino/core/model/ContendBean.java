package com.tino.core.model;

import java.util.ArrayList;

public class ContendBean {
    private String name;
    private ArrayList<String> phoneList;
    private Integer status;

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ArrayList<String> getPhoneList() {
        return this.phoneList;
    }

    public void setPhoneList(ArrayList<String> phoneList) {
        this.phoneList = phoneList;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ContendBean() {
        this.status = Integer.valueOf(0);
    }

    public ContendBean(String name, ArrayList<String> phoneList) {
        this.name = name;
        this.phoneList = phoneList;
    }

    public ContendBean(String name) {
        this.name = name;
    }
}
