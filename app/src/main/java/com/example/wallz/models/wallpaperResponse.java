package com.example.wallz.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class wallpaperResponse {

    @SerializedName("photos")
    private List<Wallpaper> photosList;

    public wallpaperResponse(List<Wallpaper> photosList) {
        this.photosList = photosList;
    }

    public List<Wallpaper> getPhotosList() {
        return photosList;
    }

    public void setPhotosList(List<Wallpaper> photosList) {
        this.photosList = photosList;
    }
}