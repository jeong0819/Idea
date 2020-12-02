package com.example.realp;

public class IngTogData {
    private String ingTogUser;
    private String ingTogTask;
    private String ingTogDate;
    private int ingTogDel;

    public IngTogData(String ingTogUser, String ingTogTask, String ingTogDate, int ingTogDel) {
        this.ingTogUser = ingTogUser;
        this.ingTogTask = ingTogTask;
        this.ingTogDate = ingTogDate;
        this.ingTogDel = ingTogDel;
    }

    public int getIngTogDel() {
        return ingTogDel;
    }

    public void setIngTogDel(int ingTogDel) {
        this.ingTogDel = ingTogDel;
    }

    public String getIngTogUser() {
        return ingTogUser;
    }

    public void setIngTogUser(String ingTogUser) {
        this.ingTogUser = ingTogUser;
    }

    public String getIngTogTask() {
        return ingTogTask;
    }

    public void setIngTogTask(String ingTogTask) {
        this.ingTogTask = ingTogTask;
    }

    public String getIngTogDate() {
        return ingTogDate;
    }

    public void setIngTogDate(String ingTogDate) {
        this.ingTogDate = ingTogDate;
    }
}
