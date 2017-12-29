package com.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by prashant.patel on 11/4/2017.
 */

public class GsonResponseWallpaperList extends RealmObject{

    @SerializedName("data")
    public RealmList<JsonImageModel> arrayListJsonImageModel;

    //@PrimaryKey
    @PrimaryKey @Index
    @SerializedName("filename")
    public String filename;

}
