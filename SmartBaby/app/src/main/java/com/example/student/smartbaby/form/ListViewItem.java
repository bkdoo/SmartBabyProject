package com.example.student.smartbaby.form;

import java.util.Date;

public class ListViewItem {
    private int boardId;
    private String regDate;
    private String sleepTime;
    private String wakeupTime;
    private String totalTime;
    private String dayNight;
    private String memo;

    public ListViewItem(int boardId, String regDate, String sleepTime, String wakeupTime, String totalTime, String dayNight, String memo) {
        this.boardId = boardId;
        this.regDate = regDate;
        this.sleepTime = sleepTime;
        this.wakeupTime = wakeupTime;
        this.totalTime = totalTime;
        this.dayNight = dayNight;
        this.memo = memo;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getDayNight() {
        return dayNight;
    }

    public void setDayNight(String dayNight) {
        this.dayNight = dayNight;
    }

    public String getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(String sleepTime) {
        this.sleepTime = sleepTime;
    }

    public String getWakeupTime() {
        return wakeupTime;
    }

    public void setWakeupTime(String wakeupTime) {
        this.wakeupTime = wakeupTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }
}
