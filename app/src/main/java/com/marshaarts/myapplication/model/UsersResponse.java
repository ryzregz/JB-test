package com.marshaarts.myapplication.model;

import com.google.gson.annotations.SerializedName;
import com.marshaarts.myapplication.network.BaseResponse;

import java.util.List;

public class UsersResponse  extends BaseResponse {
    @SerializedName("users")
    List<User> users;

    public UsersResponse() {
    }

    public UsersResponse(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
