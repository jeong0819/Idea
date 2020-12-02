package com.example.realp;

public class ProjectData {
    private String tv_Pname;
    private String tv_Tname;
    private int iv_tpc;
    private int tv_tpc;
    private String tv_sd;
    private String tv_fd;

    public ProjectData(String tv_Pname, String tv_Tname, int iv_tpc, int tv_tpc, String tv_sd, String tv_fd) {
        this.tv_Pname = tv_Pname;
        this.tv_Tname = tv_Tname;
        this.iv_tpc = iv_tpc;
        this.tv_tpc = tv_tpc;
        this.tv_sd = tv_sd;
        this.tv_fd = tv_fd;
    }

    public String getTv_Pname() {
        return tv_Pname;
    }

    public void setTv_Pname(String tv_Pname) {
        this.tv_Pname = tv_Pname;
    }

    public String getTv_Tname() {
        return tv_Tname;
    }

    public void setTv_Tname(String tv_Tname) {
        this.tv_Tname = tv_Tname;
    }

    public int getIv_tpc() {
        return iv_tpc;
    }

    public void setIv_tpc(int iv_tpc) {
        this.iv_tpc = iv_tpc;
    }

    public int getTv_tpc() {
        return tv_tpc;
    }

    public void setTv_tpc(int tv_tpc) {
        this.tv_tpc = tv_tpc;
    }

    public String getTv_sd() {
        return tv_sd;
    }

    public void setTv_sd(String tv_sd) {
        this.tv_sd = tv_sd;
    }

    public String getTv_fd() {
        return tv_fd;
    }

    public void setTv_fd(String tv_fd) {
        this.tv_fd = tv_fd;
    }
}
