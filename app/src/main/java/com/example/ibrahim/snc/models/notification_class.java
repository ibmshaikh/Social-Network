package com.example.ibrahim.snc.models;

public class notification_class {


    public notification_class(){


    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    private String UserName,Type;

}
