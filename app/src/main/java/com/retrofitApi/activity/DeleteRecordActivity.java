package com.retrofitApi.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.retrofitApi.Api;
import com.retrofitApi.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeleteRecordActivity extends AppCompatActivity {
    private EditText etGenId;
    private Button deleteReq;
    private String uGenId;
    public static final String TAG ="DeleteRecordActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_record);
        //        initialize the id
        inItId();
        deleteReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                get the value from the user
                getUserInput();
                if(!uGenId.isEmpty())
                {
//                      send the post reqest on the server
                    sendUpdateReq();
                }
                else {
                    Toast.makeText(DeleteRecordActivity.this, "Fill all field", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendUpdateReq() {
        // create the object of the Retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);

        Call<Void> call = api.deleteRecord(Integer.parseInt(uGenId));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i(TAG, "onResponse: "+response.body());
                if(response.code()==200)
                {
                    Toast.makeText(DeleteRecordActivity.this, "Record Deleted Successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(DeleteRecordActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    private void getUserInput() {
        uGenId = etGenId.getText().toString();
    }

    private void inItId() {
        etGenId = findViewById(R.id.et_generated_id_delete);
        deleteReq = findViewById(R.id.btn_delete_record);
    }
}
