package com.example.prabodhaharankahadeniya.networking;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonDownloadActivity extends AppCompatActivity {

    private static final String TAG="JsonDownloadActivity";

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private static String url = "http://demo6983891.mockable.io/contacts";
    //http://api.androidhive.info/contacts/

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_download);

        contactList=new ArrayList<>();
        lv=(ListView)findViewById(R.id.listview);
        new GetContacts().execute();

    }

    private class GetContacts extends AsyncTask<Void, Void, Void>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog=new ProgressDialog(JsonDownloadActivity.this);
            pDialog.setMessage("Please wait");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler handler=new HttpHandler();

            String jsonStr=handler.makeServiceCall(url);
            if(jsonStr!=null){
                try {
                    JSONObject jsonObject=new JSONObject(jsonStr);
                    JSONArray contacts=jsonObject.getJSONArray("contacts");

                    for (int i=0;i<contacts.length();i++){
                        JSONObject c=contacts.getJSONObject(i);

                        String id=c.getString("id");
                        String name=c.getString("name");

                        String email=c.getString("email");

                        JSONObject phone=c.getJSONObject("phone");
                        String mobile=phone.getString("mobile");
                        String home=phone.getString("home");

                        HashMap<String,String> contact=new HashMap<>();
                        contact.put("id",id);
                        contact.put("name",name);
                        contact.put("email",email);
                        contact.put("mobile",mobile);
                        contactList.add(contact);

                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Json pasrsing error : "+ e.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "could not get json from server",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(pDialog.isShowing()){
                pDialog.dismiss();
            }


            ListAdapter adapter=new SimpleAdapter(JsonDownloadActivity.this,contactList,R.layout.list_item,
                    new String[]{"name","email","mobile"},
                    new int[]{R.id.name,R.id.email,R.id.mobile});
            lv.setAdapter(adapter);
        }


    }


}
