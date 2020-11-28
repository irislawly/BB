package com.bcit.bb.adapters;

import java.io.Serializable;

/**
 * Booking template for adapter and retrieving information from database.
 */
public class BookingTemplate implements Serializable {
    private String equipment;
    private String timeslot;
    private String date;
    private String id;
    private String gymname;
    private String gymid;

    /**
     * Get equipment
     * @return equipment
     */
    public String getEquipment() {
        return equipment;
    }

    /**
     * Get timeslot.
     * @return get timeslot
     */
    public String getTimeslot() {
        return timeslot;
    }

    /**
     * Get date
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * Get id
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Get gymname
     * @return gymname
     */
    public String getGymname() {
        return gymname;
    }
    public String getGymId() {
        return gymname;
    }
    /** Consturctior */
    public BookingTemplate(String equipment, String timeslot, String date, String id, String gymname, String gymid) {
        this.equipment = equipment;
        this.timeslot = timeslot;
        this.date = date;
        this.id = id;
        this.gymname = gymname;
        this.gymid = gymid;
    }
}
