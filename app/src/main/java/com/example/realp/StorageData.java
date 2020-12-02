package com.example.realp;

public class StorageData {
    private String fileName;
    private String userName;
    private String openText;
    private String downText;
    private String delText;

    public StorageData(String fileName, String userName, String openText, String downText, String delText) {
        this.fileName = fileName;
        this.userName = userName;
        this.openText = openText;
        this.downText = downText;
        this.delText = delText;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOpenText() {
        return openText;
    }

    public void setOpenText(String openText) {
        this.openText = openText;
    }

    public String getDownText() {
        return downText;
    }

    public void setDownText(String downText) {
        this.downText = downText;
    }

    public String getDelText() {
        return delText;
    }

    public void setDelText(String delText) {
        this.delText = delText;
    }
}
