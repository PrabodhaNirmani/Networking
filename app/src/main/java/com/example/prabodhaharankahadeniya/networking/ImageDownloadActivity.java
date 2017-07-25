package com.example.prabodhaharankahadeniya.networking;

import android.app.Notification;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ImageDownloadActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    private Bitmap bitmap = null;
    Message msg;
    private final String url="http://www.gstatic.com/webp/gallery/1.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_download);
        msg = Message.obtain();

    }

    public void onClickDownload(View v){
        new DownloadImage().execute();
    }


    private class DownloadImage extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
//            pDialog = new ProgressDialog(ImageDownloadActivity.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            if(checkInternetConnection()){
                InputStream in=null;


                msg.what=1;
                try{
                    in=openHttpConnection();
                    bitmap= BitmapFactory.decodeStream(in);
                    Bundle bundle=new Bundle();
                    bundle.putParcelable("bitmap",bitmap);
                    msg.setData(bundle);
                    in.close();


                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            if(pDialog.isShowing()){
//                pDialog.dismiss();
//            }
            messageHandler.sendMessage(msg);



        }

        private Handler messageHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ImageView img = (ImageView) findViewById(R.id.image_view);
                img.setImageBitmap((Bitmap) (msg.getData().getParcelable("bitmap")));

            }
        };

        private InputStream openHttpConnection(){
            InputStream in=null;
            int resCode= -1;
            try {
                URL urlStr=new URL(url);
                URLConnection urlConn = urlStr.openConnection();
                if(!(urlConn instanceof HttpURLConnection)){
                    throw new IOException("URL is not a http url");
                }

                HttpURLConnection httpConn=(HttpURLConnection)urlConn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                resCode=httpConn.getResponseCode();
                if(resCode==HttpURLConnection.HTTP_OK){
                    in = httpConn.getInputStream();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return in;
        }

        private boolean checkInternetConnection(){
            ConnectivityManager connect=(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

            if(connect.getActiveNetworkInfo().getState() ==
                    android.net.NetworkInfo.State.CONNECTED ||
                    connect.getActiveNetworkInfo().getState() ==
                            NetworkInfo.State.CONNECTING){


                return true;
            }
            else if(connect.getActiveNetworkInfo().getState() ==
                    NetworkInfo.State.DISCONNECTED){


                return  false;
            }
            return false;
        }
    }
}
