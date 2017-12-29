package com.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by prashant.patel on 11/1/2017.
 */

public class JsonImageModel  extends RealmObject {


    @SerializedName("title")
    public String title;

    @SerializedName("image_path")
    public String image_path;

    @SerializedName("preview")
    public String preview;

    @SerializedName("thumbnail_url")
    public String thumbnail_url;

}
