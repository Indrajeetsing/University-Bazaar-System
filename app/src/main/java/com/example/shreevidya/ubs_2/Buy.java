/* Author: Diana Mascarenhas
Created on : 02/25/2018
*/

package com.example.shreevidya.ubs_2;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Buy extends AppCompatActivity {

    ListView lv;
    ArrayAdapter<String> adapter;
    String address= "https://utauniversitybazaar.000webhostapp.com/api/Buy.php";
    InputStream is= null;
    String line= null;
    String result= null;
    String[] data= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);


        lv= (ListView) findViewById(R.id.ListView1);
        //Allow Network in Main thread
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        // Retrieve
        getData();

        //Adapter
        adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data);
        lv.setAdapter(adapter);
    }
    private void getData()
    {
        try {
            URL url = new URL(address);
            HttpURLConnection con= (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.connect();
            is= new BufferedInputStream(con.getInputStream());



        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            BufferedReader br= new BufferedReader(new InputStreamReader(is));
            StringBuilder sb= new StringBuilder();

            while((br.readLine()) !=null)
            {
                sb.append(line+ "\n");
            }
            is.close();
            result= sb.toString();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        //Parse JSON Data
        try
        {
            JSONArray ja= new JSONArray(result);
            JSONObject jo= null;
            data= new String[ja.length()];

            for(int i=0;i<ja.length();i++)
            {
                jo= ja.getJSONObject(i);
                data[i]= jo.getString("Name");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}