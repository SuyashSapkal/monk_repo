package com.monk.java_implementation.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "coupon_details")
public class CouponDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_detail_id")
    private Long couponDetailId;

    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Column(name = "detail_key", length = 100)
    private String detailKey;

    @Column(name = "detail_value", length = 255)
    private String detailValue;

    // Getters and Setters

    public Long getCouponDetailId() {
        return couponDetailId;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public String getDetailKey() {
        return detailKey;
    }

    public void setDetailKey(String detailKey) {
        this.detailKey = detailKey;
    }

    public String getDetailValue() {
        return detailValue;
    }

    public void setDetailValue(String detailValue) {
        this.detailValue = detailValue;
    }

    @Override
    public String toString() {
        return "CouponDetail{" +
                "couponDetailId=" + couponDetailId +
                ", detailKey='" + detailKey + '\'' +
                '}';
    }
}
