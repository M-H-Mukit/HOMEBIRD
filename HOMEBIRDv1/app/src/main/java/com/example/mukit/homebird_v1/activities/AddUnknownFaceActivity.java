package com.example.mukit.homebird_v1.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.mukit.homebird_v1.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;

public class AddUnknownFaceActivity extends AppCompatActivity {
    Toolbar toolbar;

    String encodedString;
    ImageView img;
    private EditText inputName, inputRelation;
    RequestParams params = new RequestParams();
    ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_unknown_face);
        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);

        // tyo show uploading progress
        prgDialog = new ProgressDialog(this);

        // Set Cancelable as False
        prgDialog.setCancelable(false);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getIncomingIntent();

    }

    public void backToAlert(View view){

        Intent intent = new Intent(AddUnknownFaceActivity.this, AlertActivity.class);
        startActivity(intent);
    }

    private void getIncomingIntent(){


        if(getIntent().hasExtra("image_url")){

            String imageUrl = getIntent().getStringExtra("image_url");

            setImage(imageUrl);
        }
    }

    private void setImage(String imageUrl){

        img= findViewById(R.id.img_view);
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(img);
    }


    public void addImage(View v) {

        // get the image nme
        inputName =findViewById(R.id.name_input1);
        inputRelation= findViewById(R.id.relation_input1);

        String relation= inputRelation.getText().toString();
        String name = inputName.getText().toString();

        Log.d("XXXX", name);

        if(name.isEmpty() || relation.isEmpty() ){
            showToast("Name or Relation input missing");
        } else {
            // Put file name in Async Http Post Param which will used in Php web app
            params.put("name", name);
            params.put("relation", relation);



            // When Image is selected from Gallery
            if (img != null ) {
                prgDialog.setMessage("Processing Image");
                prgDialog.show();
                // Convert image to String using Base64
                encodeImagetoString();
                // When Image is not selected from Gallery
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "No image selected",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

//    public void uploadImages(View v){
//
//
//        if ( galleryButtonClicked == 1) {
//
//            for (String Path : allImgPaths) {
//
//                imgPath = Path;
//                Log.d("XXXX", Path);
//                uploadToServer(Path);
//                Log.d("XXXX", "tttttttttttttttttttttttttttttttttttt");
//
//            }
//        }
//        else {
//            showToast("NO IMAGE SELECTED");
//        }
//    }
//
//    public void uploadToServer(String Path)
//    {
//        // Get the Image's file name
//
//        inputName =findViewById(R.id.name_input);
//        String name = inputName.getText().toString();
//
//        if(name.isEmpty()){
//            showToast("Input Name");
//        } else {
//
//            filename =  name +"_" + String.valueOf(i) +".jpg";
//            // Put file name in Async Http Post Param which will used in Php web app
//            params.put("filename", filename);
//
//
//            if (Path != null && !Path.isEmpty()) {
//                prgDialog.setMessage("Processing Image");
//                prgDialog.show();
//                // Convert image to String using Base64
//                encodeImagetoString();
//                // When Image is not selected from Gallery
//            } else {
//                Toast.makeText(
//                        getApplicationContext(),
//                        "You must select image from gallery before you try to upload",
//                        Toast.LENGTH_LONG).show();
//            }
//        }
//
//
//
//    }

    // AsyncTask - To convert Image to String
    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            };

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                Bitmap bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 10, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                prgDialog.setMessage("Calling Upload");
                // Put converted Image string into Async Http Post param
                params.put("img", encodedString);
                // Trigger Image upload
                triggerImageUpload();
            }
        }.execute(null, null, null);
    }

    public void triggerImageUpload() {
        makeHTTPCall();
    }

    // Make Http call to upload Image to Php server
    public void makeHTTPCall() {
        prgDialog.setMessage("Uploading To Server");
        AsyncHttpClient client = new AsyncHttpClient();

        // TODO Don't forget to change the IP address to your LAN address. Port no as well.

//        "http://192.168.43.51:6060/imgupload/upload_image.php"
//        http://18.220.219.13:5001/up

        client.post("http://18.220.219.13:5001/up",
                params, new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        prgDialog.hide();
                        inputName.setText("");
                        inputRelation.setText("");

                        // TODO
                        showToast("Uploaded Successfully");
//                        Toast.makeText(getApplicationContext(), response,
//                                Toast.LENGTH_LONG).show();
                    }

                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // Hide Progress Dialog

                        prgDialog.hide();
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Error Occured n Most Common Error: n1. Device not connected to Internetn2. Web App is not deployed in App servern3. App server is not runningn HTTP Status code : "
                                            + statusCode, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }


    // ------- for toolbars
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id= item.getItemId();

        switch (id){
            case R.id.home_id:
                // on click home
                finish();
                Intent returnHome = new Intent(AddUnknownFaceActivity.this, MainActivity.class);
                startActivity(returnHome);

                break;
        }

        return super.onOptionsItemSelected(item);
    }
    public void showToast(String str){

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, str, duration);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Dismiss the progress bar when application is closed
        if (prgDialog != null) {
            prgDialog.dismiss();
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        overridePendingTransition(R.anim.right_to_left,R.anim.right_to_left);
//    }
}
