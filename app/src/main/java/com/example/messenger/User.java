package com.example.messenger;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String name;
    private String lastname;
    private boolean online;
    private String city;
    private String age;
    private String birthday;

    public User(String id, String name, String lastname, boolean online) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.online = online;
    }

    public User(){

    }

    public String getCity() {
        return city;
    }

    public String getAge() {
        return age;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public boolean isOnline() {
        return online;
    }

}
