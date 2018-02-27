package com.example.shreevidya.ubs_2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.String;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;

public class Sell extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;

    EditText NAME, DESCRIPTION, POST_DATE, PRICE, CATEGORY, IMAGE;
        //ImageView IMAGE;
        TextView textView10;
        String uploadImage;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sell);


            textView10 = (TextView) findViewById(R.id.textView10);
            textView10.setOnClickListener(this);
            Button Post = (Button) findViewById(R.id.post_btn);

            NAME = (EditText) findViewById(R.id.editText3);
            DESCRIPTION = (EditText) findViewById(R.id.description);
            POST_DATE = (EditText) findViewById(R.id.date);
            PRICE = (EditText) findViewById(R.id.price);
            CATEGORY = (EditText) findViewById(R.id.Category);
            IMAGE = (EditText) findViewById(R.id.imgURL);
            imageView = (ImageView) findViewById(R.id.imageView);

            POST_DATE.setText((new SimpleDateFormat("yyyy-MM-dd").format(new Date())));

            Post.setOnClickListener(new View.OnClickListener() {



                @Override
                public void onClick(View view) {

                    new Sell.PostItem().execute(NAME.getText().toString(), DESCRIPTION.getText().toString(),
                            (new SimpleDateFormat("yyyy-MM-dd").format(new Date())), PRICE.getText().toString(), CATEGORY.getText().toString(),
                            IMAGE.getText().toString());
                }


            });
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.textView10:


                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);//
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 202);


                    break;
            }

        }
// convert image into imageString
//    public String getStringImage(Bitmap bmp){
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] imageBytes = baos.toByteArray();
//        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//        return encodedImage;
//    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 202) {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        try {

                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(Sell.this.getContentResolver(), data.getData());
                            String s = data.toString();
                            File file = new File(s);
                            String imagePath = file.getAbsolutePath();
                            //IMAGE =() imagePath;
                            //uploadImage = getStringImage(bitmap);
                            IMAGE.setText(imagePath);
                            imageView.setImageBitmap(bitmap);
                            //imgURL.setHint(imagePath);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(Sell.this, "Cancelled", Toast.LENGTH_SHORT).show();
                }
            }


        }

        private class PostItem extends AsyncTask<String, String, String> {

            ProgressDialog pdLoading = new ProgressDialog(Sell.this);
            HttpURLConnection conn;
            URL url = null;



            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                //this method will be running on UI thread
                pdLoading.setMessage("\tLoading...");
                pdLoading.setCancelable(false);
                pdLoading.show();

            }

            @Override
            protected String doInBackground(String... params) {
                try {

                    // Enter URL address where your php file resides
                    url = new URL("https://utauniversitybazaar.000webhostapp.com/api/sell.php");

                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return "exception";
                }
                try {
                    // Setup HttpURLConnection class to send and receive data from php and mysql
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000);
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("POST");

                    // setDoInput and setDoOutput method depict handling of both send and receive
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    // Append parameters to URL
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("NAME", params[0])
                            .appendQueryParameter("DESCRIPTION", params[1])
                            .appendQueryParameter("POST_DATE", params[2])
                            .appendQueryParameter("PRICE", params[3])
                            .appendQueryParameter("CATEGORY", params[4])
                            .appendQueryParameter("IMAGE", params[5]);
                    String query = builder.build().getEncodedQuery();

                    // Open connection for sending data
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();
                    conn.connect();

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    return "exception";
                }
                try {

                    int response_code = conn.getResponseCode();

                    // Check if successful connection made
                    if (response_code == HttpURLConnection.HTTP_OK) {

                        // Read data sent from server
                        InputStream input = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                        StringBuilder result = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }

                        // Pass data to onPostExecute method
                        return (result.toString());

                    } else {

                        return ("Check Again");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    return "exception";
                } finally {
                    conn.disconnect();
                }


            }

            @Override
            protected void onPostExecute(String result) {

                //this method will be running on UI thread

                pdLoading.dismiss();

                if (result.equalsIgnoreCase("Uploaded Successfully")) {

                    startActivity(new Intent(Sell.this, Sell.class));
                    finish();
                }
                Toast.makeText(Sell.this, "" + result, Toast.LENGTH_LONG).show();
            }


        }
}
