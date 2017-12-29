package com.azwallpaper;


import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.api.UrlManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.utils.TouchImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;


import static android.os.Environment.DIRECTORY_DOWNLOADS;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFragment extends Fragment {
    // tag for logcat
    public static final String TAG = PhotoFragment.class.getSimpleName();

    private ProgressBar mProgressBar;
    private TextView mDescText;
    private ImageView mPhoto;

    private GalleryItem mItem;
    private RequestQueue mRq;
    private DownloadManager mDownloadManager;

    private boolean mLoading = false;
    String strOrigionalImageLink = "https://farm";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo,
                container,
                false);

        mItem = (GalleryItem) getActivity().getIntent().getSerializableExtra("item");

        mDownloadManager = (DownloadManager) getActivity().getSystemService(
                getActivity().DOWNLOAD_SERVICE);
        mRq = Volley.newRequestQueue(getActivity());

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        mDescText = (TextView) view.findViewById(R.id.desc_text);

        mPhoto = (ImageView) view.findViewById(R.id.photo);
        Glide.with(this).load(mItem.getUrl()).thumbnail(0.5f).into(mPhoto);

        // download original single photo
        LinearLayout downloadView = (LinearLayout) view.findViewById(R.id.download);

        viewFullscreen = (LinearLayout) view.findViewById(R.id.viewFullscreen);

        rlDetail = (RelativeLayout) view.findViewById(R.id.rlDetail);
        rlImage = (RelativeLayout) view.findViewById(R.id.rlImage);

        tvSetWallpaper = (TextView) view.findViewById(R.id.tvSetWallpaper);
        ivFullScreen = (TouchImageView) view.findViewById(R.id.ivFullScreen);
        ivClose = (ImageView) view.findViewById(R.id.ivClose);

        viewFullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlDetail.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                rlImage.setVisibility(View.VISIBLE);

                viewFullScreenImage();
            }
        });

        tvSetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap !=null)
                {
                    setWallpaper();
                }
                else
                {
                    Toast.makeText(getActivity(), "Please wait Image is Loading...!", Toast.LENGTH_LONG).show();
                }
            }
        });


        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                rlDetail.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                rlImage.setVisibility(View.GONE);
            }
        });


        downloadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //downloadPhoto();
                downloadPhotoOrigional();
                Toast.makeText(getActivity(), "Start downloading Origional data", Toast.LENGTH_LONG).show();
            }
        });

        downloadView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(strOrigionalImageLink !=null && strOrigionalImageLink.contains(".")) {
                    downloadPhoto2();
                    Toast.makeText(getActivity(), "Start downloading", Toast.LENGTH_LONG).show();


                }
                return false;
            }
        });

        // open url link for Flickr official app
        LinearLayout openView = (LinearLayout) view.findViewById(R.id.open);
        openView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApp();
            }
        });

        // load original photo
        startLoading();
        return view;
    }

    // function for downloading original photo when download button is pressed
    private void downloadPhoto() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mItem.getUrl()));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("SharkFeed Download");
        request.setDescription(mItem.getUrl());
        mDownloadManager.enqueue(request);
    }

    private void downloadPhoto2() {
        try {
            String folderName = "azwallpaper";

            String url = mItem.getUrl();
            URL urlObj = new URL(url);
            String urlPath = urlObj.getPath();
            String fileName = urlPath.substring(urlPath.lastIndexOf('/') + 1);
// fileName is now "somefilename.xy"

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mItem.getUrl()));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setTitle("Download...");
            request.setDescription(mItem.getUrl());
            request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS,File.separator + folderName + File.separator + fileName);
            mDownloadManager.enqueue(request);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void downloadPhotoOrigional() {
        try {
            String folderName = "azwallpaper";

            String url = mItem.getUrl();
            URL urlObj = new URL(url);
            String urlPath = urlObj.getPath();
            String fileName = urlPath.substring(urlPath.lastIndexOf('/') + 1);
// fileName is now "somefilename.xy"

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(strOrigionalImageLink));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setTitle("Download...");
            request.setDescription(mItem.getUrl());
            request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS,File.separator + folderName + File.separator + fileName);
            mDownloadManager.enqueue(request);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    // function for opening Flickr official app when open button is pressed
    private void openApp () {
        String url = UrlManager.getInstance().getFlickrUrl(mItem.getId());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    // function for loading single photo description by Volley
    private void startLoading() {
        mLoading = true;
        mProgressBar.setVisibility(View.VISIBLE);
        String url =  UrlManager.getInstance().getPhotoInfoUrl(mItem.getId());
        JsonObjectRequest request = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject photo = response.getJSONObject("photo");
                            JSONObject descObj = photo.getJSONObject("description");
                            String desc = descObj.getString("_content");

                            // for the origional link
                            // ->> https://farm5.staticflickr.com/4177/34691100885_5773df5b0a_o.jpg



                            String farm = photo.getString("farm");
                            strOrigionalImageLink = strOrigionalImageLink + farm +".staticflickr.com/";


                            String server = photo.getString("server");
                            strOrigionalImageLink = strOrigionalImageLink + server +"/";

                            String id = photo.getString("id");
                            strOrigionalImageLink = strOrigionalImageLink + id +"_";


                            String originalsecret = photo.getString("originalsecret");
                            strOrigionalImageLink = strOrigionalImageLink + originalsecret +"_o.";

                            String originalformat = photo.getString("originalformat");
                            strOrigionalImageLink = strOrigionalImageLink + originalformat;

                            Log.i("==URL==","---Url---"+strOrigionalImageLink);

                            mDescText.setText(desc);
                        } catch (JSONException e) {
                            if(e != null) {
                                Toast.makeText(getActivity(), e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                        mProgressBar.setVisibility(View.GONE);
                        mLoading = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                    }
                }
        );

        request.setTag(TAG);
        mRq.add(request);
    }

    public void file_download(String uRl) {
        String strFolderName = "azWallpapers";
        File direct = new File(Environment.getExternalStorageDirectory()+ "/"+strFolderName);

        if (!direct.exists()) {
            direct.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI| DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("Demo")
                .setDescription("Something useful. No, really.")
                .setDestinationInExternalPublicDir("/"+strFolderName, "test.jpg");

        mgr.enqueue(request);

    }

    // cancel downloading request when fragment is stopped
    private void stopLoading() {
        if (mRq != null) {
            mRq.cancelAll(TAG);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLoading();
    }


    RelativeLayout rlDetail,rlImage;
    ImageView ivClose;
    TouchImageView ivFullScreen;
    LinearLayout viewFullscreen;
    TextView tvSetWallpaper;

    private void viewFullScreenImage()
    {

        Glide.with(getActivity()).load(strOrigionalImageLink).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                bitmap = resource;
                ivFullScreen.setImageBitmap(resource);
                mProgressBar.setVisibility(View.GONE);

            }
        });


      /*  Glide.with(getActivity())
                .load(strOrigionalImageLink)
                .thumbnail(0.5f)
                .into(ivFullScreen);
*/


    }
    Bitmap bitmap = null;
    private void setWallpaper()
    {
        String filename = "wallpaper.png";
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, filename);

        try {
            FileOutputStream out = new FileOutputStream(dest);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();


        Uri uri = Uri.fromFile(dest);
        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("mimeType", "image/*");
        this.startActivity(Intent.createChooser(intent, "Set as :"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
