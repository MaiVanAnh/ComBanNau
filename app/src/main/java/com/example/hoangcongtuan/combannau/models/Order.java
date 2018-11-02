package com.example.hoangcongtuan.combannau.models;

import java.util.List;

/**
 * Create by hoangcongtuan on 10/14/18.
 */
public class Order {
    public String orderId;
    public String customerId;
    public String deliveryAddress;
    public String phoneContact;
    public List<String> items_key;
    public int total;
    public String orderTime;
    public String deliveryTime;
    public String state;

    public Order() {

    }
}
