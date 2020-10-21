package com.bcit.bb;

import java.io.Serializable;

public class BookingTemplate implements Serializable {
    private String equipment;
    private String timeslot;
    private String date;

    public String getEquipment() {
        return equipment;
    }

    public String getTimeslot() {
        return timeslot;
    }

    public String getDate() {
        return date;
    }

    public BookingTemplate(String equipment, String timeslot, String date) {
        this.equipment = equipment;
        this.timeslot = timeslot;
        this.date = date;
    }
}
