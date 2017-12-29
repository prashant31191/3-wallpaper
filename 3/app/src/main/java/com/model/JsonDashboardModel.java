package com.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by prashant.patel on 11/1/2017.
 */

public class JsonDashboardModel extends RealmObject {

    @SerializedName("name")
    public String name;

    @SerializedName("keyword")
    public String keyword;

    @SerializedName("image")
    public String image;

    @SerializedName("id")
    public String id;

}
