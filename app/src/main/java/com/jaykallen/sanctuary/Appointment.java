package com.jaykallen.sanctuary;
// Created by Jay Kallen on 1/7/2017.

import android.os.Parcelable;

import java.io.Serializable;

public class Appointment implements Serializable{
    private String mId;
    private String mTime;
    private String mTitle;
    private String mDescription;
    private String mImage;
    private boolean mCompleted;

    public Appointment (String Id, String Time, String Title, String Description, String Image, boolean Completed) {
        mId = Id;
        mTime = Time;
        mTitle = Title;
        mDescription = Description;
        mImage = Image;
        mCompleted = Completed;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean completed) {
        mCompleted = completed;
    }

    public String getId() {
        return mId;
    }

    public String getTime() {
        return mTime;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setId(String id) {
        mId = id;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public void setTitle(String Title) {
        mTitle = Title;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String mImage) {
        this.mImage = mImage;
    }
}
