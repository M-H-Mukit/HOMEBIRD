package com.example.mukit.homebird_v1.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mukit.homebird_v1.R;
import com.example.mukit.homebird_v1.model.RegisteredPerson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

public class RegisteredPersonRecyclerViewAdapter extends RecyclerView.Adapter<RegisteredPersonRecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<RegisteredPerson> mData;
    RequestOptions options;

    RequestParams params = new RequestParams();

    public RegisteredPersonRecyclerViewAdapter(Context mContext, List<RegisteredPerson> mData) {
        this.mContext = mContext;
        this.mData = mData;

        // request options for Glide
        options =new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.loading_shape)
                .error(R.drawable.loading_shape);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view =inflater.inflate(R.layout.registered_person_row_item,parent,false);

        return new RegisteredPersonRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.person_name.setText(mData.get(position).getName());
        holder.person_realtion.setText(mData.get(position).getRelation());

        // load image from the internet using Glide
        Glide.with(mContext).load(mData.get(position).getImage_url()).apply(options).into(holder.reg_img_thumbnail);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView person_name;
        TextView person_realtion;
        ImageView reg_img_thumbnail;
        ImageButton delete_item_button;

        public MyViewHolder(View itemView) {
            super(itemView);

            person_name = itemView.findViewById(R.id.reg_person_name);
            person_realtion=itemView.findViewById(R.id.relation);
            reg_img_thumbnail=itemView.findViewById(R.id.reg_thumbnail);
            delete_item_button=itemView.findViewById(R.id.remove_person);

            delete_item_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {


                       confirmation();


                    }catch (ArrayIndexOutOfBoundsException e){e.printStackTrace();}
                }
            });


        }

       public void confirmation(){

           AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
           alertDialog.setTitle("Confirm Delete!");
           alertDialog.setMessage("Do you really want to remove this face from known face list?");
           alertDialog.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   dialog.cancel();
               }
           });
           alertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {

                   // DO SOMETHING HERE
                   int position = getAdapterPosition();

                   String name;
                   name=mData.get(position).getName();
                   params.put("name", name);
                   makeHTTPCall();

                   mData.remove(position);
                   notifyItemRemoved(position);


               }
           });

           AlertDialog dialog = alertDialog.create();
           dialog.show();
       }


        public void makeHTTPCall() {
            showToast("Deleting from Server");
            AsyncHttpClient client = new AsyncHttpClient();

            // TODO Don't forget to change the IP address to your LAN address. Port no as well.

//        "http://192.168.43.51:6060/imgupload/upload_image.php"

            client.post("http://18.220.219.13:5001/delete",
                    params, new AsyncHttpResponseHandler() {
                        // When the response returned by REST has Http
                        // response code '200'
                        @Override
                        public void onSuccess(String response) {

                            // TODO
                            showToast("Deleted");
                        }

                        // When the response returned by REST has Http
                        // response code other than '200' such as '404',
                        // '500' or '403' etc
                        @Override
                        public void onFailure(int statusCode, Throwable error,
                                              String content) {

                            // When Http response code is '404'
                            if (statusCode == 404) {
                                Toast.makeText(mContext.getApplicationContext(),
                                        "Requested resource not found",
                                        Toast.LENGTH_LONG).show();
                            }
                            // When Http response code is '500'
                            else if (statusCode == 500) {
                                Toast.makeText(mContext.getApplicationContext(),
                                        "Something went wrong at server end",
                                        Toast.LENGTH_LONG).show();
                            }
                            // When Http response code other than 404, 500
                            else {
                                Toast.makeText(
                                        mContext.getApplicationContext(),
                                        "Error Occured n Most Common Error: n1. Device not connected to Internetn2. Web App is not deployed in App servern3. App server is not runningn HTTP Status code : "
                                                + statusCode, Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
        }


        public void showToast(String str){

            Context context = mContext.getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, str, duration);
            toast.show();
        }






    }







}
