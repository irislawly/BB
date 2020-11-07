package com.bcit.bb;

import java.io.Serializable;

public class BookingTemplate implements Serializable {
    private String equipment;
    private String timeslot;
    private String date;
    private String id;
    private String gymname;
    private String gymid;

    public String getEquipment() {
        return equipment;
    }

    public String getTimeslot() {
        return timeslot;
    }

    public String getDate() {
        return date;
    }
    public String getId() {
        return id;
    }
    public String getGymname() {
        return gymname;
    }
    public String getGymId() {
        return gymname;
    }

    public BookingTemplate(String equipment, String timeslot, String date, String id, String gymname, String gymid) {
        this.equipment = equipment;
        this.timeslot = timeslot;
        this.date = date;
        this.id = id;
        this.gymname = gymname;
        this.gymid = gymid;
    }
}
