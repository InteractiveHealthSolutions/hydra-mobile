package com.ihsinformatics.dynamicformsgenerator.data.core;

/**
 * Created by Owais on 1/11/2018.
 */
public class FormMenu{
    private String name;
    private int thumbnail;

    public FormMenu(String name, int thumbnail) {
        this.name = name;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
