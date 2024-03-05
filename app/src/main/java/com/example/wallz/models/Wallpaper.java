package com.example.wallz.models;

import com.google.gson.annotations.SerializedName;

public class Wallpaper {

    @SerializedName("src")
    private ImagesDimensions src;

    public Wallpaper(ImagesDimensions src) {
        this.src = src;

    }

    public ImagesDimensions getSrc() {
        return src;
    }

    public void setSrc(ImagesDimensions src) {
        this.src = src;
    }



}