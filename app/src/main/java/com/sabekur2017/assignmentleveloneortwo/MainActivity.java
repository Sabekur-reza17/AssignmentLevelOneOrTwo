package com.sabekur2017.assignmentleveloneortwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sabekur2017.assignmentleveloneortwo.data.APIClient;

import com.sabekur2017.assignmentleveloneortwo.data.models.CvFile;
import com.sabekur2017.assignmentleveloneortwo.data.models.UserDataUploadModel;
import com.sabekur2017.assignmentleveloneortwo.util.FileUtils;
import com.sabekur2017.assignmentleveloneortwo.util.PreferenceUtility;


import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextInputEditText edtUsername,edtUserEmail,edtUserPhone,edtUserAddress,edtUserUniversity,edtGraduationYear,edtUserCGpa,edtUserExperience,edtCurrentWorkPlace,edtUserrGitUrl,edtexpectedSalary,edtUsreReference;
    Button btnSubmit,btnChooseFile;
    private Uri filePath;
    private int PICK_PDF_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    TextView pdfName;
    APIClient apiClient;
    private RadioGroup radioApplymethodGroup;
    private RadioButton radioApplyMethodButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestStoragePermission();
        apiClient=new APIClient();
        radioApplymethodGroup =  findViewById(R.id.radioGrp);

        edtUsername=findViewById(R.id.txtUserName);
        edtUserEmail=findViewById(R.id.textUserMail);
        edtUserPhone=findViewById(R.id.textUserPhone);
        edtUserAddress=findViewById(R.id.textUserAddress);
        edtUserUniversity=findViewById(R.id.textUniversityName);
        edtGraduationYear=findViewById(R.id.textGruationYear);
        edtUserCGpa=findViewById(R.id.textUserCgpa);
        edtUserExperience=findViewById(R.id.textExperience);
        edtCurrentWorkPlace=findViewById(R.id.textCurrentWorkPlace);
        //edtApplyingMethod=findViewById(R.id.textApplyingMethod);
        edtexpectedSalary=findViewById(R.id.textUserSalary);
        edtUsreReference=findViewById(R.id.textFieldReference);
        edtUserrGitUrl=findViewById(R.id.textUserGithubUrl);

        pdfName=findViewById(R.id.txt_pdf_name);
        btnSubmit=findViewById(R.id.btnSubmt);
        btnChooseFile=findViewById(R.id.btn_choose_pdf);


        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRequirement();
            }
        });


    }
    private void submitRequirement(){
        int selectedId = radioApplymethodGroup.getCheckedRadioButtonId();
        radioApplyMethodButton = findViewById(selectedId);
        String  device_UDID;
        try {
            device_UDID = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            Log.d("deviceid", "Device UDID:" + device_UDID);
        } catch (Exception e) {
            device_UDID = "COULD NOT GET UDID";
            e.printStackTrace();
            Log.d("deviceiderror", "Failed to complete device UDID");
        }
        long unixTime = System.currentTimeMillis();

        UserDataUploadModel userDataUploadModel=new UserDataUploadModel();
        CvFile cvFile=new CvFile();
        cvFile.setTsyncId(device_UDID);
        userDataUploadModel.setCvFile(cvFile);
        userDataUploadModel.setTsyncId(device_UDID);
        userDataUploadModel.setName(edtUsername.getText().toString());
        userDataUploadModel.setEmail(edtUserEmail.getText().toString());
        userDataUploadModel.setPhone(edtUserPhone.getText().toString());
        userDataUploadModel.setFullAddress(edtUserAddress.getText().toString());
        userDataUploadModel.setNameOfUniversity(edtUserUniversity.getText().toString());
        userDataUploadModel.setGraduationYear(Integer.valueOf(edtGraduationYear.getText().toString()));
        userDataUploadModel.setCgpa(Double.valueOf(edtUserCGpa.getText().toString()));
        userDataUploadModel.setExperienceInMonths(Integer.valueOf(edtUserExperience.getText().toString()));
        userDataUploadModel.setCurrentWorkPlaceName(edtCurrentWorkPlace.getText().toString());
        userDataUploadModel.setApplyingIn(radioApplyMethodButton.getText().toString());
        userDataUploadModel.setExpectedSalary(Integer.valueOf(edtexpectedSalary.getText().toString()));
        userDataUploadModel.setFieldBuzzReference(edtUsreReference.getText().toString());
        userDataUploadModel.setGithubProjectUrl(edtUserrGitUrl.getText().toString());
        userDataUploadModel.setOnSpotCreationTime((int) unixTime);
        userDataUploadModel.setOnSpotUpdateTime((int) unixTime);


        Call<JsonObject> userRequirementResponseModelCall = apiClient.getApiService(okHttpClient).userRequirementUpload(userDataUploadModel);
        userRequirementResponseModelCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if(response.isSuccessful()){
                    JsonObject userRequirementResponseModel=response.body();

                    JsonElement id=userRequirementResponseModel.getAsJsonObject("cv_file").get("id");
                    int filedi=id.getAsInt();
                    Log.d("fileuploadid",String.valueOf(filedi));
                    uploadPdf(filePath,filedi);
                }else{
                    Log.d("errormess",String.valueOf(response.code()));
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                String auth = PreferenceUtility.getLoggedInToken(this);
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "token " + auth)
                        .build();
                return chain.proceed(newRequest);
            }).build();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            File file = FileUtils.getFile(this, filePath);
            pdfName.setText(file.getName());

        }
    }
    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void uploadPdf(Uri filePath,int id) {
        if(filePath==null){
            Toast.makeText(this, "please attach CV", Toast.LENGTH_SHORT).show();
        }
         // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        File file = FileUtils.getFile(this, filePath);

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(filePath)),
                        file
                );

        MultipartBody.Part partBody = MultipartBody.Part
                .createFormData("file", file.getName(), requestFile);


        Call<JsonObject> cvUploadResponseCall=apiClient.getApiService(okHttpClient).cvUload(id,partBody);
        cvUploadResponseCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful()){
                    JsonObject cvUploadResponse=response.body();

                    Log.d("jsonobjerestpon",cvUploadResponse.get("id").getAsString());
                   Log.d("jsonobjerestpon",cvUploadResponse.get("file").getAsString());
                   Log.d("jsonobjerespath",cvUploadResponse.get("path").getAsString());

                    Toast.makeText(MainActivity.this, "cv upload successful", Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("logerror",response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
}