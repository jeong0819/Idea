package com.example.realp;

public class BB_ListData {
    private int star,bNumber;
    private String rvTitle,rvWriter,rvTime,view1,rvPrice,rvViews,mainImage;


    public BB_ListData(int bNumber, String rvPrice, String rvViews, String mainImage, int star, String rvTitle, String rvWriter, String rvTime, String view1) {

        this.bNumber= bNumber;
        this.rvPrice = rvPrice;
        this.rvViews = rvViews;
        this.mainImage = mainImage;
        this.star = star;
        this.rvTitle = rvTitle;
        this.rvWriter = rvWriter;
        this.rvTime = rvTime;
        this.view1 = view1;
    }

    public int getbNumber() {
        return bNumber;
    }

    public void setbNumber(int bNumber) {
        this.bNumber = bNumber;
    }

    public String getRvPrice() {
        return rvPrice;
    }

    public void setRvPrice(String rvPrice) {
        this.rvPrice = rvPrice;
    }

    public String getRvViews() {
        return rvViews;
    }

    public void setRvViews(String rvViews) {
        this.rvViews = rvViews;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getRvTitle() {
        return rvTitle;
    }

    public void setRvTitle(String rvTitle) {
        this.rvTitle = rvTitle;
    }

    public String getRvWriter() {
        return rvWriter;
    }

    public void setRvWriter(String rvWriter) {
        this.rvWriter = rvWriter;
    }

    public String getRvTime() {
        return rvTime;
    }

    public void setRvTime(String rvTime) {
        this.rvTime = rvTime;
    }

    public String getView1() {
        return view1;
    }

    public void setView1(String view1) {
        this.view1 = view1;
    }
}
