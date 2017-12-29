package com.azwallpaper;

import android.util.Log;

import java.io.Serializable;

/**

 */
public class GalleryItem implements Serializable  {

    private String id;
    private String secret;
    private String server;
    private String farm;
    private String title;

    public GalleryItem(String id, String secret, String server, String farm, String title) {
        this.id = id;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        Log.d("GalleryItem", "-------------getUrl-------: http://farm" + farm + ".static.flickr.com/" + server + "/" + id + "_" + secret + ".jpg" );

        return "http://farm" + farm + ".static.flickr.com/" + server + "/" + id + "_" + secret + ".jpg" ;
    }

}
