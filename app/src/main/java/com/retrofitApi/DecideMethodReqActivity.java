package com.retrofitApi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.retrofitApi.activity.DeleteRecordActivity;
import com.retrofitApi.activity.UpdateRecordActivity;
import com.retrofitApi.activity.UploadFileActivity;

public class DecideMethodReqActivity extends AppCompatActivity {
private Button getReq,postReq,putReq,deleteReq,uploadReq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decide_method_req);
//        intialize the id
        inItId();
//        listener for each request
        getReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DecideMethodReqActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        postReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DecideMethodReqActivity.this,UserRecordActivity.class);
                startActivity(intent);
            }
        });
        putReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DecideMethodReqActivity.this, UpdateRecordActivity.class);
                startActivity(intent);
            }
        });
        deleteReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DecideMethodReqActivity.this, DeleteRecordActivity.class);
                startActivity(intent);
            }
        });
        uploadReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DecideMethodReqActivity.this, UploadFileActivity.class);
                startActivity(intent);
            }
        });

    }

    private void inItId() {
        getReq = findViewById(R.id.btn_get);
        postReq = findViewById(R.id.btn_post);
        putReq = findViewById(R.id.btn_put);
        deleteReq = findViewById(R.id.btn_delete);
        uploadReq = findViewById(R.id.btn_upload_img);

    }
}
