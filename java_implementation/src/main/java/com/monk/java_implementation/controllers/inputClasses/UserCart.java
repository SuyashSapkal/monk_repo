package com.monk.java_implementation.controllers.inputClasses;

public class UserCart {

    private int user_id;
    private Cart cart;

    // Getters and setters

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}

