package com.monk.java_implementation.services;

import com.monk.java_implementation.controllers.inputClasses.CouponRequest;
import com.monk.java_implementation.controllers.inputClasses.UserCart;
import com.monk.java_implementation.entities.Coupon.CouponStatus;
import com.monk.java_implementation.entities.Coupon;
import com.monk.java_implementation.entities.CouponDetail;
import com.monk.java_implementation.repositories.CouponRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class CouponService {
    @Autowired
    CouponRepo couponRepo;

    public List<Coupon> getCouponData(){
        List<Coupon> data = couponRepo.getCoupons();
        return data;
    }

    public Coupon getCouponById(Long id){
        Coupon data = couponRepo.getCouponById(id);
        return data;
    }

    @Autowired
    private CouponRepo couponRepository;

    @Autowired
    private CouponRepo couponDetailRepository;

    @Transactional
    public void saveCoupon(CouponRequest couponRequest) {
        try{
            Coupon coupon = new Coupon();
            coupon.setCouponCode(couponRequest.getCoupon_code());
            coupon.setCouponName(couponRequest.getCoupon_name());
            coupon.setCouponType(couponRequest.getCoupon_type());
            coupon.setDiscountValue(couponRequest.getDiscount_value());
            coupon.setStatus(CouponStatus.valueOf(couponRequest.getStatus().toLowerCase()));
            coupon.setStartDate(convertStringToDate(couponRequest.getStart_date()));
            coupon.setEndDate(convertStringToDate(couponRequest.getEnd_date()));

            if (couponRequest.getDetails() != null) {
                for (Map.Entry<String, Object> entry : couponRequest.getDetails().entrySet()) {
                    CouponDetail couponDetail = new CouponDetail();
                    couponDetail.setDetailKey(entry.getKey());
                    couponDetail.setDetailValue(entry.getValue().toString());
                    couponDetail.setCoupon(coupon);
                    coupon.getCouponDetails().add(couponDetail);
                }
            }

            couponRepository.save(coupon);
        }
        catch (Exception e){
            System.out.println("Error in saveCoupon Service:" + e.getMessage());
        }
    }


    private Date convertStringToDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public void deleteCoupon(Long id){
        try{
            couponRepository.deleteCoupon(id);
        }
        catch (Exception e){
            System.out.println("Error in deleteCoupon Service:" + e.getMessage());
        }
    }

    @Transactional
    public void updateCoupon(Long id, CouponRequest couponRequest){
        try{
            Coupon coupon = new Coupon();
            coupon.setCouponCode(couponRequest.getCoupon_code());
            coupon.setCouponName(couponRequest.getCoupon_name());
            coupon.setCouponType(couponRequest.getCoupon_type());
            coupon.setDiscountValue(couponRequest.getDiscount_value());
            coupon.setStatus(CouponStatus.valueOf(couponRequest.getStatus().toLowerCase()));
            coupon.setStartDate(convertStringToDate(couponRequest.getStart_date()));
            coupon.setEndDate(convertStringToDate(couponRequest.getEnd_date()));

            if (couponRequest.getDetails() != null) {
                for (Map.Entry<String, Object> entry : couponRequest.getDetails().entrySet()) {
                    CouponDetail couponDetail = new CouponDetail();
                    couponDetail.setDetailKey(entry.getKey());
                    couponDetail.setDetailValue(entry.getValue().toString());
                    couponDetail.setCoupon(coupon);
                    coupon.getCouponDetails().add(couponDetail);
                }
            }

            couponRepository.updateCoupon(id, coupon);
        }
        catch (Exception e){
            System.out.println("Error in updateCoupon Service:" + e.getMessage());
        }
    }

    @Transactional
    public void applyCoupon(Long id, UserCart userCart){
        try{
            couponRepository.applyCoupon(id, userCart);
        }
        catch (Exception e){
            System.out.println("Error in deleteCoupon Service:" + e.getMessage());
        }
    }
}
