package com.sabekur2017.assignmentleveloneortwo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.sabekur2017.assignmentleveloneortwo.MainActivity;
import com.sabekur2017.assignmentleveloneortwo.data.APIClient;
import com.sabekur2017.assignmentleveloneortwo.R;
import com.sabekur2017.assignmentleveloneortwo.data.ApiInterface;
import com.sabekur2017.assignmentleveloneortwo.data.models.UserRequest;
import com.sabekur2017.assignmentleveloneortwo.data.models.UserResponse;
import com.sabekur2017.assignmentleveloneortwo.util.PreferenceUtility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

   // APIClient apiClient;
    private ApiInterface apiInterface;
    TextInputEditText edtUserName,edtPassword;
    Button btnLogin;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtUserName=findViewById(R.id.textInputEditUserId);
        edtPassword=findViewById(R.id.textInputEditPassword);
        progressBar=findViewById(R.id.login_progress);
        btnLogin=findViewById(R.id.btn_login);
        apiInterface=APIClient.createService(ApiInterface.class);
     //   apiClient=new APIClient();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }
    private void login(){

        if (TextUtils.isEmpty(edtUserName.getText().toString())) {
            edtUserName.setError("Invalid user");
            return;
        }


        if (!isValidEmail(edtUserName.getText().toString())) {
            edtUserName.setError("Email is invalid");
            return;
        }

        if (TextUtils.isEmpty(edtPassword.getText().toString())) {
            edtPassword.setError("Password Required");
            return;
        }

        String username=edtUserName.getText().toString();
        String password=edtPassword.getText().toString();

        UserRequest userRequest=new UserRequest(username,password);
      //  Call<UserResponse> call = apiClient.getApiService().logInWithPhnNum(userRequest);
        Call<UserResponse> call = apiInterface.logInWithPhnNum(userRequest);
        showLoading();
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful()){
                    UserResponse userResponse=response.body();
                    String tokenid=userResponse.getToken();
                    PreferenceUtility.setLoggedInToken(LoginActivity.this
                            ,tokenid
                            );
                    Log.d("usertoken",tokenid);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    hideLoading();
                }else {
                    Log.d("responseerror",response.message());
                    Toast.makeText(LoginActivity.this, "try again,something went wrong", Toast.LENGTH_SHORT).show();
                    hideLoading();
                }

            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "try again,something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public boolean isValidEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);

    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }
}