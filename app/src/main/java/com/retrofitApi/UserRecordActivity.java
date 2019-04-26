package com.retrofitApi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.retrofitApi.modal.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRecordActivity extends AppCompatActivity {
private EditText etName,etSalary,etAge,etGenId;
private Button postReq;
private String uName,uSalary,uAge;
public static final String TAG ="UserRecordActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_record);
//        initialize the id
        inItId();
//        listener for button
        postReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                get the value from the user
                    getUserInput();
                    if(!uName.isEmpty() && !uAge.isEmpty() && !uSalary.isEmpty())
                    {
//                      send the post reqest on the server
                        sendPostReq();
                    }
                    else {
                        Toast.makeText(UserRecordActivity.this, "Fill all field", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    private void sendPostReq() {
        // create the object of the Retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        UserInfo userInfo = new UserInfo(uName,uSalary,uAge);
        Call<ResponseBody> call = api.createRecord(userInfo);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.i(TAG, "onResponse: "+response.isSuccessful());
                    if(response.code()==200)
                    {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String age = jsonObject.getString("age");
                        String name = jsonObject.getString("name");
                        String salary = jsonObject.getString("salary");
                        String generatedId = jsonObject.getString("id");
                        Toast.makeText(UserRecordActivity.this, "Request Proceed Successfully", Toast.LENGTH_SHORT).show();
                        etName.setText(name);
                        etAge.setText(age);
                        etSalary.setText(salary);
                        etGenId.setText(generatedId);
                        etName.setEnabled(false);
                        etAge.setEnabled(false);
                        etSalary.setEnabled(false);

                    }
                    else {
                        Toast.makeText(UserRecordActivity.this, " Request Not Successfully Send ", Toast.LENGTH_SHORT).show();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.getMessage());
            }
        });

    }

    private void getUserInput() {
      uName = etName.getText().toString();
      uAge = etAge.getText().toString();
      uSalary = etSalary.getText().toString();
    }

    private void inItId() {
        etName = findViewById(R.id.et_name);
        etAge = findViewById(R.id.et_age);
        etSalary = findViewById(R.id.et_salary);
        postReq = findViewById(R.id.btn_create_record);
        etGenId = findViewById(R.id.et_generated_id);
    }
}
