package com.example.hoangcongtuan.combannau;

import com.example.hoangcongtuan.combannau.models.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by hoangcongtuan on 10/12/18.
 */
public class MenuManager {
    private static MenuManager sInstance;
    public List<Menu> items;

    private MenuManager() {
        items = new ArrayList<>();
    }

    public static MenuManager getInstance() {
        if (sInstance == null)
            sInstance = new MenuManager();
        return sInstance;
    }
}
