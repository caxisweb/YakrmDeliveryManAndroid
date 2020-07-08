package com.codeclinic.yakrmdeliveryman.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderlistResponseModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("arab_message")
    @Expose
    private String arab_message;
    @SerializedName("data")
    @Expose
    private List<OrderListModel> orderlist = null;

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

    public List<OrderListModel> getOrderlist() {
        return orderlist;
    }

    public void setData(List<OrderListModel> orderlist) {
        this.orderlist = orderlist;
    }

    public String getArab_message() {
        return arab_message;
    }

    public void setArab_message(String arab_message) {
        this.arab_message = arab_message;
    }
}
