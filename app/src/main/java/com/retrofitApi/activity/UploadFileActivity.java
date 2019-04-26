package com.retrofitApi.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.retrofitApi.Api;
import com.retrofitApi.R;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadFileActivity extends AppCompatActivity {
    public static final String TAG = "UploadFileActivity";
    private static final int REQUEST_CODE = 1;
    Uri imageUri;
    private EditText etDescrip;
    private Button uploadImg, uploadImgFromGallery;
    private String uDesc;
    private boolean isOpenGallery = false;
    private URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);
        //        initialize the id
        inItId();
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                get the value from the user
                getUserInput();
                if (!uDesc.isEmpty()) {
//                      send the post reqest on the server
                    uploadImg();
                } else {
                    Toast.makeText(UploadFileActivity.this, "Fill all field", Toast.LENGTH_SHORT).show();
                }
            }
        });
        uploadImgFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpenGallery = true;
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    private void uploadImg() {
        Toast.makeText(this, "upload images", Toast.LENGTH_SHORT).show();
        //Create a file object using file path
      //  String imgPath = imageUri.getPath();
      //  Log.i(TAG, "imgPath" + imgPath);
        String path =setImageFromGallery(imageUri);
        Log.i(TAG, "uploadImg: "+path);
        File file = new File(path);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("uploads", file.getName(), fileReqBody);
        Log.i(TAG, "file.getName(): "+file.getName());
        Log.i(TAG, "fileReqBody: "+fileReqBody);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL4).
                addConverterFactory(GsonConverterFactory.create()).build();
        Api api = retrofit.create(Api.class);
        Log.i("upload file activity","before call");
        Call<ResponseBody> call = api.uploadImage(part);
        Log.i("upload file activity","after call");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Toast.makeText(UploadFileActivity.this, ":jis"+response.code(), Toast.LENGTH_SHORT).show();
               // Log.i(TAG, "onResponse: " + response.code());
                Toast.makeText(UploadFileActivity.this, "on  response"+response.code(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(UploadFileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });



    }


    private void getUserInput() {
        uDesc = etDescrip.getText().toString();
    }

    private void inItId() {
        etDescrip = findViewById(R.id.et_desc);
        uploadImg = findViewById(R.id.btn_upload_select_img);
        uploadImgFromGallery = findViewById(R.id.btn_get_img_gallery);
    }
    //    get the callback from startActivityForResult

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (isOpenGallery) {
                if (data.getData() != null) {
                    imageUri = data.getData();
                    /** we can directly set the image  by their uri and another way
                     * is use the content resolver to fetch or set the image */
                    isOpenGallery = false;
                }
                isOpenGallery = false;
            } else {
                Log.i(TAG, "onActivityResult: else " + requestCode);
            }
        }

    }

    private String setImageFromGallery(Uri imgUri) {
        String result;
        Cursor cursor=getContentResolver().query(imgUri,null,null,null,null);
        if(cursor==null)
            result=imgUri.getPath();
        else{
            cursor.moveToNext();
            int idx=cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result=cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}



