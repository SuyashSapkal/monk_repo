package com.monk.java_implementation.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monk.java_implementation.controllers.inputClasses.CouponRequest;
import com.monk.java_implementation.controllers.inputClasses.Item;
import com.monk.java_implementation.controllers.inputClasses.UserCart;
import com.monk.java_implementation.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;

@Repository
public class CouponRepo {

    @Autowired
    EntityManager entityManager;

    public List<Coupon> getCoupons(){
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("from Coupon", Coupon.class).getResultList();
    }

    public Coupon getCouponById(Long id){
        Session session = entityManager.unwrap(Session.class);
        return session.get(Coupon.class, id);
    }

    @Transactional
    public void save(Coupon coupon){
        try{
            if(coupon.getCouponId() == null){
                entityManager.persist(coupon);
            }
            else{
                entityManager.merge(coupon);
            }
        }
        catch (Exception e){
            System.out.println("Error in saveCoupon Repository:" + e.getMessage());
            throw new RuntimeException("Error saving coupon", e);
        }
    }

    @Transactional
    public void deleteCoupon(Long id){
        try{
            Coupon coupon;
            Session session = entityManager.unwrap(Session.class);

            coupon = session.get(Coupon.class, id);

            if(coupon != null){
                session.delete(coupon);
            }
        }
        catch (Exception e){
            System.out.println("Error in deleteCoupon Repository:" + e.getMessage());
            throw new RuntimeException("Error deleting coupon", e);
        }
    }

    @Transactional
    public void updateCoupon(Long id, Coupon coupon){
        try{
            Coupon existingCoupon;
            Session session = entityManager.unwrap(Session.class);

            existingCoupon = session.get(Coupon.class, id);

            if(existingCoupon != null){
                existingCoupon.setCouponCode(coupon.getCouponCode());
                existingCoupon.setCouponName(coupon.getCouponName());
                existingCoupon.setCouponType(coupon.getCouponType());
                existingCoupon.setDiscountValue(coupon.getDiscountValue());
                existingCoupon.setStatus(coupon.getStatus());
                existingCoupon.setStartDate(coupon.getStartDate());
                existingCoupon.setEndDate(coupon.getEndDate());

                existingCoupon.getCouponDetails().clear();

                if (coupon.getCouponDetails() != null) {
                    for (CouponDetail couponDetail : coupon.getCouponDetails()) {
                        couponDetail.setCoupon(existingCoupon);
                        existingCoupon.getCouponDetails().add(couponDetail);
                    }
                }

                entityManager.merge(existingCoupon);
            }
            else{
                throw new Exception("Invalid id:"+id);
            }
        }
        catch (Exception e){
            System.out.println("Error in updateCoupon Repository:" + e.getMessage());
            throw new RuntimeException("Error updating coupon", e);
        }
    }

    @Transactional
    public void applyCoupon(Long id, UserCart userCart) {
        try {
            Session session = entityManager.unwrap(Session.class);

            Coupon coupon = session.get(Coupon.class, id);
            User user = session.get(User.class, userCart.getUser_id());

            if (coupon == null) {
                throw new Exception("Coupon not found with id: " + id);
            }
            if (user == null) {
                throw new Exception("User not found with user id: " + userCart.getUser_id());
            }

            double discountValue = coupon.getDiscountValue();

            Order order = new Order();
            order.setUser(user);
            order.setCouponIds("[" + id + "]");

            List<Long> productIds = new ArrayList<>();
            for (Item item : userCart.getCart().getItems()) {
                productIds.add(item.getProduct_id());
            }
            order.setProductIds(new ObjectMapper().writeValueAsString(productIds));

            order.setOrderTime(new Date());
            session.save(order);

            CouponUsage couponUsage = new CouponUsage();
            couponUsage.setCoupon(coupon);
            couponUsage.setUser(user);
            couponUsage.setOrder(order);
            couponUsage.setDiscountApplied(discountValue);
            couponUsage.setUsedAt(new Date());
            session.save(couponUsage);

        } catch (Exception e) {
            System.out.println("Error in applyCoupon Repository: " + e.getMessage());
            throw new RuntimeException("Error applying coupon", e);
        }
    }


}
