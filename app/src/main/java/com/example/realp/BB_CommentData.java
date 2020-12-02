package com.example.realp;

public class BB_CommentData {
   private String reWrite,reMemo,reDate;

    public BB_CommentData(String reWrite, String reMemo, String reDate) {
        this.reWrite = reWrite;
        this.reMemo = reMemo;
        this.reDate = reDate;
    }

    public String getReWrite() {
        return reWrite;
    }

    public void setReWrite(String reWrite) {
        this.reWrite = reWrite;
    }

    public String getReMemo() {
        return reMemo;
    }

    public void setReMemo(String reMemo) {
        this.reMemo = reMemo;
    }

    public String getReDate() {
        return reDate;
    }

    public void setReDate(String reDate) {
        this.reDate = reDate;
    }
}
