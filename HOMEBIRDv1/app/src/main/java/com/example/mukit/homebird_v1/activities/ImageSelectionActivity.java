package com.example.mukit.homebird_v1.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.mukit.homebird_v1.R;
import com.example.mukit.homebird_v1.adapter.CustomGallery;
import com.example.mukit.homebird_v1.adapter.GalleryAdapter;
import com.example.mukit.homebird_v1.model.Action;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class ImageSelectionActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText inputName, inputRelation;
    private ImageButton  imgPickBtn;


    // for image upload ----------------
    Bitmap bitmap;
    ProgressDialog prgDialog;
    String imgPath,encodedString;
    RequestParams params = new RequestParams();

    //--------------------------
    ImageLoader imageLoader;

    //---------------------------
    int REQUEST_CODE =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selection);

        // tyo show uploading progress
        prgDialog = new ProgressDialog(this);

        // Set Cancelable as False
        prgDialog.setCancelable(false);


        //-----------------

        inputName = findViewById(R.id.name_input);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imgPickBtn = findViewById(R.id.img_select);
        imgPickBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, REQUEST_CODE);

            }
        });

//        initImageLoader();
//        init();

    }

    //---------------------------------
    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    private void init() {

//        handler = new Handler();
//        gridGallery = findViewById(R.id.gridGallery);
//        gridGallery.setFastScrollEnabled(true);
//        adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
//        adapter.setMultiplePick(false);
//        gridGallery.setAdapter(adapter);
//
////        viewSwitcher = findViewById(R.id.viewSwitcher);
////        viewSwitcher.setDisplayedChild(1);


//                galleryButtonClicked =1;
//                Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
//                startActivityForResult(i, REQUEST_CODE);






    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            String[] all_path = data.getStringArrayExtra("all_path");
//
//            allImgPaths =all_path;
//
//            ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();
//
//
//            for (String string : all_path) {
//
//                CustomGallery item = new CustomGallery();
//                item.sdcardPath = string;
//
//                dataT.add(item);
//
//            }
//
//
//            viewSwitcher.setDisplayedChild(0);
//            adapter.addAll(dataT);
//
//            // TODO for uploading images-----------
//
//
//        } else {
//            Toast.makeText(this, "You haven't picked Image",
//                    Toast.LENGTH_LONG).show();
//        }
//    }


    // When Image is selected from Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK
                    && null != data) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();


                imgPickBtn.setVisibility(View.GONE);

                ImageView imgView = findViewById(R.id.person_img_view);
                // Set the Image in ImageView
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));

                // TODO
                Log.d("XXXX", imgPath );

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    //---------------------------

    // TODO for image upload  ---------

    // When Upload button is clicked
    public void uploadImage(View v) {

        // get the image nme
        inputName =findViewById(R.id.name_input);
        inputRelation= findViewById(R.id.relation_input);

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
            if (imgPath != null && !imgPath.isEmpty()) {
                prgDialog.setMessage("Processing Image");
                prgDialog.show();
                // Convert image to String using Base64
                encodeImagetoString();
                // When Image is not selected from Gallery
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "You must select image from gallery before you try to upload",
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
                bitmap = BitmapFactory.decodeFile(imgPath,
                        options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 5
                        , stream);
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
                Intent returnHome = new Intent(ImageSelectionActivity.this, MainActivity.class);
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
}
