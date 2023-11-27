package model;

import model.builder.BookBuilder;

import java.time.LocalDate;

public class AudioBook extends Book{
    private int runTime;

    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    @Override
    public String toString() {
        return "AudioBook: " + super.toString() + String.format(", runTime = %d", runTime);
    }
}
