package com.sk.ultimateplayerhq.custom.calender;

import com.sk.ultimateplayerhq.models.EventModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MyDate implements Serializable {
    private Date date;
    private boolean isSelected = false;
    private List<EventModel> events;
    private boolean isEventIn = false;
    private boolean isTrainingIn = false;

    public MyDate(Date date, boolean isSelected, List<EventModel> events, boolean isEventIn, boolean isTrainingIn) {
        this.date = date;
        this.isSelected = isSelected;
        this.events = events;
        this.isEventIn = isEventIn;
        this.isTrainingIn = isTrainingIn;

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public List<EventModel> getEvents() {
        return events;
    }

    public void setEvents(List<EventModel> events) {
        this.events = events;
    }

    public boolean isEventIn() {
        return isEventIn;
    }

    public void setEventIn(boolean eventIn) {
        isEventIn = eventIn;
    }

    public boolean isTrainingIn() {
        return isTrainingIn;
    }

    public void setTrainingIn(boolean trainingIn) {
        isTrainingIn = trainingIn;
    }
}
