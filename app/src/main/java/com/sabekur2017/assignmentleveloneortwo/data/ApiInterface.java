package com.sabekur2017.assignmentleveloneortwo.data;

import com.google.gson.JsonObject;
import com.sabekur2017.assignmentleveloneortwo.data.models.UserDataUploadModel;
import com.sabekur2017.assignmentleveloneortwo.data.models.UserRequest;
import com.sabekur2017.assignmentleveloneortwo.data.models.UserResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {
    @POST("login/")
    Call<UserResponse> logInWithPhnNum(@Body UserRequest request);
    @POST("v0/recruiting-entities/")
    Call<JsonObject> userRequirementUpload(@Body UserDataUploadModel userDataUploadModel);

    @Multipart
    @PUT("file-object/{id}/")
    Call<JsonObject> cvUload(
            @Path("id") int Id,
            @Part MultipartBody.Part pdfFile
    );
}