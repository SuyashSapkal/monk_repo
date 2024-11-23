package com.monk.java_implementation.controllers;

import com.monk.java_implementation.controllers.inputClasses.CouponRequest;

import com.monk.java_implementation.controllers.inputClasses.UserCart;
import com.monk.java_implementation.entities.Coupon;
import com.monk.java_implementation.services.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponsController{
    @Autowired
    CouponService couponService;

    @GetMapping("/")
    public ResponseEntity<List<Coupon>> getCouponData(){
        List<Coupon> data = couponService.getCouponData();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coupon> getCouponById(@PathVariable Long id){
        Coupon data = couponService.getCouponById(id);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/")
    public ResponseEntity<String> createCoupon(@RequestBody CouponRequest couponRequest) {
        try{
            System.out.println(couponRequest);
            couponService.saveCoupon(couponRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Coupon created successfully.");
        }
        catch (Exception e){
            System.out.println("Error in createCoupon PostMapping:" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCoupon(@PathVariable Long id){
        try{
            couponService.deleteCoupon(id);
            return ResponseEntity.status(HttpStatus.OK).body("Coupon deleted successfully.");
        }
        catch (Exception e){
            System.out.println("Error in deleteCoupon DeleteMapping:" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCoupon(@PathVariable Long id, @RequestBody CouponRequest couponRequest){
        try{
            couponService.updateCoupon(id, couponRequest);
            return ResponseEntity.status(HttpStatus.OK).body("Coupon updated successfully.");
        }
        catch (Exception e){
            System.out.println("Error in updateCoupon PutMapping:" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/apply-coupon/{id}")
    public ResponseEntity<String> applyCoupon(@PathVariable Long id, @RequestBody UserCart userCart){
        try{
            couponService.applyCoupon(id, userCart);
            return ResponseEntity.status(HttpStatus.OK).body("Coupon updated successfully.");
        }
        catch (Exception e){
            System.out.println("Error in updateCoupon PutMapping:" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}