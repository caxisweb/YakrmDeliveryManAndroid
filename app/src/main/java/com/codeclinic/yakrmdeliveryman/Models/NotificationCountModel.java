package com.codeclinic.yakrmdeliveryman.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationCountModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("total_noti")
    @Expose
    private Integer totalNoti;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTotalNoti() {
        return totalNoti;
    }

    public void setTotalNoti(Integer totalNoti) {
        this.totalNoti = totalNoti;
    }
}
