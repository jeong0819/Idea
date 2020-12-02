package com.example.realp;

public class CB_ListData {
    private int cNumber,star2;
    private String cTitle,cWriter,cTime,view2,Hperson,cViews,cMainImage;

    public CB_ListData(int cNumber, int star2, String cMainImage, String cTitle, String cWriter, String cTime, String view2, String hperson, String cViews) {
        this.cNumber = cNumber;
        this.star2 = star2;
        this.cTitle = cTitle;
        this.cWriter = cWriter;
        this.cTime = cTime;
        this.view2 = view2;
        Hperson = hperson;
        this.cViews = cViews;
        this.cMainImage = cMainImage;
    }

    public int getcNumber() {
        return cNumber;
    }

    public void setcNumber(int cNumber) {
        this.cNumber = cNumber;
    }

    public int getStar2() {
        return star2;
    }

    public void setStar2(int star2) {
        this.star2 = star2;
    }

    public String getcTitle() {
        return cTitle;
    }

    public void setcTitle(String cTitle) {
        this.cTitle = cTitle;
    }

    public String getcWriter() {
        return cWriter;
    }

    public void setcWriter(String cWriter) {
        this.cWriter = cWriter;
    }

    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }

    public String getView2() {
        return view2;
    }

    public void setView2(String view2) {
        this.view2 = view2;
    }

    public String getHperson() {
        return Hperson;
    }

    public void setHperson(String hperson) {
        Hperson = hperson;
    }

    public String getcViews() {
        return cViews;
    }

    public void setcViews(String cViews) {
        this.cViews = cViews;
    }

    public String getcMainImage() {
        return cMainImage;
    }

    public void setcMainImage(String cMainImage) {
        this.cMainImage = cMainImage;
    }
}
