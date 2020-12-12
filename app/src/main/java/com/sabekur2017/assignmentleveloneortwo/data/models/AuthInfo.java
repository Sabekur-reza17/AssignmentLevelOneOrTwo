package com.sabekur2017.assignmentleveloneortwo.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthInfo {
    @SerializedName("designation")
    @Expose
    private Object designation;
    @SerializedName("is_first_login")
    @Expose
    private Boolean isFirstLogin;
    @SerializedName("role_id")
    @Expose
    private Integer roleId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("role_name")
    @Expose
    private String roleName;
    @SerializedName("user_photo")
    @Expose
    private Object userPhoto;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("assigned_to")
    @Expose
    private Integer assignedTo;

    public Object getDesignation() {
        return designation;
    }

    public void setDesignation(Object designation) {
        this.designation = designation;
    }

    public Boolean getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(Boolean isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Object getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(Object userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Integer assignedTo) {
        this.assignedTo = assignedTo;
    }
}
