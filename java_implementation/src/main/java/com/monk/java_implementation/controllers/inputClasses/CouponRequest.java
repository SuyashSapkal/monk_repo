package com.monk.java_implementation.controllers.inputClasses;

import java.util.Map;

public class CouponRequest {

    private String coupon_code;
    private String coupon_name;
    private String coupon_type;
    private Double discount_value;
    private String status;
    private String start_date;
    private String end_date;
    private Map<String, Object> details;

    @Override
    public String toString(){
        return "CouponDetail{" +
                "couponCode=" + coupon_code +
                ", couponName='" + coupon_name + '\'' +
                ", couponType='" + coupon_type + '\'' +
                ", discountValue='" + discount_value + '\'' +
                ", status='" + status + '\'' +
                ", startDate='" + start_date + '\'' +
                ", endDate='" + end_date + '\'' +
                ", details='" + details + '\'' +
                '}';
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getCoupon_name() {
        return coupon_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }

    public String getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(String coupon_type) {
        this.coupon_type = coupon_type;
    }

    public Double getDiscount_value() {
        return discount_value;
    }

    public void setDiscount_value(Double discount_value) {
        this.discount_value = discount_value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }
}
