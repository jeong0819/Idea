package com.example.realp;

public class CalData {
    private String time;
    private String contents;
    private int iv_cancal;
    private boolean tsw;
    public int getIv_cancal() {
        return iv_cancal;
    }

    public void setIv_cancal(int iv_cancal) {
        this.iv_cancal = iv_cancal;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }


    public boolean isTsw() {
        return tsw;
    }

    public void setTsw(boolean tsw) {
        this.tsw = tsw;
    }

    public CalData(String time, String contents, int iv_cancal, Boolean tsw) {
        this.time = time;
        this.contents = contents;
        this.iv_cancal=iv_cancal;
        this.tsw=tsw;
    }
}
