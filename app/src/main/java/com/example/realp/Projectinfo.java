package com.example.realp;

import java.util.ArrayList;

public class Projectinfo {
    public static int teamNo;
    public static String projectName; //프로젝트명
    public static String teamName; //팀명
    public static String startDate; //시작기간
    public static String finishDate; //종료기간
    public static String pTheme; //팀주제
    public static int tpc=0; //팀인원수
    public static ArrayList<String> pWorker= new ArrayList<>();
    public static ArrayList<String> tChar= new ArrayList<>();
    public static ArrayList<String> tRank= new ArrayList<>();

    public int getTeamNo() {
        return teamNo;
    }

    public void setTeamNo(int teamNo) {
        Projectinfo.teamNo = teamNo;
    }

    public void clearData(){
        this.projectName="";
        this.teamName="";
        this.startDate="";
        this.finishDate="";
        this.pTheme="";
        this.tpc=0;
        this.pWorker= new ArrayList<>();
        this.tChar= new ArrayList<>();
        this.tRank= new ArrayList<>();
    }
    public int getTpc() {
        return tpc;
    }

    public void setTpc(int tpc) {
        this.tpc = tpc;
    }

    public String getpTheme() {
        return pTheme;
    }

    public void setPTheme(String pTheme) {
        this.pTheme = pTheme;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }
}
