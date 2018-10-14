package com.example.hoangcongtuan.combannau.Utils;

import com.example.hoangcongtuan.combannau.models.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by hoangcongtuan on 10/14/18.
 */
public class CustomerMenuManager {
    private static CustomerMenuManager sInstance;
    public List<Menu> items;

    private CustomerMenuManager() {
        items = new ArrayList<>();
    }

    public static CustomerMenuManager getsInstance() {
        if (sInstance == null)
            sInstance = new CustomerMenuManager();
        return sInstance;
    }
}
