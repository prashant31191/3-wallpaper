package com.azwallpaper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class DownloadImage extends Activity {

    ProgressBar progressBar1;
    TextView tvProcess;
    String userImageUrl, fileName, isSetWallpaper = "0";


    String img_id = "111222333";
    String img_id_extension = ".jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.progress_dialog);

        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        tvProcess = (TextView) findViewById(R.id.tvProcess);

        progressBar1.setVisibility(View.GONE);

        userImageUrl = getIntent().getStringExtra("url");

        if (getIntent().getStringExtra("setwallpaper") != null) {
            isSetWallpaper = getIntent().getStringExtra("setwallpaper");
        }
        if (getIntent().getStringExtra("img_id") != null) {
            img_id = getIntent().getStringExtra("img_id");

            if(img_id.contains("www.") || img_id.contains("http")) {
                img_id = URLUtil.guessFileName(img_id, null, null);
                System.out.println("==new-->img_id=="+img_id);
            }
        }




        if (userImageUrl != null && userImageUrl != "") {

            System.out.println("==userImageUrl start downloading==");


            img_id_extension = userImageUrl.substring(userImageUrl.lastIndexOf('/') + 1, userImageUrl.lastIndexOf('.')); //  add comment and remove extension

            if(img_id.contains("."))
            {
                fileName = img_id;
            }
            else
            {
                fileName = img_id+img_id_extension;
            }


            System.out.println("==file name is==" + fileName);

            String filePath = App.getAppFolderName()+ "/" + fileName;

            System.out.println("==op/ file path is==" + filePath);

            File file = new File(filePath);
            if (file.exists()) {
                //Do something
                if(isSetWallpaper.equalsIgnoreCase("1")) {
                    Uri uri = Uri.fromFile(new File(filePath));
                    Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(uri, "image/*");
                    intent.putExtra("mimeType", "image/*");
                    this.startActivity(Intent.createChooser(intent, "Set as :"));
                }

                if(isSetWallpaper.equalsIgnoreCase("2")) {

                    Uri uri = Uri.fromFile(new File(filePath));
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(intent, "Share Image"));
                }
//                Toast.makeText(DownloadImage.this,"Image already downloaded.",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                // Do something else.
                new DownloadFileFromURL().execute(userImageUrl);
            }



        } else {
            System.out.println("==userImageUrl not getting==");
        }

    }

    /**
     * Background Async Task to download file
     */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar1.setVisibility(View.VISIBLE);
            //	        showDialog(progressBar1);
        }

        /**
         * Downloading file in background thread
         */
        @SuppressLint("SdCardPath")
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                //main--InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file

				/*File file = new File("/sdcard/OrpheausApp/"+fileName);
				if (file.exists())
				{
					fileName = "Selfie_"+fileName;
				}*/

                File dir = new File(App.getAppFolderName());
                try {
                    if (dir.mkdirs()) {
                        System.out.println("Directory created");
                    } else {
                        System.out.println("Directory is not created");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                OutputStream output = new FileOutputStream(App.getAppFolderName()+ "/" + fileName);

                System.out.println("==save image path is ==>>" + App.getAppFolderName()+ "/" + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressBar1.setProgress(Integer.parseInt(progress[0]));
            tvProcess.setText("" + Integer.parseInt(progress[0]) + "%");
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            //      dismissDialog(progressBar1);
            progressBar1.setVisibility(View.GONE);

            System.out.println("=On post=");



            String filePath = App.getAppFolderName()+ "/" + fileName;
            System.out.println("==Save path is ==" +filePath);

            if (isSetWallpaper.equalsIgnoreCase("1")) {
                Uri uri = Uri.fromFile(new File(filePath));
                Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setDataAndType(uri, "image/*");
                intent.putExtra("mimeType", "image/*");
                startActivity(Intent.createChooser(intent, "Set as :"));
            }

            if(isSetWallpaper.equalsIgnoreCase("2")) {

                Uri uri = Uri.fromFile(new File(filePath));
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, "Share Image"));


                  /*  intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(uri, "image*//*");
                    intent.putExtra("mimeType", "image*//*");
                    this.startActivity(Intent.createChooser(intent, "Set as :"));*/
            }

            setResult(RESULT_OK);
            finish();
        }

    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DownloadImage.this);

        alertDialogBuilder

                .setTitle("").setMessage("Are you sure do you want to cancel download ?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        File file = new File(App.getAppFolderName()+ "/" + fileName);
                        if (file.exists()) {
                            file.delete();
                            System.out.println("=File deleted=");
                        }
                        setResult(RESULT_OK);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

}

