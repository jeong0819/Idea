package com.example.realp;

public class WorkerData {
    private String wName;
    private String wChar;
    private String wRank;

    public String getwName() {
        return wName;
    }

    public void setwName(String wName) {
        this.wName = wName;
    }

    public String getwChar() {
        return wChar;
    }

    public void setwChar(String wChar) {
        this.wChar = wChar;
    }

    public String getwRank() {
        return wRank;
    }

    public void setwRank(String wRank) {
        this.wRank = wRank;
    }

    public WorkerData(String wName, String wChar, String wRank) {
        this.wName = wName;
        this.wChar = wChar;
        this.wRank = wRank;
    }
}
