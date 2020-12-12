package com.sabekur2017.assignmentleveloneortwo.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrganizationInfo {
    @SerializedName("organization_status")
    @Expose
    private Integer organizationStatus;
    @SerializedName("translated_project_name")
    @Expose
    private String translatedProjectName;
    @SerializedName("project_name")
    @Expose
    private String projectName;
    @SerializedName("vat_registration_number")
    @Expose
    private String vatRegistrationNumber;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("code")
    @Expose
    private String code;

    public Integer getOrganizationStatus() {
        return organizationStatus;
    }

    public void setOrganizationStatus(Integer organizationStatus) {
        this.organizationStatus = organizationStatus;
    }

    public String getTranslatedProjectName() {
        return translatedProjectName;
    }

    public void setTranslatedProjectName(String translatedProjectName) {
        this.translatedProjectName = translatedProjectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getVatRegistrationNumber() {
        return vatRegistrationNumber;
    }

    public void setVatRegistrationNumber(String vatRegistrationNumber) {
        this.vatRegistrationNumber = vatRegistrationNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
