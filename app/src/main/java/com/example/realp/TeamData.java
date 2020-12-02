package com.example.realp;

public class TeamData {
    private String mName;
    private String mChar;
    private String mRank;
    private int mClear;
    private int psta;
    private Boolean isApp;

    public Boolean getApp() {
        return isApp;
    }

    public void setApp(Boolean app) {
        isApp = app;
    }

    public int getPsta() {
        return psta;
    }

    public void setPsta(int psta) {
        this.psta = psta;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmChar() {
        return mChar;
    }

    public void setmChar(String mChar) {
        this.mChar = mChar;
    }

    public String getmRank() {
        return mRank;
    }

    public void setmRank(String mRank) {
        this.mRank = mRank;
    }

    public int getmClear() {
        return mClear;
    }

    public void setmClear(int mClear) {
        this.mClear = mClear;
    }

    public TeamData(String mName, String mChar, String mRank, int mClear,int psta,Boolean isApp) {
        this.mName = mName;
        this.mChar = mChar;
        this.mRank = mRank;
        this.mClear=mClear;
        this.psta=psta;
        this.isApp=isApp;
    }
}
