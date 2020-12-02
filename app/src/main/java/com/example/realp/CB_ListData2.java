package com.example.realp;

public class CB_ListData2 {

    private int cNumber;
    private String imageUrl;

    public CB_ListData2(int cNumber, String imageUrl) {
        this.cNumber = cNumber;
        this.imageUrl = imageUrl;
    }

    public int getcNumber() {
        return cNumber;
    }

    public void setcNumber(int cNumber) {
        this.cNumber = cNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
