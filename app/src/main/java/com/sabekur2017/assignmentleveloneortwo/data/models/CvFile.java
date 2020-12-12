package com.sabekur2017.assignmentleveloneortwo.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CvFile {
    @SerializedName("tsync_id")
    @Expose
    private String tsyncId;

    public String getTsyncId() {
        return tsyncId;
    }

    public void setTsyncId(String tsyncId) {
        this.tsyncId = tsyncId;
    }
}
