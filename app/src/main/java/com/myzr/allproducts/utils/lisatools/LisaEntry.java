package com.myzr.allproducts.utils.lisatools;

import java.io.Serializable;

public class LisaEntry implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getEveryDayNums() {
        return everyDayNums;
    }

    public void setEveryDayNums(int[] everyDayNums) {
        this.everyDayNums = everyDayNums;
    }

    public int getLeftNumsToToday() {
        return leftNumsToToday;
    }

    public void setLeftNumsToToday(int leftNumsToToday) {
        this.leftNumsToToday = leftNumsToToday;
    }

    public LisaEntry(String name, int[] everyDayNums, int leftNumsToToday, int indexDay) {
        this.name = name;
        this.everyDayNums = everyDayNums;
        this.leftNumsToToday = leftNumsToToday;
        this.indexDay = indexDay;
    }

    public int getIndexDay() {
        return indexDay;
    }

    public void setIndexDay(int indexDay) {
        this.indexDay = indexDay;
    }

    private int[]everyDayNums;
    private int leftNumsToToday;
    private int indexDay;
}
