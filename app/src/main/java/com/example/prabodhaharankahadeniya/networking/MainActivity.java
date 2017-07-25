package com.example.prabodhaharankahadeniya.networking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="MainActivity";

//    Button json, image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        json=(Button)findViewById(R.id.json);
//        image=(Button)findViewById(R.id.image);
    }



    public void onClick(View view) {
        if(view.getId()==R.id.json){
            Log.d(TAG,"json button clicked");
            Intent intent=new Intent(this,JsonDownloadActivity.class);
            startActivity(intent);

        }else if(view.getId()==R.id.image) {
            Log.d(TAG,"image button clicked");
            Intent intent=new Intent(this,ImageDownloadActivity.class);
            startActivity(intent);

        }

    }
}
