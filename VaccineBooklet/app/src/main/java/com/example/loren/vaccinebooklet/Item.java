package com.example.loren.vaccinebooklet;

import android.graphics.drawable.Drawable;

public class Item{
    public final String text;
    public final Drawable icon;
    public Item(String text, Drawable icon) {
        this.text = text;
        this.icon = icon;
    }
    @Override
    public String toString() {
        return text;
    }
}