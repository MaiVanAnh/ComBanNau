package com.example.hoangcongtuan.combannau.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by hoangcongtuan on 10/6/18.
 */
public class SampleMenu {
    public String mTitle;
    public List<DishObj> mDishObjs;

    public SampleMenu() {

    }

    public SampleMenu(String mTitle, List<DishObj> mDishObjs) {
        this.mTitle = mTitle;
        this.mDishObjs = mDishObjs;
    }

    public SampleMenu(String mTitle) {
        this.mDishObjs = new ArrayList<>();
        this.mTitle = mTitle;

    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public List<DishObj> getmDishObjs() {
        return mDishObjs;
    }

    public void setmDishObjs(List<DishObj> mDishObjs) {
        this.mDishObjs = mDishObjs;
    }
}

