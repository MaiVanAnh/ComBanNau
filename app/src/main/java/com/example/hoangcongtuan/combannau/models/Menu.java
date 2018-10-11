package com.example.hoangcongtuan.combannau.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by hoangcongtuan on 10/6/18.
 */
public class Menu {
    public String ownerId;
    public String name;
    public float latitude;
    public float longtitude;
    public String startTime;
    public String endTime;
    public String address;
    public List<DishObj> items;

    public Menu() {

    }

    private Menu(Builder builder) {
        this.ownerId = builder.ownerId;
        this.name = builder.name;
        this.latitude = builder.latitude;
        this.longtitude = builder.longtitude;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.address = builder.address;
        this.items = new ArrayList<>();
        this.items.addAll(builder.items);


    }

    public static class Builder {
        public String ownerId;
        String name;
        float latitude;
        float longtitude;
        String startTime;
        String endTime;
        String address;
        List<DishObj> items;

        public Builder(String ownerId) {
            this.ownerId = ownerId;
            items = new ArrayList<>();
        }

        public Menu build() {
            return new Menu(this);
        }

        public Builder setOwnerId(String ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setLatitude(float latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder setLongtitude(float longtitude) {
            this.longtitude = longtitude;
            return this;
        }

        public Builder setStartTime(String startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder setEndTime(String endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setItems(List<DishObj> items) {
            this.items = items;
            return this;
        }

        public Builder addItem(DishObj dishObj) {
            this.items.add(dishObj);
            return this;
        }


    }
}
