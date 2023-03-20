package com.saxion.sanderh.androidproject;

import java.util.Calendar;

public class GameDriver implements Comparable<GameDriver> {
    public String name;
    public String code;
    public int id;
    public int position;

    public boolean isSelected;

    int tireAgeLeft;
    int pitCount = 0;

    public Calendar totalLapTime;
    public Calendar gapToCarAhead;


    public GameDriver(int id, String name, String code, int position, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.position = position;
        this.isSelected = isSelected;

        setTireAge();

        totalLapTime = Calendar.getInstance();
        totalLapTime.setTimeInMillis(0);

        gapToCarAhead = Calendar.getInstance();
        gapToCarAhead.setTimeInMillis(0);
    }

    public void addMillis(int millis) {
        totalLapTime.setTimeInMillis(totalLapTime.getTimeInMillis() + millis);
    }

    public void setTireAge() {
        this.tireAgeLeft = GameActivity.getRandomNumber(20, 35);
    }

    public void setGap(long millils) {
        gapToCarAhead.setTimeInMillis(millils);
    }

    @Override
    public int compareTo(GameDriver driver) {
        if (totalLapTime == null || driver.totalLapTime == null) {
            return 0;
        }
        return totalLapTime.compareTo(driver.totalLapTime);
    }
}
