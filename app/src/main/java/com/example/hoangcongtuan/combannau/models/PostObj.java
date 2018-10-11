package com.example.hoangcongtuan.combannau.models;

/**
 * Created by hoangcongtuan on 3/23/18.
 */

public class PostObj {
    public String ownerID;
    public String title;
    public String description;
    public String imageUrl;
    public float latitude;
    public float longtitude;
    public String address;
    public int price; //in thounsand VND
    public int total;
    public int rest;
    public String region;
    public String keyWords; //example: chicken|egg|vegetable
    public String postTime;
    public String endTime;


    public PostObj() {

    }

    public PostObj(Builder builder) {
        this.ownerID = builder.ownerID;
        this.title = builder.title;
        this.description = builder.description;
        this.imageUrl = builder.imageUrl;
        this.latitude = builder.latitude;
        this.longtitude = builder.longtitude;
        this.address = builder.address;
        this.price = builder.price;
        this.total = builder.total;
        this.rest = builder.rest;
        this.region = builder.region;
        this.keyWords = builder.keyWords;
        this.postTime = builder.postTime;
        this.endTime = builder.endTime;
    }

    public PostObj(PostObj postObj) {
        this.ownerID = postObj.ownerID;
        this.title = postObj.title;
        this.description = postObj.description;
        this.imageUrl = postObj.imageUrl;
        this.latitude = postObj.latitude;
        this.longtitude = postObj.longtitude;
        this.address = postObj.address;
        this.price = postObj.price;
        this.total = postObj.total;
        this.rest = postObj.rest;
        this.region = postObj.region;
        this.keyWords = postObj.keyWords;
        this.postTime = postObj.postTime;
        this.endTime = postObj.endTime;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(float longtitude) {
        this.longtitude = longtitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public static class Builder {
        String ownerID;
        String title;
        String description;
        String imageUrl;
        float latitude;
        float longtitude;
        public String address;
        int price; //in thounsand VND
        int total;
        int rest;
        String region;
        String keyWords; //example: chicken|egg|vegetable
        String postTime;
        String endTime;

        public Builder(String ownerID) {
            this.ownerID = ownerID;
        }

        public String getOwnerID() {
            return ownerID;
        }

        public Builder setOwnerID(String ownerID) {
            this.ownerID = ownerID;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public Builder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public float getLatitude() {
            return latitude;
        }

        public Builder setLatitude(float latitude) {
            this.latitude = latitude;
            return this;
        }

        public float getLongtitude() {
            return longtitude;
        }

        public Builder setLongtitude(float longtitude) {
            this.longtitude = longtitude;
            return this;
        }

        public String getAddress() {
            return address;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public int getPrice() {
            return price;
        }

        public Builder setPrice(int price) {
            this.price = price;
            return this;
        }

        public int getTotal() {
            return total;
        }

        public Builder setTotal(int total) {
            this.total = total;
            return this;
        }

        public int getRest() {
            return rest;
        }

        public Builder setRest(int rest) {
            this.rest = rest;
            return this;
        }

        public String getRegion() {
            return region;
        }

        public Builder setRegion(String region) {
            this.region = region;
            return this;
        }

        public String getKeyWords() {
            return keyWords;
        }

        public Builder setKeyWords(String keyWords) {
            this.keyWords = keyWords;
            return this;
        }

        public String getPostTime() {
            return postTime;
        }

        public Builder setPostTime(String postTime) {
            this.postTime = postTime;
            return this;
        }

        public String getEndTime() {
            return endTime;
        }

        public Builder setEndTime(String endTime) {
            this.endTime = endTime;
            return this;
        }

        public PostObj build() {
            return new PostObj(this);
        }

    }
}
