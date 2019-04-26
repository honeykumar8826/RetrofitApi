package com.retrofitApi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.retrofitApi.Api;
import com.retrofitApi.R;
import com.retrofitApi.UserRecordActivity;
import com.retrofitApi.modal.UpdateRecord;
import com.retrofitApi.modal.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateRecordActivity extends AppCompatActivity {
    public static final String TAG = "UpdateRecordActivity";
    // create the object of the Retrofit
    Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL2)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Api api = retrofit.create(Api.class);
    private EditText etName, etSalary, etAge, etGenId;
    private Button updateReq, patchReq;
    private String uName, uSalary, uAge, uGenId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_record);
        //        initialize the id
        inItId();
        //        listener for button update
        updateReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                get the value from the user
                getUserInput();
                if (!uName.isEmpty() && !uAge.isEmpty() && !uSalary.isEmpty() && !uGenId.isEmpty()) {
//                      send the post reqest on the server
                    sendUpdateReq();
                } else {
                    Toast.makeText(UpdateRecordActivity.this, "Fill all field", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //        listener for button patch
        patchReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                get the value from the user
                getUserInput();
                if (!uName.isEmpty() || !uAge.isEmpty() || !uSalary.isEmpty() || !uGenId.isEmpty()) {

//                      send the post reqest on the server
                    sendPatchReq();
                } else {
                    Toast.makeText(UpdateRecordActivity.this, "Fill all field", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendPatchReq() {
        // create the object of the Retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL3)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<ResponseBody> call = api.updateSomeField(Integer.parseInt(uGenId), new UserInfo(uName, uSalary));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Toast.makeText(UpdateRecordActivity.this, "Record Updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UpdateRecordActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "onResponse: " + response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void sendUpdateReq() {
        // create the object of the Retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        UserInfo userInfo = new UserInfo(uName, uSalary, uAge);
/*        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("name",uName);
        hashMap.put("salary",uSalary);
        hashMap.put("id",uGenId);
        hashMap.put("age",uAge);*/
        Call<ResponseBody> call = api.updateRecord(Integer.parseInt(uGenId), userInfo);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // String result = response.body().string();
                Log.i(TAG, "onResponse: " + response.isSuccessful());
                String result = null;
                try {
                    result = response.body().string();
                    Log.i(TAG, "onResponse: " + result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (response.code() == 200) {
                        String age = jsonObject.getString("age");
                        String name = jsonObject.getString("name");
                        String salary = jsonObject.getString("salary");
                        //String generatedId = jsonObject.getString("id");
                        Toast.makeText(UpdateRecordActivity.this, "Record Updated Successfully", Toast.LENGTH_SHORT).show();
                        etName.setText(name);
                        etAge.setText(age);
                        etSalary.setText(salary);
                        // etGenId.setText(generatedId);
                        etName.setEnabled(false);
                        etAge.setEnabled(false);
                        etSalary.setEnabled(false);
                        etGenId.setEnabled(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "onFailure: ");
            }
        });
    }

    private void inItId() {
        etName = findViewById(R.id.et_name_put);
        etAge = findViewById(R.id.et_age_put);
        etSalary = findViewById(R.id.et_salary_put);
        updateReq = findViewById(R.id.btn_update_record);
        etGenId = findViewById(R.id.et_generated_id_put);
        patchReq = findViewById(R.id.btn_update_any_record);
    }

    private void getUserInput() {
        uName = etName.getText().toString();
        uAge = etAge.getText().toString();
        uSalary = etSalary.getText().toString();
        uGenId = etGenId.getText().toString();
    }
//    putting the menubar in this activity

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.put_req:
                updateReq.setVisibility(View.VISIBLE);
                patchReq.setVisibility(View.GONE);
                etSalary.setEnabled(true);
                break;
            case R.id.patch_req:
                patchReq.setVisibility(View.VISIBLE);
                updateReq.setVisibility(View.GONE);
                etSalary.setEnabled(false);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
