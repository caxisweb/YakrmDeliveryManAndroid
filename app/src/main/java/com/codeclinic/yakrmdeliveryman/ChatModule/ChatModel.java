package com.codeclinic.yakrmdeliveryman.ChatModule;

public class ChatModel {
    private String orderId, userType, message, customerId, customerName, driverId, driverName, cDate, cTime;

    public ChatModel(String orderId, String userType, String message, String customerId, String customerName, String driverId, String driverName, String cDate, String cTime) {
        this.orderId = orderId;
        this.message = message;
        this.userType = userType;
        this.customerId = customerId;
        this.customerName = customerName;
        this.driverId = driverId;
        this.driverName = driverName;
        this.cDate = cDate;
        this.cTime = cTime;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getcDate() {
        return cDate;
    }

    public void setcDate(String cDate) {
        this.cDate = cDate;
    }

    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }
}
