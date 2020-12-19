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
import android.text.TextUtils;
import android.util.Base64;
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
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sabekur2017.assignmentleveloneortwo.data.APIClient;

import com.sabekur2017.assignmentleveloneortwo.data.ApiInterface;
import com.sabekur2017.assignmentleveloneortwo.data.models.CvFile;
import com.sabekur2017.assignmentleveloneortwo.data.models.UserDataUploadModel;
import com.sabekur2017.assignmentleveloneortwo.util.FilePath;
import com.sabekur2017.assignmentleveloneortwo.util.Networkcheck;
import com.sabekur2017.assignmentleveloneortwo.util.PreferenceUtility;


import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
   // APIClient apiClient;
    ApiInterface apiInterface;
    private RadioGroup radioApplymethodGroup;
    private RadioButton radioApplyMethodButton;
    String authtoken;

    long unixTime;
    long kilobytes;
    byte[] pdfByteArray;
    MultipartBody.Part fileToUpload;
    String base64String;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestStoragePermission();
         unixTime = System.currentTimeMillis();
        apiInterface=APIClient.createService(ApiInterface.class);
        radioApplymethodGroup =  findViewById(R.id.radioGrp);
        authtoken ="token" + " " + PreferenceUtility.getLoggedInToken(this);
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
                startActivityForResult(Intent.createChooser(intent, "Select Pdf"), 101);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Networkcheck.isConnectedToInternet(getApplicationContext())){
                    submitRequirement();
                }else {
                    Toast.makeText(getApplicationContext(), "check the internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void submitRequirement(){
        if(dataValidation()){
            int selectedId = radioApplymethodGroup.getCheckedRadioButtonId();
            radioApplyMethodButton = findViewById(selectedId);
            String  device_UDID;
            try {

                UUID uid = UUID.fromString("9cd7c32cf97e623a95dd62c3d2cca8015517508a");

                // checking the value of random UUID
                device_UDID=uid.randomUUID().toString();
                System.out.println("Random UUID value: "+uid.randomUUID());
                Log.d("deviceid", "Device UDID:" + device_UDID);
            } catch (Exception e) {
                device_UDID = "COULD NOT GET UDID";
                e.printStackTrace();
                Log.d("deviceiderror", "Failed to complete device UDID");
            }


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


            //  Call<JsonObject> userRequirementResponseModelCall = apiClient.getApiService(okHttpClient).userRequirementUpload(userDataUploadModel);
            Call<JsonObject> userRequirementResponseModelCall = apiInterface.userRequirementUpload(authtoken,userDataUploadModel);
            userRequirementResponseModelCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                    if(response.code()==201){
                        JsonObject userRequirementResponseModel=response.body();
                        Gson gson=new Gson();
                        String strrees=gson.toJson(userRequirementResponseModel);

                        JsonElement id=userRequirementResponseModel.getAsJsonObject("cv_file").get("id");
                        String filedi=id.getAsString();
                        Log.d("fileuploadid",String.valueOf(filedi));
                        uploadPdf(fileToUpload,filedi);
                    }else{
                        Log.d("errormess",String.valueOf(response.code()));
                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 101 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Log.e("pdf path", filePath.getPath());

            String path = FilePath.getPathFromUri(getApplication(), filePath);
            pdfName.setText(path);
            Log.e("file path", path);
            File myFile = new File(path);

            long bytes = myFile.length();

            kilobytes = (bytes / 1024);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Path pdfPath = Paths.get(path);
                try {
                    FileInputStream fis = new FileInputStream(myFile);

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    try {
                        for (int readNum; (readNum = fis.read(buf)) != -1;) {
                            bos.write(buf, 0, readNum); //no doubt here is 0

                            System.out.println("read " + readNum + " bytes,");
                        }
                    } catch (IOException ex) {
                        Log.e(" stream exception",ex.toString());
                    }
                    byte[] byteasd = bos.toByteArray();

                    pdfByteArray = Files.readAllBytes(pdfPath);
                    base64String= Base64.encodeToString(pdfByteArray,Base64.DEFAULT);
                    RequestBody requestBody1=RequestBody.create(pdfByteArray);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), myFile);

                    RequestBody requestFile = RequestBody.create(
                            MediaType.parse(MainActivity.this.getContentResolver().getType(filePath)),
                            myFile
                    );
                    fileToUpload = MultipartBody.Part.createFormData("file", myFile.getName(), requestFile);
                    Log.e("my pdf binary file", pdfByteArray.length+"");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }






    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if (requestCode == 102) {


            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                requestStoragePermission();
                Toast.makeText(MainActivity.this, "Permission granted", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(MainActivity.this, "permission denied", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == 103) {
            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                requestStoragePermission();
                Toast.makeText(MainActivity.this, "Permission granted", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(MainActivity.this, "permission denied", Toast.LENGTH_LONG).show();
            }
        }


    }

    public void uploadPdf(MultipartBody.Part filePath, String id) {
        if(filePath==null){
            Toast.makeText(this, "please attach CV", Toast.LENGTH_SHORT).show();
        }

      //  Call<JsonObject> cvUploadResponseCall=apiClient.getApiService(okHttpClient).cvUload(id,partBody);
        Call<JsonObject> cvUploadResponseCall=apiInterface.cvUload(authtoken,id,filePath);
        cvUploadResponseCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful()){
                    JsonObject cvUploadResponse=response.body();
                    Gson gson=new Gson();
                    String struploadres=gson.toJson(cvUploadResponse);
                //    Log.d("strruploadsucces",struploadres);

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
                Log.d("upload error",call.toString());
                Log.d("upload execption",t.getMessage());
                Toast.makeText(MainActivity.this, "something went wrong uploading", Toast.LENGTH_SHORT).show();

            }
        });

    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 102);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 103);
            }
        }


    }
    private boolean dataValidation(){
        boolean status = true;
        if(TextUtils.isEmpty(edtUsername.getText().toString()) && edtUsername.getText().toString().length()<1){
            edtUsername.setError("Enter valid name");
            status=false;
        }
        if(TextUtils.isEmpty(edtUserEmail.getText().toString()) && !isValidEmail(edtUserEmail.getText().toString())){
            edtUserEmail.setError("Enter valid mail");
            status=false;
        }
        if(TextUtils.isEmpty(edtUserPhone.getText().toString()) && edtUserPhone.getText().toString().length()<14){
            edtUserPhone.setError("Enter valid phone number");
            status=false;
        }
        if(TextUtils.isEmpty(edtUserAddress.getText().toString())){
            edtUserAddress.setError("please enter address");
            status=false;
        }
        if(TextUtils.isEmpty(edtUserUniversity.getText().toString())){
            edtUserUniversity.setError("please enter university name");
            status=false;
        }
        if(TextUtils.isEmpty(edtGraduationYear.getText().toString())){
            edtGraduationYear.setError("enter valid year");
            status=false;
        }
        if(TextUtils.isEmpty(edtUserCGpa.getText().toString())){
            edtUserCGpa.setError("enter valid cgpa");
            status=false;
        }
        if(TextUtils.isEmpty(edtUserExperience.getText().toString())){
            edtUserExperience.setError("enter valid experience");
            status=false;
        }
        if(TextUtils.isEmpty(edtCurrentWorkPlace.getText().toString())){
            edtCurrentWorkPlace.setError("enter current work place");
            status=false;
        }
        if(TextUtils.isEmpty(edtexpectedSalary.getText().toString())){
            edtexpectedSalary.setError("enter expected salary");
            status=false;
        }
        if(TextUtils.isEmpty(edtUsreReference.getText().toString())){
            edtUsreReference.setError("Enter reference");
            status=false;
        }
        if(TextUtils.isEmpty(edtUserrGitUrl.getText().toString())){
            edtUserrGitUrl.setError("Enter github url");
            status=false;
        }


        return  status;
    }
    public boolean isValidEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}