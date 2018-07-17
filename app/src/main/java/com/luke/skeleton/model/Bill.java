package com.luke.skeleton.model;

import org.joda.time.DateTime;

public class Bill {

    private float value;
    private DateTime date;

    public Bill(float value, DateTime date) {
        this.value = value;
        this.date = date;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }
}
