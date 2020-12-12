package com.sabekur2017.assignmentleveloneortwo.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("organization_logo")
    @Expose
    private String organizationLogo;
    @SerializedName("auth_info")
    @Expose
    private AuthInfo authInfo;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("app_top_module_assignment")
    @Expose
    private List<Object> appTopModuleAssignment = null;
    @SerializedName("organization_name")
    @Expose
    private String organizationName;
    @SerializedName("organization_info")
    @Expose
    private OrganizationInfo organizationInfo;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getOrganizationLogo() {
        return organizationLogo;
    }

    public void setOrganizationLogo(String organizationLogo) {
        this.organizationLogo = organizationLogo;
    }

    public AuthInfo getAuthInfo() {
        return authInfo;
    }

    public void setAuthInfo(AuthInfo authInfo) {
        this.authInfo = authInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Object> getAppTopModuleAssignment() {
        return appTopModuleAssignment;
    }

    public void setAppTopModuleAssignment(List<Object> appTopModuleAssignment) {
        this.appTopModuleAssignment = appTopModuleAssignment;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public OrganizationInfo getOrganizationInfo() {
        return organizationInfo;
    }

    public void setOrganizationInfo(OrganizationInfo organizationInfo) {
        this.organizationInfo = organizationInfo;
    }
}
