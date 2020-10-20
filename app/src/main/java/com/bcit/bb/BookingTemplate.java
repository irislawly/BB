package com.bcit.bb;

import java.io.Serializable;

public class BookingTemplate implements Serializable {
    private String equiment;
    private String timeslot;
    private String date;

    public String getEquiment() {
        return equiment;
    }

    public String getTimeslot() {
        return timeslot;
    }

    public String getDate() {
        return date;
    }

    public BookingTemplate(String equiment, String timeslot, String date) {
        this.equiment = equiment;
        this.timeslot = timeslot;
        this.date = date;
    }
}
