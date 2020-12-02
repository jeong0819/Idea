package com.example.realp;

import java.util.ArrayList;

public class CurProjectinfo {
    public static int teamNo;
    public static String projectName; //프로젝트명
    public static String teamName; //팀명
    public static String startDate; //시작기간
    public static String finishDate; //종료기간
    public static String pTheme; //팀주제
    public static int tpc=0; //팀인원수
    public static String curPath;
    public static ArrayList<String> worker= new ArrayList<>();
    public static ArrayList<String> wChar= new ArrayList<>();
    public static ArrayList<String> wRank= new ArrayList<>();

    public static String getCurPath() {
        return curPath;
    }

    public static void setCurPath(String curPath) {
        CurProjectinfo.curPath = curPath;
    }

    public int getTeamNo() {
        return teamNo;
    }

    public void setTeamNo(int teamNo) {
        CurProjectinfo.teamNo = teamNo;
    }

    public void clearData(){
        this.projectName="";
        this.teamName="";
        this.startDate="";
        this.finishDate="";
        this.pTheme="";
        this.tpc=0;
        this.worker= new ArrayList<>();
        this.wChar= new ArrayList<>();
        this.wRank= new ArrayList<>();
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
