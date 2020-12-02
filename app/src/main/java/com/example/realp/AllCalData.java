package com.example.realp;

public class AllCalData {
    private String date;
    private String con;
    public AllCalData(String date, String con) {
        this.date = date;
        this.con = con;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCon() {
        return con;
    }

    public void setCon(String con) {
        this.con = con;
    }
}
