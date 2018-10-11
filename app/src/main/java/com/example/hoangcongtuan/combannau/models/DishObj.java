package com.example.hoangcongtuan.combannau.models;

import java.io.Serializable;

/**
 * Create by hoangcongtuan on 10/6/18.
 */
public class DishObj implements Serializable {
    public String title;
    public String description;
    public String imageUrl;
    public int price; //in thounsand VND
    public int total;
    public int rest;
    public String region;
    public String keyWords; //example: chicken|egg|vegetable

    public DishObj() {

    }

    public DishObj(Builder builder) {
        this.title = builder.title;
        this.description = builder.description;
        this.imageUrl = builder.imageUrl;
        this.price = builder.price;
        this.total = builder.total;
        this.rest = builder.rest;
        this.region = builder.region;
        this.keyWords = builder.keyWords;
    }

    public DishObj(DishObj dishObj) {
        this.title = dishObj.title;
        this.description = dishObj.description;
        this.imageUrl = dishObj.imageUrl;
        this.price = dishObj.price;
        this.total = dishObj.total;
        this.rest = dishObj.rest;
        this.region = dishObj.region;
        this.keyWords = dishObj.keyWords;
    }

    public static class Builder {
        String title;
        String description;
        String imageUrl;
        int price; //in thounsand VND
        int total;
        int rest;
        String region;
        String keyWords; //example: chicken|egg|vegetable

        public Builder(String title) {
            this.title = title;
        }


        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder setPrice(int price) {
            this.price = price;
            return this;
        }

        public Builder setTotal(int total) {
            this.total = total;
            return this;
        }

        public Builder setRest(int rest) {
            this.rest = rest;
            return this;
        }

        public Builder setRegion(String region) {
            this.region = region;
            return this;
        }

        public Builder setKeyWords(String keyWords) {
            this.keyWords = keyWords;
            return this;
        }

        public DishObj build() {
            return new DishObj(this);
        }
    }
}
