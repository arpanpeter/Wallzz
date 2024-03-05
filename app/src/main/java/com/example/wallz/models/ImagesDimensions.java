package com.example.wallz.models;

import com.google.gson.annotations.SerializedName;

public class ImagesDimensions {

        @SerializedName("medium")
        private String medium;

        @SerializedName("large")
        private String large;

        public ImagesDimensions(String medium, String large) {
            this.medium = medium;
            this.large = large;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }
    }

