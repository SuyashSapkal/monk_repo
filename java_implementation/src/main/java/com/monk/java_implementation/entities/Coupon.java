package com.monk.java_implementation.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long couponId;

    @Column(name = "coupon_code", unique = true, nullable = false, length = 50)
    private String couponCode;

    @Column(name = "coupon_name", length = 255)
    private String couponName;

    @Column(name = "coupon_type", length = 50)
    private String couponType;

    @Column(name = "discount_value")
    private Double discountValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private CouponStatus status = CouponStatus.active;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CouponDetail> couponDetails = new ArrayList<>();;

    // Getters and Setters

    public Long getCouponId() {
        return couponId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public CouponStatus getStatus() {
        return status;
    }

    public void setStatus(CouponStatus status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<CouponDetail> getCouponDetails() {
        return couponDetails;
    }

    public void setCouponDetails(List<CouponDetail> couponDetails) {
        this.couponDetails = couponDetails;
    }

    public enum CouponStatus {
        active, inactive, expired
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "couponCode='" + couponCode + '\'' +
                ", couponType='" + couponType + '\'' +
                '}';
    }
}
